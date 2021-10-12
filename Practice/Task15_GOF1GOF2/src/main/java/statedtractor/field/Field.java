package statedtractor.field;

import statedtractor.geometry.Position;

public class Field {

    int xSize;
    int ySize;

    /**
     * Field size [0, xSize]x[0, ySize]
     * @param xSize
     * @param ySize
     */
    public Field(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public void inField(Position p) throws TractorInDitchException {
        if ((p.getX() > xSize) || (p.getX() < 0) ||
                (p.getY() > ySize) || (p.getY() < 0)){
            throw new TractorInDitchException(
                    "You came out of the field " + p + ".");
        }
    }

    public int getXSize() {
        return xSize;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }
}
