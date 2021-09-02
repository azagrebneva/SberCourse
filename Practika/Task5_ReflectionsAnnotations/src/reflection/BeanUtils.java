package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtils {

    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */
    public static void assign(Object to, Object from) throws InvocationTargetException, IllegalAccessException {
        // "to" должны быть предками "from"
        if(!to.getClass().isAssignableFrom(from.getClass())) return;

        // собираем все геттеры и сеттеры
        List<Method> getters =  getAllGetters(from);
        List<Method> setters =  getAllSetters(to);

        // составляем пары геттеров и сеттеров
        Map<Method, Method> pairs = choosePairs(getters, setters);

        // присваиваем значения
        for (Map.Entry<Method, Method> pair : pairs.entrySet()) {
            Method getter = pair.getKey();
            Method setter = pair.getValue();
            Object object = getter.invoke(from);
            setter.invoke(to, object);
        }
    }

    private static boolean isGetter(Method method) {
        if (!method.getName().startsWith("get")) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        return !void.class.equals(method.getReturnType());
    }

    private static boolean isSetter(Method method){
        if (!method.getName().startsWith("set")) {
            return false;
        }
        return method.getParameterTypes().length == 1;
    }

    private static List<Method> getAllGetters(Object object) {
        List<Method> getters = new ArrayList<>();
        Method[] publicMethods = object.getClass().getMethods();
        for (Method method : publicMethods) {
            if (isGetter(method)) {
                getters.add(method);
            }
        }
        return getters;
    }

    private static List<Method> getAllSetters(Object object) {
        List<Method> setters = new ArrayList<>();
        Method[] publicMethods = object.getClass().getMethods();
        for (Method method : publicMethods) {
            if (isSetter(method)) {
                setters.add(method);
            }
        }
        return setters;
    }

    private static Map<Method, Method> choosePairs(List<Method> getters, List<Method> setters) {
        Map<Method, Method> pairs = new HashMap<>();
        for (Method getter : getters) {
            for (Method setter : setters) {
                if (isPair(getter, setter)){
                    pairs.put(getter, setter);
                }
            }
        }
        return pairs;
    }

    private static boolean isPair(Method getter, Method setter) {
        if (getter.getName().substring(3).compareTo(setter.getName().substring(3)) != 0) return false;
        Class<?> getterType = getter.getReturnType();
        Class<?> setterType = setter.getParameterTypes()[0];
        return setterType.isAssignableFrom(getterType);
    }
}