package util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Path {
    protected List<Point> allPoints = new ArrayList<>();
    protected List<Point> Direction1;
    protected List<Point> Direction2;
    protected List<Point> Direction3;
    protected List<Point> Direction4;

    protected Path() {
        for(int i=0;i<1000;i+=50) {
            for(int j=0;j<1000;j+=50) {
                Point p = new Point(j,i);
                allPoints.add(p);
            }
        }
    }

    public List<Point> getDirection1() {
        return Direction1;
    }

    public List<Point> getDirection2() {
        return Direction2;
    }

    public List<Point> getDirection3() {
        return Direction3;
    }

    public List<Point> getDirection4() {
        return Direction4;
    }
}
