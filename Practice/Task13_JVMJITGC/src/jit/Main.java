package jit;

import java.util.HashMap;
import java.util.Map;

/**
 * Задание 13. JVM, JIT, GC
 * 13.1. Сделать цикл на 100000 итераций, в цикле в предварительно созданную Map<Integer, String> сложить ключ - индекс, значение - "value" + индекс
 * Запустить с опцией -XX:+PrintCompilation, проанализировать информацию в консоли
 * Запустить с опцией -XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining , проанализировать информацию в консоли
 */

public class Main {
    final public static int LOOP_SIZE = 100_000;


    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < LOOP_SIZE; i++) {
            map.put(i, "string" + i);
        }

        System.out.println("End of execution.");

        // java -XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining  -classpath ./out/production/Ta
        //sk13_JVMJITGC jit.Main
        //java -XX:+PrintCompilation -classpath ./out/production/Task13_JVMJITGC jit.Main
    }
}
