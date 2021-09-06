package iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Параметризованные тесты
 */

@RunWith(Parameterized.class)
public class ParamObjectArrayTest {

    @Parameterized.Parameters
    public static Collection myData(){
        return Arrays.asList(new Object[][]{
                {new Object[]{-1, 0, 1, 2, 3, 4}},
                {new Object[]{"one", "two", "three", "four"}},
                {new Object[]{new Cat("Vasia",1), new Cat("Murka",3)}}
        });
    }

    ObjectArrayIterator iterator;
    Object[] a;

    public ParamObjectArrayTest(Object[] a) {
        this.a = a;
    }

    @Before
    public void prepare() {
        iterator = new ObjectArrayIterator(a);
    }

    @Test
    public void testNextHasNext() {
        int i = 0;
        while(iterator.hasNext()){
            Assert.assertEquals(a[i++], iterator.next());
        }
    }

    @Test
    public void testRemove() {
        Assert.assertThrows(UnsupportedOperationException.class,
                ()->{iterator.remove();});
    }
}
