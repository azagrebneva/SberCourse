package proxy;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class CacheAdvancedProxy implements InvocationHandler {
    // хранит пары параметр значение
    private final Map<Object, Object> results = new HashMap<>();
    private final Object delegate;

    public CacheAdvancedProxy(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!method.isAnnotationPresent(Cache.class)){
            return method.invoke(delegate, args);
        }

        // проверяем является ли объект возвращающий значение Serializable
        boolean isSerializable = isReturnValueSerializable(method);
        System.out.println("ser "+isSerializable);

        Cache annotation = method.getAnnotation(Cache.class);
        System.out.println(annotation.value());



        // кешируем с помощью файла
        if (annotation.value()){

            // проверяем является ли объект Serializable

            // проверяем сеществует ли файл

            //

            //Сериализация в файл с помощью класса ObjectOutputStream
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
//                    new FileOutputStream("person.out"));
//            objectOutputStream.writeObject(igor);
//            objectOutputStream.writeObject(renat);
//            objectOutputStream.close();


            //String fileName = getFileName(method, args);
            //System.out.println(fileName);
        }



//        Object invokeReturnObject;
//        if(!results.containsKey(key(method, args))){
//            System.out.println("Вычисление метода: "+ method.getName());
//            invokeReturnObject =  method.invoke(delegate, args);
//            results.put(key(method,args), invokeReturnObject);
//        } else {
//            System.out.println("Считывание из оперативной памяти для метода: "+ method.getName());
//            invokeReturnObject = results.get(key(method,args));
//        }
        return method.invoke(delegate, args);
    }

    public static boolean isReturnValueSerializable(Method method){
        Class returnType = method.getReturnType();
        Class[] interfaces = returnType.getInterfaces();
        //Serializable.class.isInstance(someObj))
        Arrays.stream(interfaces).iterator().forEachRemaining(System.out::println);
        System.out.println(interfaces.length);
        return Arrays.asList(interfaces).contains(Serializable.class);
    }


    private Object getKey(Method method, Object[] args) {
        List<Object> key = new ArrayList<>();
        key.add(method);
        key.addAll(Arrays.asList(args));
        return key;
    }

    private String getFileName(Method method, Object[] args) {
        List<Object> object = new ArrayList<>();
        object.add(method);
        object.addAll(Arrays.asList(args));
        return (String.valueOf(object.hashCode())+".txt");
    }
}
