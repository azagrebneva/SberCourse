import cache.calculator.Calculator;
import cache.calculator.CalculatorImpl;
import cache.proxy.CacheProxy;
import database.connection.DataSourceHelper;

import java.lang.reflect.Proxy;

/**
 * Задание 16. Работа с базами данных в Java
 * Разработать продвинутый кэш, который помнит о
 * кэшированных данных после перезапуска приложения
 */

public class FibonacciMain {

    public static void main(String[] args) {

        DataSourceHelper.createDb();

        Calculator calculator = new CalculatorImpl();

        Calculator cachingCalculator = (Calculator) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                calculator.getClass().getInterfaces(),
                new CacheProxy(calculator));

        System.out.println("Результат:" + cachingCalculator.fibonacci(3));
        System.out.println("Результат:" + cachingCalculator.fibonacci(10));
        System.out.println("Результат:" + cachingCalculator.fibonacci(10));
    }
}
