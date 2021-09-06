package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PerformanceProxy implements InvocationHandler {
    Object delegate;

    public PerformanceProxy(Calculator delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(!method.isAnnotationPresent(Metric.class)){
            return method.invoke(delegate, args);
        }
        System.out.println("Метод: " + method.getName() + " вызван.");
        Long before = System.currentTimeMillis();
        Object invokeReturnObject = method.invoke(delegate, args);
        Long after = System.currentTimeMillis();
        System.out.println("Метод: " + method.getName() + " завершен, время: "+ (after-before));

        return invokeReturnObject;
    }
}
