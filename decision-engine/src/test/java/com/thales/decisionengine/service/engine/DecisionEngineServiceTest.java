package com.thales.decisionengine.service.engine;

import static com.thales.decisionengine.TestsConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import com.thales.decisionengine.enums.Segment;
import com.thales.decisionengine.model.request.SimulationRequest;
import com.thales.decisionengine.model.response.external.userprofile.UserProfileResponse;
import com.thales.decisionengine.service.engine.rules.RulesEngineComposer;
import com.thales.decisionengine.service.external.userprofile.UserProfileMockedService;
import com.thales.decisionengine.service.score.ScoreService;
import com.thales.decisionengine.service.score.impl.SimpleCreditScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DecisionEngineServiceTest {

  private final ScoreService mockedScoreService = Mockito.mock(ScoreService.class);
  private final UserProfileMockedService mockedUserProfileService =
      Mockito.mock(UserProfileMockedService.class);
  private final RulesEngineComposer mockedRulesEngineComposer = new RulesEngineComposer();

  @Test
  @DisplayName("Should properly invoke user profile service")
  void shouldProperlyInvokeUserProfileService() {
    Mockito.when(mockedUserProfileService.retrieveUserProfileByPersonalCode(SEGMENT1_CODE))
        .thenReturn(new UserProfileResponse(Segment.SEGMENT_1, 100, false));

    var test =
        new DecisionEngineService(
            mockedScoreService, mockedUserProfileService, mockedRulesEngineComposer);
    final SimulationRequest request = new SimulationRequest(SEGMENT1_CODE, 2000F, 12);

    test.simulate(request);
    Mockito.verify(mockedUserProfileService, times(1))
        .retrieveUserProfileByPersonalCode(SEGMENT1_CODE);
  }

  @Test
  @DisplayName("Should not call score service when the user has debts")
  void shouldNotCallScoreServiceWhenUserHasDebts() {
    Mockito.when(mockedUserProfileService.retrieveUserProfileByPersonalCode(DEBT_CODE))
        .thenReturn(new UserProfileResponse(Segment.UNDEFINED, 100, true));

    var test =
        new DecisionEngineService(
            mockedScoreService, mockedUserProfileService, mockedRulesEngineComposer);
    final SimulationRequest request = new SimulationRequest(DEBT_CODE, 2000F, 12);

    test.simulate(request);
    Mockito.verify(mockedUserProfileService, times(1)).retrieveUserProfileByPersonalCode(DEBT_CODE);
    Mockito.verifyNoInteractions(mockedScoreService);
  }

  @Test
  @DisplayName("Should simulate for a user in Segment 2")
  void shouldSimulateForUserInSegment2() {
    Mockito.when(mockedUserProfileService.retrieveUserProfileByPersonalCode(SEGMENT2_CODE))
        .thenReturn(new UserProfileResponse(Segment.SEGMENT_2, 300, false));

    Mockito.when(mockedScoreService.calculateCreditScore(300, 3000F, 12)).thenReturn(1.2F);

    final SimulationRequest request = new SimulationRequest(SEGMENT2_CODE, 3000F, 12);

    var decisionEngineService =
        new DecisionEngineService(
            mockedScoreService, mockedUserProfileService, mockedRulesEngineComposer);

    var simulation = decisionEngineService.simulate(request);

    assertTrue(simulation.success());
  }

  @Test
  @DisplayName("Should have a negative decision for an user with debts")
  void shouldHaveNegativeDecisionForUserWithDebts() {
    Mockito.when(mockedUserProfileService.retrieveUserProfileByPersonalCode(DEBT_CODE))
        .thenReturn(new UserProfileResponse(Segment.UNDEFINED, 300, true));

    final SimulationRequest request = new SimulationRequest(DEBT_CODE, 3000F, 12);

    var decisionEngineService =
        new DecisionEngineService(
            new SimpleCreditScoreService(), mockedUserProfileService, new RulesEngineComposer());

    var simulation = decisionEngineService.simulate(request);

    assertFalse(simulation.success());
  }

  @Test
  @DisplayName("Should return the max suitable amount for a user without debts")
  void shouldReturnMaxSuitableAmountForUserWithoutDebts() {
    Mockito.when(mockedUserProfileService.retrieveUserProfileByPersonalCode(SEGMENT2_CODE))
        .thenReturn(new UserProfileResponse(Segment.SEGMENT_2, 300, false));

    final SimulationRequest request = new SimulationRequest(SEGMENT2_CODE, 4000F, 12);

    var decisionEngineService =
        new DecisionEngineService(
            new SimpleCreditScoreService(), mockedUserProfileService, new RulesEngineComposer());

    var simulation = decisionEngineService.simulate(request);

    assertTrue(simulation.success());
    assertEquals(3600F, simulation.decisionAmount());
    assertEquals(12, simulation.loanPeriod());
  }

  @Test
  @DisplayName("Should return the suitable loan period for a user without debts")
  void shouldReturnSuitableLoanPeriodForUserWithoutDebts() {
    Mockito.when(mockedUserProfileService.retrieveUserProfileByPersonalCode(SEGMENT1_CODE))
        .thenReturn(new UserProfileResponse(Segment.SEGMENT_1, 100, false));

    final SimulationRequest request = new SimulationRequest(SEGMENT1_CODE, 2000F, 12);

    var decisionEngineService =
        new DecisionEngineService(
            new SimpleCreditScoreService(), mockedUserProfileService, new RulesEngineComposer());

    var simulation = decisionEngineService.simulate(request);

    assertTrue(simulation.success());
    assertEquals(2000F, simulation.decisionAmount());
    assertEquals(20, simulation.loanPeriod());
  }

  @Test
  @DisplayName("Should return a negative decision for a super high loan amount")
  void shouldReturnNegativeDecisionForSuperHighLoanAmount() {
    Mockito.when(mockedUserProfileService.retrieveUserProfileByPersonalCode(SEGMENT1_CODE))
        .thenReturn(new UserProfileResponse(Segment.SEGMENT_1, 100, false));

    final SimulationRequest request = new SimulationRequest(SEGMENT1_CODE, 6001F, 12);

    var decisionEngineService =
        new DecisionEngineService(
            new SimpleCreditScoreService(), mockedUserProfileService, new RulesEngineComposer());

    var simulation = decisionEngineService.simulate(request);

    assertFalse(simulation.success());
    assertEquals(6001F, simulation.decisionAmount());
    assertEquals(12, simulation.loanPeriod());
  }
}
