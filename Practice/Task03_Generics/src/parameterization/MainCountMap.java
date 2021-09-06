package parameterization;

// Задание 3.1. Параметризовать CountMap и реализовать его

import java.util.Map;

public class MainCountMap {
    public static void main(String[] args) {
        CountMap<Integer> countMap = new CountMapImpl<>();

        countMap.add(10);
        countMap.add(10);
        countMap.add(5);
        countMap.add(6);
        countMap.add(5);
        countMap.add(10);

        System.out.println("-----------------------------------------");
        System.out.println("Исходный countMap:" + countMap);
        System.out.println("Количество 10: " + countMap.getCount(10));
        System.out.println("Количество 6: " + countMap.getCount(6));
        System.out.println("Удаление 11, количество эл. до удаления " + countMap.remove(11));
        System.out.println("Удаление 10, количество эл. до удаления " + countMap.remove(10));
        System.out.println("СountMap после удаления элементов: " + countMap);
        System.out.println("Количество разных элементов: " + countMap.size());
        CountMap<Integer> subMap = new CountMapImpl<>(){{
            add(5); add(5); add(1); add(1); add(2);
        }};
        System.out.println("Массив для добавления: " + subMap);
        countMap.addAll(subMap);
        System.out.println("Результат addAll(): " + countMap);
        Map<Integer, Integer> map = countMap.toMap();
        System.out.println("Результат toMap(): " + map);
        map.clear();
        System.out.println("СountMap после удаления элементов map: " + countMap);
        countMap.toMap(map);
        System.out.println("Результат toMap(dst): " + map);
    }
}
