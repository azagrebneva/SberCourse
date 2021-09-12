package proxy;

import annotation.Cache;
import annotation.CacheType;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.ClassLoader.getSystemClassLoader;
import static proxy.SerializationUtil.createZipFileName;


public class CachedAdvancedProxy implements InvocationHandler {

    private final File rootDir;
    private final Map<Object, Object> results = new HashMap<>();
    private final Object delegate;

    /**
     * Настройка кеширующего прокси
     * @param folder - папка в которой хранятся данные с сериализацией
     */
    public CachedAdvancedProxy(File folder) {
        this.delegate = null;
        if (folder == null) {
            folder = new File(System.getProperty("user.dir") + File.separator + "cache");
            if (!folder.exists()) {
                folder.mkdir();
            }
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Переданный путь не является директорией.");
        }
        this.rootDir = folder;
    }

    private CachedAdvancedProxy(Object delegate, File folder) {
        if (delegate == null) {
            throw new IllegalArgumentException("Передан null .");
        }
        this.delegate = delegate;
        this.rootDir = folder;
    }

    /**
     * Создает объект Кеширующий прокси
     * @param delegate
     * @param <T> интерфейс объекта
     * @return интерфейс с кешированием
     */
    public <T> T cache(Object delegate) {
        return (T) Proxy.newProxyInstance(getSystemClassLoader(),
                delegate.getClass().getInterfaces(),
                new CachedAdvancedProxy(delegate, rootDir));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if(!method.isAnnotationPresent(Cache.class)){
            return method.invoke(delegate, args);
        }

        Cache annotation = method.getAnnotation(Cache.class);

        Object invokeReturnObject = null;
        if (annotation.cacheType() == CacheType.FILE){
            invokeReturnObject = cacheInFile(method, args);
        } else if (annotation.cacheType() == CacheType.IN_MEMORY){
            invokeReturnObject = cacheInMemory(method, args);
        }

        return invokeReturnObject;
    }

    private Object cacheInMemory(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object result = null;
        Object[] chosenArguments = chosenArguments(method, args);
        if(!results.containsKey(getKey(method, chosenArguments))){
            result =  method.invoke(delegate, args);
            result = checkItemsAmountToCache(method, result);
            results.put(getKey(method,chosenArguments), result);
        } else {
            result = results.get(getKey(method,chosenArguments));
        }
        return result;
    }

    private Object cacheInFile(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object result = null;

        Object[] chosenArguments = chosenArguments(method, args);
        File file = new File(generateFileName(method, chosenArguments));
        File zipFile = new File(createZipFileName(file));

        boolean isZip =  method.getAnnotation(Cache.class).zip();
        try {
            if ((!isZip && !file.exists()) ||
                    (isZip && !zipFile.exists()) ) {
                result = serialize(file, method, args);
            } else {
                result = deserialize(file, method, args);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибки при кешировании при работе с файлом "
                    + file.getName() + ".", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибки при десериализации.", e);
        }

        return result;
    }

   public Object serialize(File file, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, IOException {
        Object result = method.invoke(delegate, args);
        result = checkItemsAmountToCache(method, result);
        SerializationUtil.serialize(new Result(result), file);

        if (method.getAnnotation(Cache.class).zip()) {
            SerializationUtil.zipFile(file);
            if (file.exists())
                file.delete();
        }
        return result;
    }

    public Object deserialize(File file, Method method, Object[] args) throws IOException, ClassNotFoundException {
        Object result = null;
        if (!method.getAnnotation(Cache.class).zip()) {
            Result myResult = SerializationUtil.deserialize(file);
            result = myResult.getResult();
        } else {
            File zipFile = new File(createZipFileName(file));
            if (zipFile.exists()) {
                ZipFile zip = new ZipFile(zipFile);
                Enumeration<? extends ZipEntry> entries = zip.entries();
                if (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    InputStream inputStream = zip.getInputStream(zipEntry);
                    List<Integer> buffer = new ArrayList<>();

                    while (inputStream.available() > 0) {
                        buffer.add(inputStream.read());
                    }
                    byte[] bytes = new byte[buffer.size()];
                    for (int i = 0; i < buffer.size(); i++) {
                        Integer integer = buffer.get(i);
                        bytes[i] = (byte)integer.intValue();
                    }
                    Result myResult = SerializationUtil.deserialize(bytes);
                    result = myResult.getResult();
                }
            }
        }
        return result;
    }

    public static Object[] chosenArguments(Method method, Object[] args) {
        Cache cache = method.getAnnotation(Cache.class);
        Class<?>[] classesFromCache = cache.identityBy();
        if (classesFromCache.length == 0 || classesFromCache.length == args.length)
            return args;

        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> resultArgs = new ArrayList<>();

        for (int i = 0; i < parameterTypes.length; i++) {
            for (int j = 0; j < classesFromCache.length; j++) {
                if (parameterTypes[i].equals(classesFromCache[j])) {
                    resultArgs.add(args[i]);
                    classesFromCache[j] = null;
                }
            }
        }
        return resultArgs.toArray();
    }

    public static Object checkItemsAmountToCache(Method method, Object value) {
        if (method.getReturnType().equals(List.class)) {
            List<Object> result = (List<Object>) value;
            int toIndex = method.getAnnotation(Cache.class).maxItemsAmountInList();
            if ((toIndex!=-1) && (toIndex < result.size()))
                result = new ArrayList<>(result.subList(0, toIndex));
            return result;
        } else
            return value;
    }

    private Object getKey(Method method, Object[] args) {
        List<Object> key = new ArrayList<>();
        key.add(method);
        key.addAll(Arrays.asList(args));
        return key;
    }

    public String generateFileName(Method method, Object[] args) {
        String prefix = method.getAnnotation(Cache.class).fileNamePrefix();
        if ("".equals(prefix))
            prefix = method.getName();
        StringBuilder sb = new StringBuilder();
        sb.append(prefix)
                .append("_");

        for (Object arg : args) {
            sb.append(arg).append("_");
        }
        sb.replace(sb.lastIndexOf("_"), sb.lastIndexOf("_"), ".out");

        String fileName = sb.toString();
        fileName = fileName.substring(0, fileName.lastIndexOf("_"));

        return (rootDir.getPath() + File.separator + fileName);
    }
}
