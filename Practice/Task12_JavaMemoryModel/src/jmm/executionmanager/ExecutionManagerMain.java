package jmm.executionmanager;

import java.util.concurrent.TimeUnit;

/**
 * Задание 12.2 Модель памяти в Java
 * Ваша задача реализовать интерфейс ExecutionManager
 *
 * public interface ExecutionManager {
 *     Context execute(Runnable callback, Runnable... tasks);
 * }
 *
 * Метод execute принимает массив тасков, это задания которые
 * ExecutionManager должен выполнять параллельно (в вашей реализации
 * пусть будет в своем пуле потоков). После завершения всех тасков
 * должен выполниться callback (ровно 1 раз).
 * Метод execute – это неблокирующий метод, который сразу возвращает
 * объект Context. Context это интерфейс следующего вида:
 *
 * public interface Context {
 *     int getCompletedTaskCount();
 *     int getFailedTaskCount();
 *     int getInterruptedTaskCount();
 *     void interrupt();
 *     boolean isFinished();
 * }
 * Метод getCompletedTaskCount() возвращает количество тасков, которые на текущий момент успешно выполнились.
 * Метод getFailedTaskCount() возвращает количество тасков, при выполнении которых произошел Exception.
 * Метод interrupt() отменяет выполнения тасков, которые еще не начали выполняться.
 * Метод getInterruptedTaskCount() возвращает количество тасков, которые не были выполены из-за отмены (вызовом предыдущего метода).
 * Метод isFinished() вернет true, если все таски были выполнены или отменены, false в противном случае.
 */


public class ExecutionManagerMain {

    public static void main(String[] args) {

        ExecutionManager manager = new TaskExecutionManager();

        Context context = manager.execute(() -> System.out.println("All is done!!!"),
                new Task(1),
                new Task(2),
                new Task(3),
                new Task(4),
                new Task(5),
                new Task(6),
                new Task(7));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        context.interrupt();
        System.out.println("Completed task: " + context.getCompletedTaskCount());
        System.out.println("Is finished: " + context.isFinished());
        System.out.println("Amount of failed tasks: " + context.getFailedTaskCount());
        System.out.println("Amount of interrupted tasks: " + context.getInterruptedTaskCount());
    }

    static class Task implements Runnable {
        final int number;

        public Task(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            System.out.println("Begin computation " + number + "! " + Thread.currentThread().getName());
            if (number % 2 == 0)
                throw new RuntimeException("Execution mistakes in computation " + number + " ... ");
            try {
                Long duration = (long) (Math.random() * 5);
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End computation " + number + "! " + Thread.currentThread().getName());
        }
    }
}
