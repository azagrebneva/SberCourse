package sort_search;

import java.util.Random;

/*
  Задание 1.1
  Написать сортировку пузырьком или бинарный поиск элемента в массиве.
*/

public class MainSortSearch {

    public static final int MIN_RANDOM_VALUE = -9;
    public static final int MAX_RANDOM_VALUE =  9;

    /*
     * Заполнение массива случайными числами, лежащими в диапазоне
     * [MIN_RANDOM_VALUE, MAX_RANDOM_VALUE]
     * */
    public static void fillArrayWithRandomNumbers(int []x){
        Random rand = new Random();
        for (int i = 0; i < x.length; i++) {
            x[i] = MIN_RANDOM_VALUE +  rand.nextInt(MAX_RANDOM_VALUE - MIN_RANDOM_VALUE + 1);
        }
    }

    /*
     * Вывод массива на экран
     * */
    public static void showArray(int []x){
        for (int i = 0; i < x.length; i++) {
            System.out.print(x[i] + " ");
        }
        System.out.println();
    }

    /*
     * Пузырьковая сортировка - сортирует входящий массив
     * */
    public static void bubbleSort(int []x){
       for (int i = 0; i < x.length - 1; i++) {
            for (int j = 0; j < x.length - i - 1; j++) {
                if (x[j] > x[j + 1]) {
                    int c = x[j];
                    x[j] = x[j + 1];
                    x[j + 1] = c;
                }
            }
        }
    }

    /*
    * Бинарный поиск - поиск элемента в отсортированном массиве
    * возвращает индекс найденного элемента в массиве или
    * -1, если элемент не найден
    * */
    public static int searchBinary (int []x, int key)
    {
        int middleIndex = 0;
        int leftIndex = 0;
        int rightIndex = x.length - 1;

        while (leftIndex <= rightIndex)
        {
            middleIndex = (int) (leftIndex + rightIndex) / 2;

            if (key == x[middleIndex])
                return middleIndex;
            else if (key < x[middleIndex])
                rightIndex = middleIndex - 1;
            else if (key > x[middleIndex])
                leftIndex = middleIndex + 1;
        }
        return -1;
    }

    public static void main(String[] args) {

        int[] x = new int[20];

        fillArrayWithRandomNumbers(x);
        showArray(x);

        bubbleSort(x);
        showArray(x);

        int key = 0;
        int index = searchBinary(x, 0);
        System.out.println( index == -1 ? "Элемент не найден " :
                "Элемент " + x[index] + " найден на позиции " + index);

        key = 10;
        index = searchBinary(x, 10);
        System.out.println( index == -1 ? "Элемент " + key + " не найден " :
                "Элемент " + key + " найден на позиции " + index);
    }

}
