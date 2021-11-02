package sber.course.calculator;

import org.springframework.stereotype.Component;

@Component
public class Calculator {

    public long factorial(int n) {
        long result = 1;

        for (int i = 2; i <= n; i++) {
            result *= i;
        }

        return result;
    }
}
