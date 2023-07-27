package cat.robot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RobotState {

    @NotNull
    @Min(0)
    @Max(5)
    Integer x;

    @NotNull
    @Min(0)
    @Max(5)
    Integer y;

    @NotNull
    Facing facing;

    public static RobotState of(Integer x, Integer y, Facing facing) {

        return RobotState.builder()
                .x(x)
                .y(y)
                .facing(facing)
                .build();
    }

}
