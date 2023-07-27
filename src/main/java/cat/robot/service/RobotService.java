package cat.robot.service;

import cat.robot.exception.NotFoundException;
import cat.robot.model.RobotState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class RobotService {

    private final HashMap<String, RobotState> robotStateMap;

    public RobotService() {

        this.robotStateMap = new HashMap<>();
    }

    public void place(String robotId, RobotState robotState) {

        robotStateMap.put(robotId, robotState);
    }

    public RobotState retrieveRobotState(String robotId) {

        return Optional.ofNullable(robotStateMap.get(robotId)).orElseThrow(() -> new NotFoundException(robotId + " is not a valid robot"));
    }


}
