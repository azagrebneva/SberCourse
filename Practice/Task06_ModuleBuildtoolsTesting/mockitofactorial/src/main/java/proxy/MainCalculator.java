package proxy;

import java.lang.reflect.Proxy;

/**
 * Задание 5.1 Рефлексия
 *  5.1 Имплементировать интерфейс Calculator в классе CalculatorImpl
 *  5.5 Реализовать кэширующий прокси
 *  5.6 Создать свою аннотацию Metric. Реализовать proxy класс PerformanceProxy, который в случае если метод аннотирован Metric будет выводить на консоль время выполнения метода.
 */

public class MainCalculator {

    public static void main(String[] args) {

        System.out.println("5.1 Имплементировать интерфейс Calculator в классе CalculatorImpl.");
        Calculator calculator = new CalculatorImpl();

        System.out.println("\n5.5a Реализовать кэширующий прокси.");
        Calculator cachingCalculator = (Calculator) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                calculator.getClass().getInterfaces(),
                new CacheProxy(calculator));
        System.out.println("Результат:" + cachingCalculator.calc(3));
        System.out.println("Результат:" + cachingCalculator.calc(25));
        System.out.println("Результат:" + cachingCalculator.calc(25));

        System.out.println("\n5.5б Реализовать кэширующий прокси с возможностью выбора способа кеширования.");
        cachingCalculator = (Calculator) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                calculator.getClass().getInterfaces(),
                new CacheAdvancedProxy(calculator));
        System.out.println("Результат:" + cachingCalculator.calc(3));
        System.out.println("Результат:" + cachingCalculator.calc(25));
        System.out.println("Результат:" + cachingCalculator.calc(25));

        System.out.println("\n5.6 Создать свою аннотацию Metric. Реализовать proxy класс PerformanceProxy, который в случае если метод аннотирован Metric будет выводить на консоль время выполнения метода.");
        Calculator performanceCalculator = (Calculator) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                calculator.getClass().getInterfaces(),
                new PerformanceProxy(calculator));
        System.out.println("Результат:" + performanceCalculator.calc(3));
        System.out.println("Результат:" + performanceCalculator.calc(25));
    }
}
