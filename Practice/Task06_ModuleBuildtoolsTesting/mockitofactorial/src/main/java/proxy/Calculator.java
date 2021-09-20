package proxy;

public interface Calculator {
    /**
    * Расчет факториала числа.
    * @param number
    */
    @Metric
    @Cache(true)
    long calc (int number);
}
