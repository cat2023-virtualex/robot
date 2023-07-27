package cat.robot;

import cat.robot.model.Facing;
import cat.robot.model.RobotState;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class RobotApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldReturnNotFoundGivenMissingRobotId() {

		// given
		String url = "/robot/{robotId}";
		String robotId = "notFoundRobotId";

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<RobotState> requestEntity = new HttpEntity<>(headers);

		// when
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, robotId);

		// then
		assertThat(responseEntity.getStatusCode(), CoreMatchers.equalTo(HttpStatus.NOT_FOUND));
	}

	@Test
	void shouldRetrieveRobotGivenNewRobot() {

		// given
		String url = "/robot/{robotId}";
		String robotId = "robotToRetrieve";
		RobotState initialRobotState = RobotState.of(0, 0, Facing.NORTH);

		HttpHeaders headers = new HttpHeaders();

		// when
		ResponseEntity<Void> responseEntityPut = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(initialRobotState, headers), Void.class, robotId);
		ResponseEntity<RobotState> responseEntityGet = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), RobotState.class, robotId);

		// then
		assertThat(responseEntityPut.getStatusCode(), CoreMatchers.equalTo(HttpStatus.CREATED));
		assertThat(responseEntityGet.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
		assertThat(responseEntityGet.getBody(), CoreMatchers.equalTo(initialRobotState));
	}

	@ParameterizedTest
	@MethodSource
	void shouldPlaceRobotGivenRobotState(RobotState robotState, HttpStatus expectedStatus) {

		// given
		String url = "/robot/{robotId}";
		String robotId = "robotId";

		HttpEntity<RobotState> requestEntity = new HttpEntity<>(robotState);

		// when
		ResponseEntity<Void> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class, robotId);

		// then
		assertThat(responseEntity.getStatusCode(), CoreMatchers.equalTo(expectedStatus));
	}

	private static Stream<Arguments> shouldPlaceRobotGivenRobotState() {

		return Stream.of(
				Arguments.of(RobotState.of(0, 0, Facing.NORTH), HttpStatus.CREATED),

				// limits of x
				Arguments.of(RobotState.of(5, 0, Facing.NORTH), HttpStatus.CREATED),
				Arguments.of(RobotState.of(-1, 0, Facing.NORTH), HttpStatus.BAD_REQUEST),
				Arguments.of(RobotState.of(6, 0, Facing.NORTH), HttpStatus.BAD_REQUEST),

				// limits of y
				Arguments.of(RobotState.of(0, 5, Facing.NORTH), HttpStatus.CREATED),
				Arguments.of(RobotState.of(0, -1, Facing.NORTH), HttpStatus.BAD_REQUEST),
				Arguments.of(RobotState.of(0, 6, Facing.NORTH), HttpStatus.BAD_REQUEST)
		);
	}

}
