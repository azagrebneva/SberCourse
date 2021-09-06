package object_hierarchy;

public class Square extends Figure {
    private double a;

    public Square(double a) {
        this.a = a;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    @Override
    public double getSquare() {
        return a*a;
    }

    @Override
    public double getPerimeter() {
        return 4*a;
    }

    @Override
    public String toString() {
        return "Square(" +
                "a=" + a +
                ')';
    }
}
