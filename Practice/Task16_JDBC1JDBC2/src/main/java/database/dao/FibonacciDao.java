package database.dao;

import java.util.List;

public interface FibonacciDao {

    /**
     * Получить все числа Фибоначчи от 0 до number включительно
     * @param number - число
     * @return все числа Фибоначчи до числа number
     */
    List<Integer> getFibonacciNumbers(int number);

    /**
     * Создает записи в базе данных с числами Фибоначчи
     * @param numbers последовательность чисел Фибоначчи
     * @return true - запись в базу данных сделана
     */
    boolean createFibonacciNumbers(List<Integer> numbers);


}
