package statedtractor.tractor;

import statedtractor.field.TractorInDitchException;
import statedtractor.field.Field;
import statedtractor.geometry.Orientation;
import statedtractor.geometry.Position;

public class StatedTractor {
    private Position position;
    private Orientation orientation;
    private Field field;

    public StatedTractor(Field field) {
        this(new Position(0, 0),
                Orientation.NORTH, field);
    }

    public StatedTractor(Position position, Orientation orientation, Field field) {
        this.position = position;
        this.orientation = orientation;
        this.field = field;
    }

    public void moveForwards() throws TractorInDitchException {
        position = orientation.moveForwards(position);
        field.inField(position);
    }

    public void moveBackwards() throws TractorInDitchException {
        position = orientation.moveBackwards(position);
        field.inField(position);
    }

    public void turnClockwise() {
        orientation = orientation.turnClockwise();
    }

    public void turnСounterclockwise(){
        orientation = orientation.turnСounterclockwise();
    }

    public Position getPosition() {
        return position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        return position +
                ", " + orientation;
    }
}
