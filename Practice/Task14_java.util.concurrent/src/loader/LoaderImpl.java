package loader;

import java.util.*;

public class LoaderImpl implements Loader {

    @Override
    public Map<String,Integer> doSomething(String status) {
        System.out.println("Begin computation " + status + " " + Thread.currentThread().getName());

        // каким-то образом формируем отображение
        Map<String,Integer> map = new TreeMap<>();
        for (int i = 1; i <= 50; i++) {
            map.put(status + String.format("_%02d", i), i*i);
        }
        // эмуляция длительных вычислений
        try{
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("End computation " + status + " " + Thread.currentThread().getName());
        return map;
    }
}
