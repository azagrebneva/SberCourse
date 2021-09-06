package car;

/*
   Задание 2.1
   Имеется список парка машин Car(String model, String type).
   Необходимо разбить его на списки сгруппированные по type.
   Пример исходного списка: Лада седан, Лада хэтчбек, Мерседес седан,
   Бмв кроссовер,  Форд хэтчбек, Пежо кроссовер, Тойота седан и т.п.
*/

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class MainCars {

    // создание списка машин из входящей строки
    public static List<Car> createCarsList(String str){
        String[] subStr = str.split(",");
        List<Car> cars = new ArrayList<>();
        for(int i = 0; i < subStr.length; i++) {
            String[] carStr = subStr[i].trim().split(" ");
            cars.add(new Car(carStr[0], carStr[1]));
        }
        return cars;
    }

    // разбивка списка парка машин на списки сгруппированные по types
    public static Map<String, List<Car>> splitCarsIntoGroups(List<Car> cars){
        Map<String, List<Car>> groups = new HashMap<>();
        for (Car car : cars) {
            String type = car.getType();
            if (!groups.containsKey(type)) {
                groups.put(type, new ArrayList<Car>());
            }
            groups.get(type).add(car);
        }
        return groups;
    }

    // разбивка списка парка машин на списки сгруппированные по types
    // c помощью stream api
    public static Map<String, List<Car>> splitCarsIntoGroupsStream(List<Car> cars) {
        return cars.stream().collect(groupingBy(r -> r.getType()));
    }

    public static void main(String[] args) {

        // обработка исходной строки
        String str = "Лада седан, Лада хэтчбек, Мерседес седан, Бмв кроссовер,  Форд хэтчбек, Пежо кроссовер, Тойота седан";

        // создание списка машин из входной строки
        List<Car> cars = createCarsList(str);
        cars.forEach(System.out::println);
        System.out.println("---------------------------------------");

        // разбивка списка парка машин на списки сгруппированные по types
        Map<String, List<Car>> groups = splitCarsIntoGroups(cars);
        groups.forEach((k, v) -> System.out.println( k + ": " + v));
        System.out.println("---------------------------------------");

        // разбивка списка парка машин на списки сгруппированные по type
        // c помощью stream api
        groups = splitCarsIntoGroupsStream(cars);
        groups.forEach((k, v) -> System.out.println( k + ": " + v));
    }
}
