//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import threadpool.FixedThreadPool;
import threadpool.ScalableThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeUnit;

/**
 * Задание 11 Многопоточность (часть 2)
 * Сделать 2 реализации ThreadPool.
 * 11.1 FixedThreadPool - Количество потоков задается в конструкторе и не меняется.
 * 11.2 ScalableThreadPool в конструкторе задается минимальное
 * и максимальное(int min, int max) число потоков,
 * количество запущенных потоков может быть увеличено от
 * минимального к максимальному, если при добавлении
 * нового задания в очередь нет свободного потока для
 * исполнения этого задания. При отсутствии задания в
 * очереди, количество потоков опять должно быть уменьшено
 * до значения min.
 */


public class ThreadPoolMain {

    public static void main(String[] args) {

        // fixed thread pool

        FixedThreadPool fixedThreadPool = new FixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            fixedThreadPool.execute(new Task(i));
        }
        System.out.println(fixedThreadPool.toString());
        fixedThreadPool.shutdown();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(fixedThreadPool.toString());

        System.out.println("\n\n");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executor.execute(new Task(i));
        }
        System.out.println(executor.toString());
        executor.shutdown();
        System.out.println(executor.toString());

        // Scalable thread pool

        System.out.println("\n\n");
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(3, 7);
        for (int i = 0; i < 10; i++) {
            scalableThreadPool.execute(new Task(i));
        }
        System.out.println(scalableThreadPool.toString());
        scalableThreadPool.shutdown();

    }

    static class Task implements Runnable {
        final int workNumber;
        public Task(int workNumber) {
            this.workNumber = workNumber;
        }
        @Override
        public void run() {
            try {
                Long duration = (long) (Math.random() * 5);
                System.out.println("Running Task " +
                        workNumber + "! Thread Name: " +
                        Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(duration);
                System.out.println("Task "+
                        workNumber + " completed! Thread Name: " +
                        Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println("In run...");
                e.printStackTrace();
            }
        }
    }

}
