package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class CacheProxy implements InvocationHandler {

    // хранит пары параметр значение
    private final Map<Object, Object> results = new HashMap<>();
    private final Object delegate;

    public CacheProxy(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!method.isAnnotationPresent(Cache.class)){
            return method.invoke(delegate, args);
        }

        Object invokeReturnObject;
        if(!results.containsKey(key(method, args))){
            System.out.println("Вычисление метода: "+ method.getName());
            invokeReturnObject =  method.invoke(delegate, args);
            results.put(key(method,args), invokeReturnObject);
        } else {
            System.out.println("Считывание из кеша для метода: "+ method.getName());
            invokeReturnObject = results.get(key(method,args));
        }
        return invokeReturnObject;
    }

    private Object key(Method method, Object[] args) {
        List<Object> key = new ArrayList<>();
        key.add(method);
        key.addAll(Arrays.asList(args));
        return key;
    }
}
