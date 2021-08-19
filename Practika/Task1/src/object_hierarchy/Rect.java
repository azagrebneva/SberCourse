package object_hierarchy;

public class Rect extends Figure{
    private double a;
    private double b;

    public Rect(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    @Override
    public double getSquare() {
        return a*b;
    }

    @Override
    public double getPerimeter() {
        return 2*(a + b);
    }

    @Override
    public String toString() {
        return "Rectangle(" +
                "a=" + a +
                ", b=" + b + ')';
    }
}
