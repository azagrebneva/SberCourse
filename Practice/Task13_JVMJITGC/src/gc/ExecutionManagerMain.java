package gc;

import java.util.concurrent.TimeUnit;

/**
 * Задание 13. JVM, JIT, GC
 * 13.2. Из %JAVA_HOME%\bin запустить jvisualvm, установить через пункт меню
 * Tools\Plugins\Available Plugis плагин: Visual GC
 * Запустить приложение создающее много объектов с разными GC,
 * посмотреть в jvisualvm как заполняются объекты в разных
 * областях памяти(heap)
 */


public class ExecutionManagerMain {

    public static void main(String[] args) {

        ExecutionManager manager = new TaskExecutionManager();

        Context context = manager.execute(() -> System.out.println("All is done!!!"),
                new Task(1), new Task(11),
                new Task(2), new Task(12),
                new Task(3), new Task(13),
                new Task(4), new Task(14),
                new Task(5), new Task(15),
                new Task(6), new Task(16),
                new Task(7),
                new Task(8),
                new Task(9),
                new Task(10));

        try {
            Thread.sleep(20000);
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
                Long duration = (long) (Math.random() * 20);
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End computation " + number + "! " + Thread.currentThread().getName());
        }
    }
}
