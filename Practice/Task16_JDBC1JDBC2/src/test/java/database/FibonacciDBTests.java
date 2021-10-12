package database;

import database.connection.DataSourceHelper;
import database.dao.*;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FibonacciDBTests {

    private static FibonacciDao fibonacciDao;

    @BeforeAll
    public static void createDao() throws SQLException {
        DataSourceHelper.createDb();
        fibonacciDao = new FibonacciDaoImpl();
        Server.createTcpServer().start();
    }

    @Test
    public void insertAndGetListOfFibonacciNumbers() {

        System.out.println("Fibonacci numbers for insertion");
        List<Integer> numbers = new ArrayList<>(
                Arrays.asList(0, 1, 1, 2, 3, 5));
        numbers.forEach(n -> System.out.print(n + " "));

        fibonacciDao.createFibonacciNumbers(numbers);

        List<Integer> fibs = fibonacciDao.getFibonacciNumbers(5);
        System.out.println("\nFibonacci numbers from base");
        fibs.forEach(n -> System.out.print(n + " "));
    }

    @Test
    public void getListUnknownFibonacciNumbers() {

        List<Integer> numbers = new ArrayList<>(
                Arrays.asList(0, 1, 1, 2, 3, 5));
        fibonacciDao.createFibonacciNumbers(numbers);

        List<Integer> fibs = fibonacciDao.getFibonacciNumbers(6);
        System.out.println("\nFibonacci numbers from base");
        fibs.forEach(n -> System.out.print(n + " "));
    }

}
