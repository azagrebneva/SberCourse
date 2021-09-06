package iterator;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ObjectArrayIterator implements Iterator<Object> {
    private final Object array;
    private int currentIndex = 0;

    public ObjectArrayIterator(Object array) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("not an array");
        } else {
            this.array = array;
        }
    }

    @Override
    public boolean hasNext() {
        return this.currentIndex < Array.getLength(this.array);
    }

    @Override
    public Object next() {
        return Array.get(this.array, this.currentIndex++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("cannot remove items from an array");
    }
}


