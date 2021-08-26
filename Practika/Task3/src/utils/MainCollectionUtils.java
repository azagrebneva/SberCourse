package utils;

/*
    Задание 3.2. Параметризовать методы, используя правило PECS, и реализовать их
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MainCollectionUtils {

    public static void main(String[] args) {
        List<Pet> pets = new ArrayList(
                Arrays.asList(new Dog("Rex",2),
                        new Dog("Tuzik", 10),
                        new Dog("Tuzik", 1),
                        new Pet("Polly",1),
                        new Cat("Bussia",4),
                        new Cat("Vasia",0)));

        List<Dog> dogs = new ArrayList(
                Arrays.asList(new Dog("Zyshka",2),
                        new Dog("Sharik", 7),
                        new Dog("Strelka", 1),
                        new Dog("Belka", 1)
                        ));

        List<Pet> cats = new ArrayList(
                Arrays.asList( new Cat("Mursik",7),
                        new Cat("Vasia", 0)));

        System.out.println("3.2.1--------------------------------");
        System.out.println("Исходный список: ");
        pets.stream().forEach(s->{System.out.print(s + " ");});
        System.out.println("\nСписок для добавления: ");
        dogs.stream().forEach(s->{System.out.print(s + " ");});
        CollectionUtils.addAll(dogs, pets);
        System.out.println("\nСписок после добавления addAll: ");
        pets.stream().forEach(s->{System.out.print(s + " ");});

        System.out.println("\n\n3.2.2-------------------------------");
        Dog dog = new Dog("Tuzik",1);
        System.out.println("Индекс питомца " + dog + " в списке " +
            CollectionUtils.indexOf(pets, dog));

        System.out.println("\n3.2.3--------------------------------");
        List<Pet> petsShortList = CollectionUtils.limit(pets,2);
        System.out.println("Список после сокращения limit(pets, 2): ");
        petsShortList.stream().forEach(s->{System.out.print(s + " ");});

        System.out.println("\n\n3.2.4--------------------------------");
        Cat cat = new Cat("Myrzik",1);
        CollectionUtils.add(petsShortList, cat);
        System.out.println("Список после сокращения добавления add(pets, cat): ");
        petsShortList.stream().forEach(s->{System.out.print(s + " ");});

        System.out.println("\n\n3.2.5--------------------------------");
        System.out.println("Исходный список: ");
        pets.stream().forEach(s->{System.out.print(s + " ");});
        System.out.println("\nСписок элементов к удалению: ");
        dogs.stream().forEach(s->{System.out.print(s + " ");});
        CollectionUtils.removeAll(pets, dogs);
        System.out.println("\nСписок после сокращения removeAll(pets, petsShortList): ");
        pets.stream().forEach(s->{System.out.print(s + " ");});

        System.out.println("\n\n3.2.6--------------------------------");
        System.out.println("Содержит ли список: ");
        pets.stream().forEach(s->{System.out.print(s + " ");});
        System.out.println("\nвсе элементы списка: ");
        petsShortList.stream().forEach(s->{System.out.print(s + " ");});
        System.out.println("\nОтвет - " + CollectionUtils.containsAll(pets, petsShortList));

        System.out.println("\n3.2.7--------------------------------");
        System.out.println("Содержит ли список: ");
        pets.stream().forEach(s->{System.out.print(s + " ");});
        System.out.println("\nхотя бы один элемент списка: ");
        cats.stream().forEach(s->{System.out.print(s + " ");});
        System.out.println("\nОтвет - " + CollectionUtils.containsAny(pets, cats));

        System.out.println("\n3.2.8--------------------------------");
        System.out.println("Исходный список: ");
        pets.stream().forEach(s->{System.out.print(s + " ");});
        Cat minCat = new Cat("Lushik",1);
        Cat maxCat = new Cat("Vasia",10);
        System.out.println("\nПитомцы: минимальный " + minCat + ", максимальный " + maxCat);
        List<Pet> petsRange = CollectionUtils.range(pets, minCat, maxCat);
        System.out.println("Результат range(pets, minPet, maxPet): ");
        petsRange.stream().forEach(s->{System.out.print(s + " ");});

        System.out.println("\n\n3.2.9--------------------------------");
        System.out.println("Исходный список: ");
        pets.stream().forEach(s->{System.out.print(s + " ");});
        minCat = new Cat("Lushik",0);
        maxCat = new Cat("Vasia",3);
        System.out.println("\nПитомцы: минимальный " + minCat + ", максимальный " + maxCat);
        Comparator<Pet> petComparator = new Comparator<>() {
            @Override
            public int compare(Pet o1, Pet o2) {
                int result;
                result = o1.getAge().compareTo(o2.getAge());
                if(result != 0) return result;
                return o1.getName().compareTo(o2.getName());
            }
        };
        petsRange = CollectionUtils.range(pets, minCat, maxCat, petComparator);
        System.out.println("Результат range(pets, minPet, maxPet, petComparator): ");
        petsRange.stream().forEach(s->{System.out.print(s + " ");});
    }
}
