package cat.robot.controller;

import cat.robot.model.RobotState;
import cat.robot.service.RobotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping(path ="/robot")
@Slf4j
@Tag(name = "Robot API", description = "Operations to create and control robots")
public class RobotController {

    private final RobotService robotService;

    public RobotController(RobotService robotService) {
        this.robotService = robotService;
    }

    @Operation(summary = "Place a robot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Robot Placed"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping(path = "/{robotId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> placeRobot(
            @RequestBody
            @Valid
            RobotState robotState,
            @PathVariable
            @Parameter(description = "id of the robot", example = "newRobot")
            String robotId) {

        log.info("Creating robot with: robotState={}, robotId={}", robotState, robotId);
        robotService.place(robotId, robotState);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Retrieve current robot state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current robot state"),
            @ApiResponse(responseCode = "404", description = "Requested robot does not exist")
    })
    @GetMapping(path = "/{robotId}")
    public ResponseEntity<RobotState> placeRobot(
            @PathVariable
            @Parameter(description = "id of the robot", example = "newRobot")
            String robotId) {

        log.info("Retrieving robot with: robotId={}", robotId);
        RobotState robotState = robotService.retrieveRobotState(robotId);
        return ResponseEntity.ok(robotState);
    }


}
