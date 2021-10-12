package cache.calculator;

import cache.annotation.Cachable;
import database.dao.FibonacciDao;
import database.dao.FibonacciDaoImpl;
import handlers.FibonacciServiceHandler;

import java.util.List;

public interface Calculator {
    /**
    * Расчет чисел Фибоначчи
    * @param number
    */
    @Cachable(FibonacciServiceHandler.class)
    List<Integer> fibonacci (int number);
}
