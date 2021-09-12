package loader;

import java.util.*;

public class LoaderImpl implements Loader {

    @Override
    public Map<String,Integer> doSomething(String status) {
        // каким-то образом формируем отображение
        Map<String,Integer> map = new TreeMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(status + String.format("%03d", i), i*i);
        }
        // эмуляция длительных вычислений
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
