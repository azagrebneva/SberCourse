package proxy;

public class CalculatorImpl implements Calculator {

    /**
     * Расчет факториала числа
     * @param number положительное натуральное число
     * @return факториал введенного число
     */
    @Override
    public long calc(int number) {
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (number <= 1) return 1;
        long result = 1;
        for (int i = 1; i <= number; i++) {
            result = result * i;
        }
        return result;
    }
}
