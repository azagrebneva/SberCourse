package cache.proxy;

import cache.annotation.Cachable;
import handlers.Handlers;
import handlers.IHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CacheProxy implements InvocationHandler {

    private final Object delegate;

    public CacheProxy(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        IHandler handler = null;

        if (method.isAnnotationPresent(Cachable.class)){
            handler = Handlers.findHandlerByCode(method.getAnnotation(Cachable.class).value());
        }

        if (handler == null) {
            return method.invoke(delegate, args);
        }

        return handler.execute(delegate, method, args);
    }

}
