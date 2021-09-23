package threadpool;

public class ScalableThreadPool extends GeneralThreadPool {

    public ScalableThreadPool(int minSize, int maxSize) {
        super(minSize, maxSize);
    }
}
