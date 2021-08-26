package parameterization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CountMapImpl <T> implements CountMap<T> {

    private Map<T, Integer> map = null;

    public CountMapImpl() {
        map = new HashMap<>();
    }

    @Override
    public void add(T key) {
        Integer count = map.get(key);
        if (null == count) {
            map.put(key, 1);
        } else {
            map.replace(key, count + 1);
        }
    }

    // добавляет элемент и количество встреч
    public void add(T key, Integer value) {
        Integer count = map.get(key);
        if (null == count) {
            map.put(key, value);
        } else {
            map.replace(key, count + value);
        }
    }

    @Override
    // возвращает количество добавлений данного элемента
    public int getCount(T key) {
        return map.get(key);
    }

    @Override
    // удаляет элемент из контейнера и возвращает количество его добавлений (до удаления)
    public int remove(T key) {
        Integer count = map.get(key);
        if (null == count) { return 0;}
        map.remove(key);
        return count;
    }

    @Override
    // количество разных элементов
    public int size() {
        return map.size();
    }

    @Override
    // Добавить все элементы из source в текущий контейнер,
    // при совпадении ключей, суммировать значения
    public void addAll(CountMap<? extends T> source) {
        if (source != null){
            Map<? extends T, Integer> sourceMap = source.toMap();
            sourceMap.forEach((k, v) -> {this.add(k, v);});
        }
    }

    @Override
    //Вернуть java.util.Map. ключ - добавленный элемент,
    // значение - количество его добавлений
    public Map<T, Integer> toMap() {
        Map<T, Integer> rst = new HashMap<>(map);
        return rst;
    }

    @Override
    //Тот же самый контракт как и toMap(), только всю информацию записать в destination
    public void toMap(Map<T, Integer> destination) {
        if (destination != null) {
            destination.clear();
            destination.putAll(map);
        }
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
