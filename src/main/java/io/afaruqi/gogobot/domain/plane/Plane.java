package io.afaruqi.gogobot.domain.plane;

public record Plane(
    int xAxis,
    int yAxis
) {
    @Override
    public String toString() {
        return "[x=" + xAxis + ", y=" + yAxis + "]";
    }
}
