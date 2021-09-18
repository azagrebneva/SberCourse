package factorial;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Задание 10. Многопоточность (часть 1)
 * Дан файл содержащий несколько случайных натуральных чисел от 1 до 50.
 * Необходимо написать многопоточное приложение, которое параллельно рассчитает
 * и выведет в консоль факториал для каждого числа из файла.
 */

public class FactorialMain {

    public static void main(String[] args) {

        List<Integer> numbers = getPositiveNumbers("numbers.txt");

        System.out.println("----------------------------" +
                "\nRecursive factorial computation");
        Thread[] threads = createThreads(numbers, Factorial::factSequentialRecursive);
        startThreads(threads);
        Factorial.waitForThreads(threads);


        System.out.println("----------------------------" +
                "\nMultithreading factorial computation");
        threads = createThreads(numbers, Factorial::factMultithreaded);
        startThreads(threads);
    }

    private static void startThreads(Thread[] threads){
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }

    private static Thread[] createThreads(List<Integer> numbers, Function<Integer, BigInteger> function){
        Thread[] threads = new Thread[numbers.size()];

        for (int i = 0; i < threads.length ; i++) {
            Integer number = numbers.get(i);
            threads[i] = new Thread(() -> {
                BigInteger result = function.apply(number);
                System.out.println(String.format(
                        "#thread \"%s\", fact(%d) = %s",
                        Thread.currentThread().getName(), number, result.toString()));
            });
        }
        return threads;
    }

    private static List<Integer> getPositiveNumbers(String filename) {
        File file = new File(filename);
        List<Integer> numbers = new ArrayList<>();
        try {
            Scanner scanner = null;
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                int number = Integer.parseInt(scanner.nextLine());
                if (number > 0) {
                    numbers.add(number);
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("File "+ filename + " wasn't found.", e);
        }
        return numbers;
    }
}
