package io.afaruqi.gogobot.domain.plane;

import io.afaruqi.gogobot.domain.common.Coordinate;

public class PlaneService {
    private final Plane plane;

    private PlaneService(int xAxis, int yAxis) {
        this.plane = new Plane(xAxis, yAxis);
    }

    public static PlaneService initDefault() {
        return new PlaneService(5, 5);
    }

    // for custom plane size
    public static PlaneService init(int xAxis, int yAxis) {
        return new PlaneService(xAxis, yAxis);
    }

    public boolean isWithinPlane(Coordinate coordinate) {
        if (coordinate == null) {
            return false;
        }

        return coordinate.x() >= 0
                && coordinate.x() <= plane.xAxis()
                && coordinate.y() >= 0
                && coordinate.y() <= plane.yAxis();
    }

    public Plane getPlane() {
        return plane;
    }
}
