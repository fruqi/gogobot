package integration;

import io.afaruqi.gogobot.application.Cli;
import io.afaruqi.gogobot.domain.plane.PlaneService;
import io.afaruqi.gogobot.domain.robot.RobotService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CliIntegrationTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream inputContent;
    private ByteArrayOutputStream outputContent;

    Cli cli;

    @BeforeEach
    void setUp() {
        outputContent = new ByteArrayOutputStream();
        var writer = new BufferedWriter(new OutputStreamWriter(outputContent)) {
            @Override
            public void newLine() throws IOException {
                write('\n');
            }
        };


        System.setOut(new PrintStream(outputContent));

        var planeService = PlaneService.initDefault();
        var robotService = new RobotService(planeService);
        cli = new Cli(robotService);
    }

    @AfterEach
    void cleanUp() {
        System.setIn(systemIn);
        System.setOut(systemOut);

        System.out.println(System.lineSeparator());
    }

    @Test
    void should_print_greeting_texts() {
        userInput("EXIT");

        cli.run();

        var expected = """
            Blip blop, welcome to Gogobot! Where you can move a robot around.
            Type 'HELP' to see all available commands.
            Goodbye!""";

        assertThat(printOutput())
            .isEqualTo(expected);
    }

    @Test
    void should_print_user_manual() {
        userInput("HELP");

        cli.run();

        var expected = """
            Blip blop, welcome to Gogobot! Where you can move a robot around.
            Type 'HELP' to see all available commands.
            Type 'PLACE <x,y,NORTH|SOUTH|EAST|WEST>' to place the robot in x & y coordinate with face direction (e.g. PLACE 1,1,NORTH).
            Type 'MOVE' to move the robot one unit forward in a direction the robot is facing.
            Type 'LEFT' to turn the face direction to the left.
            Type 'RIGHT' to turn the face direction to the right.
            Type 'REPORT' to announce the robot's current coordinate and its face direction.
            Type 'EXIT' to exit the program.
            Goodbye!""";

        assertThat(printOutput())
            .isEqualTo(expected);
    }

    @Test
    void should_move_the_robot_and_report_the_coordinate() {
        var input = """
            PLACE 1,1,NORTH
            MOVE
            RIGHT
            REPORT
            EXIT
            """;
        userInput(input);

        cli.run();

        var expected = """
            Blip blop, welcome to Gogobot! Where you can move a robot around.
            Type 'HELP' to see all available commands.
            Output: 1,2,EAST
            Goodbye!""";

        assertThat(printOutput())
            .isEqualTo(expected);
    }

    @Test
    void should_inform_user_when_the_first_command_is_incorrect() {
        var input = """
            REPORT
            EXIT
            """;
        userInput(input);

        cli.run();

        var expected = """
            Blip blop, welcome to Gogobot! Where you can move a robot around.
            Type 'HELP' to see all available commands.
            Please place a robot first with 'PLACE' command.
            Goodbye!""";

        assertThat(printOutput())
            .isEqualTo(expected);
    }

    @Test
    void should_inform_user_when_given_command_is_invalid() {
        var input = """
            PLEASE
            EXIT
            """;
        userInput(input);

        cli.run();

        var expected = """
            Blip blop, welcome to Gogobot! Where you can move a robot around.
            Type 'HELP' to see all available commands.
            Unknown command 'PLEASE', type 'HELP' to see available commands.
            Goodbye!""";

        assertThat(printOutput())
            .isEqualTo(expected);
    }

    private void userInput(String data) {
        inputContent = new ByteArrayInputStream(data.getBytes());
        System.setIn(inputContent);
    }

    private String printOutput() {
        if (isWindowsSystem()) {
            return sanitizeCrlfFromText(outputContent.toString());
        }

        return outputContent.toString().strip().trim();
    }

    private boolean isWindowsSystem() {
        return System.lineSeparator().equals("\r\n");
    }

    /**
     * remove any CRLF character ('\r\n') from the text and replace it with new line character '\n'
     * @param text original text
     * @return sanitized text
     */
    private String sanitizeCrlfFromText(String text) {
        return text
            .lines()
            .map(s -> s.replace("\r\n", ""))
            .collect(Collectors.joining("\n"));
    }
}
