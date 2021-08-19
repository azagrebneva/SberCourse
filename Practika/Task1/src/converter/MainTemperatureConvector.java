package converter;

/*
 Задание 1.3: Реализовать конвертеры температуры.
 Считаем, что значения будут поступать по шкале Цельсия,
 конвертеры должны преобразовывать значение в свою шкалу.
*/

public class MainTemperatureConvector {

    public static void main(String[] args) {
        int celsius = 0;
        System.out.println("Перевод " + celsius + "\u00B0С (градусов по Цельсию) в другие единицы измерения температуры:");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toFahrenheitDegrees(celsius) + "\u00B0F  (градус по Фаренге́йту),");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toKelvinDegrees(celsius) + "\u00B0K (градус Кельвина),");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toReaumurDegrees(celsius) + "\u00B0Re (градус Реомю́ра),");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toRemerDegrees(celsius) + "\u00B0R\u00D8 (градус Рёмера),");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toRankinDegrees(celsius) + "\u00B0Ra (градус Ранкина),");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toDelilahDegrees(celsius) + "\u00B0D (градус Делиля),");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toHooksDegrees(celsius) + "\u00B0H (градус Гука),");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toDaltonDegrees(celsius) + "\u00B0Da (градус Дальтона),");
        System.out.println("\t \uD83D\uDF84 " + TemperatureConverter.toPlanckDegrees(celsius) + " Tp (Планковская температура).");
    }
}
