package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceImpl implements Service {

    @Override
    public List<String> run(String item, double value, Date date) {
        // какам-то образом формируем список
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i) + " " + item + " " + String.valueOf(value) + " " + date.getTime());
        }
        // эмуляция длителдьных вычислений
        try{
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<String> work(String item) {
        // какам-то образом формируем список
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i) + " " + item);
        }
        // эмуляция длителдьных вычислений
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
