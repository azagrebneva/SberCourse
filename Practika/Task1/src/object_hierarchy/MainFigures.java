package object_hierarchy;

/*
 Задание 1.2 Реализовать иерархию объектов Circle, Rect, Triangle, Square
*/

public class MainFigures {
    public static void main(String[] args) {

        Figure[] figures = {new Circle(5),
                new Rect(2,10),
                new Triangle(5, 6, 7),
                new Square(2)};

        for(Figure figure : figures)
            System.out.println(figure +
                    ": square = " + figure.getSquare() +
                    ", perimeter = " + figure.getPerimeter());
    }
}
