package proxy;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CalculatorImplTest {

    @Test
    public void testFactorial(){
        Calculator calculator = mock(CalculatorImpl.class);
        Mockito.when(calculator.calc(7)).thenReturn(5040L);

        long factReal =  new CalculatorImpl().calc(7);
        long factMockito = calculator.calc(7);

        assertEquals(factMockito, factReal);
    }
}
