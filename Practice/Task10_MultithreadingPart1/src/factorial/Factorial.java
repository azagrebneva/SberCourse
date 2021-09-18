package factorial;

import java.math.BigInteger;

public class Factorial {

    /**
     * Recursively calculates the factorial of it's argument
     * @param number positive integer number
     * @return factorial of the argument
     */
    public static BigInteger factSequentialRecursive(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number in factorial must be >= 0, but receive " + number +".");
        }

        if ((number == 0) || (number == 1)) {
            return BigInteger.ONE;
        }

        return BigInteger.valueOf(number).multiply(factSequentialRecursive(number - 1));
    }

    /**
     * Calculates the factorial of its argument in several threads
     * @param number positive integer number
     * @return factorial of the argument
     */
    public static BigInteger factMultithreaded(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number in factorial must be >= 0, but receive " + number +".");
        }
        if ((number == 0) || (number == 1)) {
            return BigInteger.ONE;
        }

        final int NUM_THREADS = 4;
        final int STEP = number/NUM_THREADS;

        if (STEP < 2) {
            return factPartition(1, number);
        }

        BigInteger[] res = new BigInteger[NUM_THREADS];
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            int index = i;
            int begin = i*STEP;
            int end = (i == NUM_THREADS - 1) ? number : (i+1)*STEP - 1;
            threads[i] = new Thread(() -> {
                res[index] = Factorial.factPartition(begin, end);
             });
            threads[i].start();
        }

        waitForThreads(threads);

        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < NUM_THREADS; i++) {
           result = result.multiply(res[i]);
        }

        return result;
    }

    private static BigInteger factPartition(int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("Begin > end in partition factorial computation");
        }
        begin = begin > 0 ? begin : 1;
        BigInteger result = BigInteger.ONE;
        for (int i = begin; i <= end; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    public static void waitForThreads(Thread[] threads){
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Thread is interrupted.", e);
            }
        }
    }
}
