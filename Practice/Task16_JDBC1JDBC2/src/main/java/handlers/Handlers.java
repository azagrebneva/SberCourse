package handlers;

import database.dao.FibonacciDao;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализован паттерн фабричный метод
 */
public enum Handlers {
    FIBONACCHI(FibonacciServiceHandler.class,
            new FibonacciServiceHandler(),
            "Кеширование чисел Фибоначчи.");

    private final static Map<Class<?>, IHandler> HANDLERS = Arrays.stream(Handlers.values())
            .map(e -> new AbstractMap.SimpleEntry<>(e.getServiceCode(),e.getHandler()))
            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    private final Class<?> serviceCode;
    private final IHandler handler;
    private final String description;

    Handlers(Class<?> code, IHandler handler, String description) {
        this.serviceCode = code;
        this.handler = handler;
        this.description = description;
    }

    public Class<?> getServiceCode() {
        return serviceCode;
    }

    public IHandler getHandler() {
        return handler;
    }

    public static IHandler findHandlerByCode(final Class<?> code){
        return HANDLERS.get(code);
    }
}
