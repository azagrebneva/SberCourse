package iterator;

/*
   Задание 6.2 Реализуйте свой итератор массива объектов.
   Напишите тесты для проверки его работоспособности.
   Оформите сборку кода через maven.
*/

public class MainArrayIterator {

    public static void main(String[] args) {
        ObjectArrayIterator iterator = new ObjectArrayIterator(
                new int[]{-1, 0, 1, 2, 3, 4});
        iterator.forEachRemaining(s->{System.out.print(s + " ");});
    }
}