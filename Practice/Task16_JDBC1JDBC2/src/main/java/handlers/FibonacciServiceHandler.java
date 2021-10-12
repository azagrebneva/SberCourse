package handlers;

import database.dao.FibonacciDao;
import database.dao.FibonacciDaoImpl;

import java.lang.reflect.Method;
import java.util.List;

public class FibonacciServiceHandler implements IHandler {

    @Override
    public Object execute(Object delegate, Method method, Object[] args) throws Throwable {

        System.out.println("--- расчет чисел Фибоначчи в сервисе ------ ");
        Object result;
        int number = (int) args[0];

        FibonacciDao fibonacciDao = new FibonacciDaoImpl();
        result = fibonacciDao.getFibonacciNumbers(number);

        if (((List<Integer>) result).size() == 0) { // чисел в базе нет
            System.out.println("Вычисление метода: " + method.getName());
            result = method.invoke(delegate, args);
            fibonacciDao.createFibonacciNumbers((List<Integer>) result);
        }

        return result;
    }
}
