package io.afaruqi.gogobot.domain.robot;

import io.afaruqi.gogobot.domain.common.Coordinate;
import io.afaruqi.gogobot.domain.plane.PlaneService;

public class RobotService {
    private final PlaneService planeService;

    public RobotService(PlaneService planeService) {
        this.planeService = planeService;
    }

    public Robot place(Coordinate coordinate, FaceDirection faceDirection) throws RobotException {
        var isWithinPlane = planeService.isWithinPlane(coordinate);

        if (isWithinPlane) {
            return new Robot(coordinate, faceDirection);
        } else {
            throw new RobotException("Robot is not placed within " + planeService.getPlane());
        }
    }

    public Robot move(Robot robot) throws RobotException {
        var newCoordinate = switch (robot.faceDirection()) {
            case NORTH: {
                var x = robot.coordinate().x();
                var y = robot.coordinate().y() + 1;
                yield new Coordinate(x, y);
            }
            case SOUTH: {
                var x = robot.coordinate().x();
                var y = robot.coordinate().y() - 1;
                yield new Coordinate(x, y);
            }
            case EAST: {
                var x = robot.coordinate().x() + 1;
                var y = robot.coordinate().y();
                yield new Coordinate(x, y);
            }
            case WEST:
                var x = robot.coordinate().x() - 1;
                var y = robot.coordinate().y();
                yield new Coordinate(x, y);
        };

        var isWithinPlane = planeService.isWithinPlane(newCoordinate);

        if (isWithinPlane) {
            return new Robot(newCoordinate, robot.faceDirection());
        } else {
            throw new RobotException("Robot cannot move there.");
        }
    }

    public Robot left(Robot robot) {
        return switch (robot.faceDirection()) {
            case NORTH:
                yield new Robot(robot.coordinate(), FaceDirection.WEST);
            case WEST:
                yield new Robot(robot.coordinate(), FaceDirection.SOUTH);
            case SOUTH:
                yield new Robot(robot.coordinate(), FaceDirection.EAST);
            case EAST:
                yield new Robot(robot.coordinate(), FaceDirection.NORTH);
        };
    }

    public Robot right(Robot robot) {
        return switch (robot.faceDirection()) {
            case NORTH:
                yield new Robot(robot.coordinate(), FaceDirection.EAST);
            case EAST:
                yield new Robot(robot.coordinate(), FaceDirection.SOUTH);
            case SOUTH:
                yield new Robot(robot.coordinate(), FaceDirection.WEST);
            case WEST:
                yield new Robot(robot.coordinate(), FaceDirection.NORTH);
        };
    }

    public String report(Robot robot) {
        return String.format("Output: %d,%d,%s", robot.coordinate().x(), robot.coordinate().y(), robot.faceDirection());
    }
}
