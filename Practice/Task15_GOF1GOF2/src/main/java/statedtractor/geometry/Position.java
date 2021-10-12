package statedtractor.geometry;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position changeY(int dy) {
        return new Position(x, y + dy);
    }

    public Position changeX(int dx){
        return new Position(x + dx, y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ']';
    }
}
