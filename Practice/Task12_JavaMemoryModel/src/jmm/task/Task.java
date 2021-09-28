package jmm.task;

import java.util.concurrent.Callable;

public class Task<T> {

    private final Callable<? extends T> callable;
    private volatile T result;
    private volatile ComputationalException exception;

    public Task(Callable<? extends T> callable) {
        this.result = null;
        this.exception = null;
        this.callable = callable;
    }

    public T get() throws ComputationalException {

        if (exception != null) {
            throw exception;
        } else if (result == null) {
            synchronized (this) {
                if (result == null) {
                    System.out.println("Begin computation " + Thread.currentThread().getName());
                    try {
                        result = callable.call();
                    } catch (Exception e) {
                        exception = new ComputationalException("Computation exception in Task ...", e);
                        throw exception;
                    }
                    System.out.println("End computation " + Thread.currentThread().getName());
                }
            }
        }
        return result;
    }
}
