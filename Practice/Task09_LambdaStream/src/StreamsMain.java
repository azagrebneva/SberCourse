import person.Person;
import stream.Streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Задание 9. Лямбда функции, стримы
 * Реализовать класс похожий на java.util.stream.Stream (http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html)
 * Использование этого класса должно выглядеть примерно так:
 * List<Person> someCollection = ...
 * Map m = Streams.of(someCollection)
 *                .filter(p -> p.getAge() > 20)
 *                .transform( p -> new Person(p.geAge() + 30)))
 *                .toMap(p -> p.geName(), p -> p);
 */


public class StreamsMain {

    public static void main(String[] args) {

        List<Person> someCollection = new ArrayList<>(
                Arrays.asList(
                        new Person("Vasia","Ivanov", 4),
                        new Person("Misha","Sidorov", 81),
                        new Person("Olga", "Petrova", 31),
                        new Person("Lida", "Ivanova", 24),
                        new Person("Vladimir", "Sidorov", 70)));

        Map<String, Person> stringPersonMap = Streams.of(someCollection)
                .filter(p -> p.getAge() < 20)
                .transform(p -> new Person(p.getName(), p.getSurname(),
                        p.getAge() + 30))
                .toMap(p -> p.getName(), p -> p);
        System.out.println("Example: \n" + stringPersonMap.toString());

        stringPersonMap = Streams.of(someCollection)
                .filter(p -> p.getAge() >= 61)
                .filter(p -> p.getAge() <= 75)
                .toMap(Person::getName, p -> p);
        System.out.println("Elderly people: \n" + stringPersonMap.toString());

        Map<String, Integer> namesLength = Streams.of(someCollection)
                .transform(p -> p.getName())
                .toMap(p -> p, p -> p.length());
        System.out.println("Names and name's length: \n" + namesLength.toString());
    }
}
