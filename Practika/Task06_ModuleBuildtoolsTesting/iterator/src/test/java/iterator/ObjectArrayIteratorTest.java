package iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Простые тесты без использования параметризации
 */
public class ObjectArrayIteratorTest {
    ObjectArrayIterator iterator;
    int[] a;

    @Before
    public void prepare() {
        a = new int[]{-1, 0, 1, 2, 3, 4};
        iterator = new ObjectArrayIterator(
                new int[]{-1, 0, 1, 2, 3, 4});
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

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAnnotations() {
        iterator.remove();
    }
}
