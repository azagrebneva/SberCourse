package file;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class InverseIterator implements Iterable<String> {

    private List<String> arrayList;

    public InverseIterator(List<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public Iterator<String> iterator() {
        Iterator<String> it = new Iterator<String>() {
            private int currentIndex = arrayList.size()-1;

            @Override
            public boolean hasNext() {
                return currentIndex >= 0;
            }

            @Override
            public String next() {
                return arrayList.get(currentIndex--);
            }

            @Override
            public void remove() {
                arrayList.remove(currentIndex);
            }

            @Override
            public void forEachRemaining(Consumer<? super String> action) {
                Iterator.super.forEachRemaining(action);
            }
        };

        return it;
    }
}