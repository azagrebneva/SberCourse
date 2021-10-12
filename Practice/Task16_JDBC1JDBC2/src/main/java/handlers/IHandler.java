package handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface IHandler {

    public Object execute(Object delegate, Method method, Object[] args) throws Throwable;
}
