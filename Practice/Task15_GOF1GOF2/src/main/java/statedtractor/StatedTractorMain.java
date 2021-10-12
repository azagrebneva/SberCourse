package statedtractor;

import statedtractor.field.Field;
import statedtractor.geometry.Orientation;
import statedtractor.geometry.Position;
import statedtractor.tractor.StatedTractor;

/**
 * Задание 15. Паттерны проектирования, SOLID
 * Рефакторить код
 * 15.1 https://bitbucket.org/agoshkoviv/patterns-homework-1/src/69a61334ea43ff4c3fd950a00095377cf1e3bfd4/src/main/java/ru/sbt/test/refactoring/?at=master
 * (применен паттерн состояние (state))
 */

public class StatedTractorMain {

    public static void main(String[] args) {

        StatedTractor tractor = new StatedTractor(
                new Position(2,2),
                Orientation.WEST,
                new Field(5,5));

        System.out.println("initial position: " + tractor);

        tractor.moveForwards();
        System.out.println("move forwards: " + tractor);

        tractor.turnClockwise();
        System.out.println("turn clockwise: " + tractor);

        tractor.moveBackwards();
        System.out.println("move backwards: " + tractor);

        tractor.turnСounterclockwise();
        System.out.println("turn counterclockwise: " + tractor);

    }
}
