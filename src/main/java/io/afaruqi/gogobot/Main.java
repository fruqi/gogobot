package io.afaruqi.gogobot;

import io.afaruqi.gogobot.application.Cli;
import io.afaruqi.gogobot.domain.plane.PlaneService;
import io.afaruqi.gogobot.domain.robot.RobotService;

public class Main {
    public static void main(String[] args) {
        new Main().startCli();
    }

    public void startCli() {
        var planeService = PlaneService.initDefault();
        var robotService = new RobotService(planeService);

        new Cli(robotService).run();
    }
}
