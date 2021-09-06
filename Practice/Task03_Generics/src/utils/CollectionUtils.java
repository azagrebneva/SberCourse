package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
    Задание 3.2. Параметризовать методы, используя правило PECS, и реализовать их
*/

public class CollectionUtils {

    private static List<String> list;

    public static <T> void addAll(List<? extends T> source, List<? super T> destination) {
        destination.addAll(source);
    }

    public static int indexOf(List<?> source, Object o) {
        return source.indexOf(o);
    }

    public static <T> List<T> limit(List<? extends T> source, int size) {
        List<T> list = new ArrayList<>();
        if ((size <= 0) || source == null) return list;
        int length = (size > source.size()) ? source.size() : size;
        for (int i = 0; i < length; i++) {
            list.add(source.get(i));
        }
        return list;
    }

    public static <T> void add(List<? super T> source, T o) {
        source.add(o);
    }

    public static <T> void removeAll(List<? super T> removeFrom, List<? extends T> removeElem) {
        removeFrom.removeAll(removeElem);
    }

    // true если первый лист содержит все элементы второго
    public static <T> boolean containsAll(List<?> c1, List<?> c2) {
        return c1.containsAll(c2);
    }

    // true если первый лист содержит хотя-бы 1 второго
    public static boolean containsAny(List<?> c1, List<?> c2) {
        return !Collections.disjoint(c1, c2);
    }

    // Возвращает лист, содержащий элементы из входного листа в диапазоне от min до max.
    // Элементы сравнивать через Comparable.
    // Пример range(Arrays.asList(8,1,3,5,6, 4), 3, 6) вернет {3,4,5,6}
    public static <T extends Comparable<T>> List<T> range(List<? extends T> list, T min, T max) {
        List<T> result = new ArrayList<>();
        if ((max.compareTo(min) < 0) || list == null) return result;
        for (T e: list) {
            if ((e.compareTo(max)<=0) && (e.compareTo(min)>=0)){
                result.add(e);
            }
        }
        Collections.sort(result);
        return result;
    }

    // Возвращает лист, содержащий элементы из входного листа в диапазоне от min до max.
    // Элементы сравнивать через Comparator
    public static <T> List<T> range(List<? extends T> list, T min, T max, Comparator<? super T> c) {
        List<T> result = new ArrayList<>();
        if ((c.compare(max,min) < 0) || list == null) return result;
        for (T e: list) {
            if ((c.compare(e, max)<=0) && (c.compare(e, min)>=0)){
                result.add(e);
            }
        }
        Collections.sort(result, c);
        return result;
    }

}
