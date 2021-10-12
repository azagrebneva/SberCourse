package cache.calculator;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CalculatorImpl implements Calculator {

    @Override
    public List<Integer> fibonacci(int number) {

        return Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(number+1)
                .map(n -> n[0])
                .collect(toList());
    }
}
