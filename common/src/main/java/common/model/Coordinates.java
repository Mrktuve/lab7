package common.model;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private int x;
    private long y;

    public Coordinates(int x, long y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}