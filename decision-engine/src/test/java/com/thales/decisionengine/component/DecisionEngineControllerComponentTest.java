package com.thales.decisionengine.component;

import static com.thales.decisionengine.TestsConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.thales.decisionengine.model.request.SimulationRequest;
import com.thales.decisionengine.model.response.SimulationResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DecisionEngineControllerComponentTest {

  @LocalServerPort Integer port;

  @BeforeEach
  public void setup() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @Test
  @DisplayName("Should return 400 for an invalid loan amount (high)")
  public void shouldReturn400ForInvalidLoanAmountHigh() {

    final var request = new SimulationRequest(DEBT_CODE, 10001F, 12);

    given()
        .header(CONTENT_TYPE, APP_JSON)
        .body(request)
        .when()
        .post(SIMULATION_URL)
        .then()
        .statusCode(400)
        .body("errors[0]", equalTo("loanAmount: must be less than or equal to 10000.0"));
  }

  @Test
  @DisplayName("Should return 400 for an invalid loan amount (low)")
  public void shouldReturn400ForInvalidLoanAmountLow() {

    final var request = new SimulationRequest(DEBT_CODE, 1999F, 12);

    given()
        .header(CONTENT_TYPE, APP_JSON)
        .body(request)
        .when()
        .post(SIMULATION_URL)
        .then()
        .statusCode(400)
        .body("errors[0]", equalTo("loanAmount: must be greater than or equal to 2000.0"));
  }

  @Test
  @DisplayName("Should return 400 for an invalid loan period (low)")
  public void shouldReturn400ForInvalidLoanPeriodLow() {

    final var request = new SimulationRequest(DEBT_CODE, 2000F, 11);

    given()
        .header(CONTENT_TYPE, APP_JSON)
        .body(request)
        .when()
        .post(SIMULATION_URL)
        .then()
        .statusCode(400)
        .body("errors[0]", equalTo("loanPeriod: must be greater than or equal to 12"));
  }

  @Test
  @DisplayName("Should return 400 for an invalid loan period (high)")
  public void shouldReturn400ForInvalidLoanPeriodHigh() {

    final var request = new SimulationRequest(DEBT_CODE, 2000F, 61);

    given()
        .header(CONTENT_TYPE, APP_JSON)
        .body(request)
        .when()
        .post(SIMULATION_URL)
        .then()
        .statusCode(400)
        .body("errors[0]", equalTo("loanPeriod: must be less than or equal to 60"));
  }

  @Test
  @DisplayName("Should return 200 for a valid request with a user with debts")
  public void shouldReturn200ForValidRequestWithUserWithDebts() {

    final var request = new SimulationRequest(DEBT_CODE, 2000F, 60);

    var response =
        given()
            .header(CONTENT_TYPE, APP_JSON)
            .body(request)
            .when()
            .post(SIMULATION_URL)
            .then()
            .statusCode(200)
            .extract()
            .as(SimulationResponse.class);

    assertThat(response, is(notNullValue()));
    assertThat(response.success(), is(false));
  }

  @Test
  @DisplayName("Should return 200 for a valid request with a user without debts")
  public void shouldReturn200ForValidRequestWithUserWithoutDebts() {

    final var request = new SimulationRequest(SEGMENT1_CODE, 2000F, 12);

    var response =
        given()
            .header(CONTENT_TYPE, APP_JSON)
            .body(request)
            .when()
            .post(SIMULATION_URL)
            .then()
            .statusCode(200)
            .extract()
            .as(SimulationResponse.class);

    assertThat(response, is(notNullValue()));
    assertThat(response.success(), is(true));
    assertThat(response.decisionAmount(), is(2000F));
    assertThat(response.loanPeriod(), is(20));
  }

  @Test
  @DisplayName("Should return 200 for a valid request with a different decision amount")
  public void shouldReturn200ForValidRequestWithDifferentDecisionAmount() {

    final var request = new SimulationRequest(SEGMENT1_CODE, 5000F, 12);

    var response =
        given()
            .header(CONTENT_TYPE, APP_JSON)
            .body(request)
            .when()
            .post(SIMULATION_URL)
            .then()
            .statusCode(200)
            .extract()
            .as(SimulationResponse.class);

    assertThat(response, is(notNullValue()));
    assertThat(response.success(), is(true));
    assertThat(response.decisionAmount(), is(5000F));
    assertThat(response.loanPeriod(), is(50));
  }
}
