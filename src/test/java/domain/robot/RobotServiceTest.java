package domain.robot;

import io.afaruqi.gogobot.domain.common.Coordinate;
import io.afaruqi.gogobot.domain.plane.Plane;
import io.afaruqi.gogobot.domain.plane.PlaneService;
import io.afaruqi.gogobot.domain.robot.FaceDirection;
import io.afaruqi.gogobot.domain.robot.Robot;
import io.afaruqi.gogobot.domain.robot.RobotException;
import io.afaruqi.gogobot.domain.robot.RobotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RobotServiceTest {
    @Mock
    PlaneService planeService;

    RobotService robotService;

    @BeforeEach
    void setUp() {
        robotService = new RobotService(planeService);
    }

    @Test
    void should_place_robot_on_given_coordinate_and_face_direction() throws RobotException {
        var coordinate = new Coordinate(1, 0);

        given(planeService.isWithinPlane(coordinate))
            .willReturn(true);

        var result = robotService.place(coordinate, FaceDirection.EAST);

        var expected = new Robot(coordinate, FaceDirection.EAST);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void should_throw_exception_when_given_coordinate_is_not_valid_during_robot_placement() {
        var coordinate = new Coordinate(10, 5);
        var plane = new Plane(5, 5);

        given(planeService.isWithinPlane(coordinate))
            .willReturn(false);
        given(planeService.getPlane())
            .willReturn(plane);

        Throwable thrown = catchThrowable(() -> robotService.place(coordinate, FaceDirection.EAST));

        assertThat(thrown).isInstanceOf(RobotException.class)
            .hasMessage("Robot is not placed within [x=5, y=5]");
    }

    @Test
    void should_move_robot_to_the_right_by_one_unit_when_it_is_facing_east() throws RobotException {
        var originalCoordinate = new Coordinate(0, 0);
        var faceDirection = FaceDirection.EAST;
        var robot = new Robot(originalCoordinate, faceDirection);

        var expectedCoordinate = new Coordinate(1, 0);

        given(planeService.isWithinPlane(expectedCoordinate))
            .willReturn(true);

        var result = robotService.move(robot);

        var expected = new Robot(expectedCoordinate, faceDirection);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void should_move_robot_to_the_top_by_one_unit_when_it_is_facing_north() throws RobotException {
        var originalCoordinate = new Coordinate(0, 0);
        var faceDirection = FaceDirection.NORTH;
        var robot = new Robot(originalCoordinate, faceDirection);

        var expectedCoordinate = new Coordinate(0, 1);

        given(planeService.isWithinPlane(expectedCoordinate))
            .willReturn(true);

        var result = robotService.move(robot);

        var expected = new Robot(expectedCoordinate, faceDirection);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void should_move_robot_to_the_left_by_one_unit_when_it_is_facing_west() throws RobotException {
        var originalCoordinate = new Coordinate(2, 2);
        var faceDirection = FaceDirection.WEST;
        var robot = new Robot(originalCoordinate, faceDirection);

        var expectedCoordinate = new Coordinate(1, 2);

        given(planeService.isWithinPlane(expectedCoordinate))
            .willReturn(true);

        var result = robotService.move(robot);

        var expected = new Robot(expectedCoordinate, faceDirection);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void should_move_robot_to_the_bottom_by_one_unit_when_it_is_facing_south() throws RobotException {
        var originalCoordinate = new Coordinate(2, 2);
        var faceDirection = FaceDirection.SOUTH;
        var robot = new Robot(originalCoordinate, faceDirection);

        var expectedCoordinate = new Coordinate(2, 1);

        given(planeService.isWithinPlane(expectedCoordinate))
            .willReturn(true);

        var result = robotService.move(robot);

        var expected = new Robot(expectedCoordinate, faceDirection);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void should_throw_exception_when_robot_movement_exceed_given_boundary() {
        var originalCoordinate = new Coordinate(0, 0);
        var faceDirection = FaceDirection.SOUTH;
        var robot = new Robot(originalCoordinate, faceDirection);

        var expectedCoordinate = new Coordinate(0, -1);

        given(planeService.isWithinPlane(expectedCoordinate))
            .willReturn(false);

        Throwable thrown = catchThrowable(() -> robotService.move(robot));

        assertThat(thrown).isInstanceOf(RobotException.class)
            .hasMessage("Robot cannot move there.");
    }

    @Test
    void should_report_the_current_coordinate_and_face_direction() {
        var coordinate = new Coordinate(2, 0);
        var faceDirection = FaceDirection.EAST;
        var robot = new Robot(coordinate, faceDirection);

        var result = robotService.report(robot);

        assertThat(result)
            .isEqualTo("Output: 2,0,EAST");
    }

    private static Stream<Arguments> leftTurnParams() {
        return Stream.of(
            Arguments.of(FaceDirection.NORTH, FaceDirection.WEST),
            Arguments.of(FaceDirection.WEST, FaceDirection.SOUTH),
            Arguments.of(FaceDirection.SOUTH, FaceDirection.EAST),
            Arguments.of(FaceDirection.EAST, FaceDirection.NORTH)
        );
    }

    @DisplayName("Turn robot to the left")
    @ParameterizedTest(name = "Origin face direction: \"{0}\", expected face direction: \"{1}\"")
    @MethodSource("leftTurnParams")
    void should_turn_robot_to_the_left(FaceDirection start, FaceDirection expected) {
        var coordinate = new Coordinate(0, 0);

        assertThat(robotService.left(new Robot(coordinate, start)))
            .isEqualTo(new Robot(coordinate, expected));
    }


    private static Stream<Arguments> rightTurnParams() {
        return Stream.of(
            Arguments.of(FaceDirection.NORTH, FaceDirection.EAST),
            Arguments.of(FaceDirection.EAST, FaceDirection.SOUTH),
            Arguments.of(FaceDirection.SOUTH, FaceDirection.WEST),
            Arguments.of(FaceDirection.WEST, FaceDirection.NORTH)
        );
    }

    @DisplayName("Turn robot to the right")
    @ParameterizedTest(name = "Origin face direction: \"{0}\", expected face direction: \"{1}\"")
    @MethodSource("rightTurnParams")
    void should_move_robot_to_the_right(FaceDirection start, FaceDirection expected) {
        var coordinate = new Coordinate(0, 0);

        assertThat(robotService.right(new Robot(coordinate, start)))
            .isEqualTo(new Robot(coordinate, expected));
    }
}
