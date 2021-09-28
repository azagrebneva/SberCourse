package jmm.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Задание 12 Модель памяти в Java
 * 12.1 Ваша задача реализовать класс Task имеющий один метод get():
 * public class Task<T> {
 *     …
 *     public Task(Callable<? extends T> callable) {
 *         //...
 *      }
 *     public T get() {
 *          ….. // todo implement me
 *     }
 * }
 * Данный класс в конструкторе принимает экземпляр java.util.concurrent.Callable.
 * Callable похож на Runnuble, но результатом его работы является объект (а не void).
 * Ваша задача реализовать метод get() который возвращает результат работы Callable.
 * Выполнение callable должен начинать тот поток, который первый вызвал метод get().
 * Если несколько потоков одновременно вызывают этот метод, то выполнение должно
 * начаться только в одном потоке, а остальные должны ожидать конца выполнения
 * (не нагружая процессор).
 * Если при вызове get() результат уже просчитан, то он должен вернуться сразу,
 * (даже без задержек на вход в синхронизированную область).
 * Если при просчете результата произошел Exception, то всем потокам при вызове get(),
 * надо кидать этот Exception, обернутый в ваш RuntimeException
 * (подходящее название своему ексепшену придумайте сами).
 */
public class TaskMain{

    public static void main(String[] args) {

        Task<String> taskWithException = new Task<>(() -> {
            throw new RuntimeException("Computation exception in callable ... ");
        });
        Task<String> task = new Task<>(() -> "callable result");

        List<Future<String>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            Future<String> result = executor.submit(() -> task.get());
            futures.add(result);
        }
        executor.shutdown();

        for (Future<String> future : futures){
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}