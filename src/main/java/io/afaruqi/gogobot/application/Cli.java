package io.afaruqi.gogobot.application;

import io.afaruqi.gogobot.domain.common.Coordinate;
import io.afaruqi.gogobot.domain.robot.FaceDirection;
import io.afaruqi.gogobot.domain.robot.Robot;
import io.afaruqi.gogobot.domain.robot.RobotException;
import io.afaruqi.gogobot.domain.robot.RobotService;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Cli {
    private static final Pattern VALID_COMMANDS_REGEX = Pattern.compile("^(PLACE?.+|MOVE|LEFT|RIGHT|REPORT)$");
    private static final Pattern PLACE_COMMAND_REGEX = Pattern.compile("^(PLACE) (?<xCoordinate>\\d),(?<yCoordinate>\\d),(?<faceDirection>NORTH|SOUTH|EAST|WEST)$");

    private final RobotService robotService;
    private Robot robot;

    public Cli(RobotService robotService) {
        System.setProperty("line.separator", "\n");
        this.robotService = robotService;
    }

    public void run() {
        System.out.println("Blip blop, welcome to Gogobot! Where you can move a robot around.");
        System.out.println("Type 'HELP' to see all available commands.");

        var scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            var command = scanner.nextLine();

            if (command.equals("EXIT")) {
                break;
            }

            if (command.equals("HELP")) {
                printHelp();
                continue;
            }

            translateCommand(command);
        }

        System.out.println("Goodbye!");

        scanner.close();
    }

    private void printHelp() {
        System.out.println("Type 'PLACE <x,y,NORTH|SOUTH|EAST|WEST>' to place the robot in x & y coordinate with face direction (e.g. PLACE 1,1,NORTH).");
        System.out.println("Type 'MOVE' to move the robot one unit forward in a direction the robot is facing.");
        System.out.println("Type 'LEFT' to turn the face direction to the left.");
        System.out.println("Type 'RIGHT' to turn the face direction to the right.");
        System.out.println("Type 'REPORT' to announce the robot's current coordinate and its face direction.");
        System.out.println("Type 'EXIT' to exit the program.");
    }

    private void translateCommand(String command) {
        if (command.isBlank())
            System.out.println("Command is blank");

        var commandMatcher = VALID_COMMANDS_REGEX.matcher(command);

        if (commandMatcher.matches()) {
            executeCommand(command);
        } else {
            System.out.println("Unknown command '" + command + "', type 'HELP' to see available commands.");
        }
    }

    private void executeCommand(String command) {
        if (command.startsWith("PLACE")) {
            executePlaceCommand(command);
        } else if (command.equals("MOVE")) {
            executeMoveCommand();
        } else if (command.equals("LEFT")) {
            executeLeftCommand();
        } else if (command.equals("RIGHT")) {
            executeRightCommand();
        } else if (command.equals("REPORT")) {
            executeReportCommand();
        }
    }

    private void executePlaceCommand(String command) {
        try {
            var matcher = PLACE_COMMAND_REGEX.matcher(command);

            if (matcher.matches()) {
                var xCoordinate = Integer.parseInt(matcher.group("xCoordinate"));
                var yCoordinate = Integer.parseInt(matcher.group("yCoordinate"));
                var coordinate = new Coordinate(xCoordinate, yCoordinate);
                var faceDirection = FaceDirection.valueOf(matcher.group("faceDirection"));

                robot = robotService.place(coordinate, faceDirection);
            } else {
                System.out.println("Invalid PLACE arguments: '" + command + "'");
                System.out.println("Hint: PLACE <x coordinate>,<v coordinate>,<face direction: NORTH, SOUTH, EAST, WEST>");
            }
        } catch (RobotException e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeMoveCommand() {
        try {
            validateRobot();
            robot = robotService.move(robot);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeLeftCommand() {
        try {
            validateRobot();
            robot = robotService.left(robot);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeRightCommand() {
        try {
            validateRobot();
            robot = robotService.right(robot);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeReportCommand() {
        try {
            validateRobot();
            var message = robotService.report(robot);
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * this is to ensure robot is initialized before any commands is executed (except PLACE command).
     */
    private void validateRobot() {
        if (robot == null)
            throw new RuntimeException("Please place a robot first with 'PLACE' command.");
    }
}
