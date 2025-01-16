package domain.plane;

import io.afaruqi.gogobot.domain.common.Coordinate;
import io.afaruqi.gogobot.domain.plane.PlaneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaneServiceTest {
    PlaneService planeService;

    @BeforeEach
    void setUp() {
        planeService = PlaneService.initDefault();
    }

    private static Stream<Coordinate> validCoordinatesParams() {
        return Stream.of(
            new Coordinate(0,0),
            new Coordinate(2,3),
            new Coordinate(3,3),
            new Coordinate(1,4),
            new Coordinate(5,5)
        );
    }

    @DisplayName("Coordinates within plane")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validCoordinatesParams")
    void should_return_true_when_given_coordinate_is_within_plane_boundary(Coordinate coordinate) {
        assertThat(planeService.isWithinPlane(coordinate)).isTrue();
    }

    private static Stream<Coordinate> invalidCoordinatesParams() {
        return Stream.of(
            new Coordinate(-1,0),
            new Coordinate(4,6),
            new Coordinate(6,-1),
            new Coordinate(6,6)
        );
    }

    @DisplayName("Coordinates outside plane")
    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCoordinatesParams")
    void should_return_false_when_given_coordinate_is_outside_of_plane_boundary(Coordinate coordinate) {
        assertThat(planeService.isWithinPlane(coordinate)).isFalse();
    }
}
