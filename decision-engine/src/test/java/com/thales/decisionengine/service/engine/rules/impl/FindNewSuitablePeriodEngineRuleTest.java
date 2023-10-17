package com.thales.decisionengine.service.engine.rules.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thales.decisionengine.service.engine.rules.model.LoadDecisionRuleInputParam;
import com.thales.decisionengine.service.score.ScoreService;
import com.thales.decisionengine.service.score.impl.SimpleCreditScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public class FindNewSuitablePeriodEngineRuleTest {
  private final ScoreService scoreService = new SimpleCreditScoreService();

  @Test
  @DisplayName("Should check if ScoreService is being called")
  public void shouldCheckIfScoreServiceIsBeingCalled() {
    var params = new LoadDecisionRuleInputParam(300, 2000F, 12);
    var mockedScoreService = Mockito.mock(ScoreService.class);

    new FindNewSuitablePeriodEngineRule(mockedScoreService, params).process();

    Mockito.verify(mockedScoreService, Mockito.atLeastOnce()).calculateCreditScore(300, 2000F, 12);
  }

  @Test
  @DisplayName("Should check if this is rule is not a finalDecision")
  public void shouldCheckIfThisRuleIsNotAFinalDecision() {
    var params = new LoadDecisionRuleInputParam(300, 2000F, 12);
    var mockedScoreService = Mockito.mock(ScoreService.class);

    var ruleResult = new FindNewSuitablePeriodEngineRule(mockedScoreService, params).process();
    assertFalse(ruleResult.isFinalDecision());
  }

  @Test
  @DisplayName("Should have a positive decision for a suitable loan value")
  public void shouldHaveAPositiveDecisionForASuitableLoanValue() {
    var params = new LoadDecisionRuleInputParam(300, 2000F, 12);
    var mockedScoreService = Mockito.mock(ScoreService.class);
    Mockito.when(mockedScoreService.calculateCreditScore(300, 2000F, 12)).thenReturn(1F);

    var ruleResult = new FindNewSuitablePeriodEngineRule(mockedScoreService, params).process();

    assertTrue(ruleResult.success());
  }

  @Test
  @DisplayName("Should have a negative decision for a non suitable loan value")
  public void shouldHaveANegativeDecisionForANonSuitableLoanValue() {
    var params = new LoadDecisionRuleInputParam(100, 10000F, 12);
    var mockedScoreService = Mockito.mock(ScoreService.class);
    Mockito.when(mockedScoreService.calculateCreditScore(100, 10000F, 12)).thenReturn(0.2F);

    var engineResult = new FindNewSuitablePeriodEngineRule(mockedScoreService, params).process();

    assertFalse(engineResult.success());
  }

  @ParameterizedTest(
      name =
          "creditModifier {0}, loanAmount {1}, loanPeriod {2}=>{3} should return a positive decision")
  @CsvSource({
    "100, 3000.0, 12, 30",
    "100, 5000.0, 12, 50",
    "300, 7000.0, 12, 24",
    "300, 5000.0, 24, 24",
    "1000, 10000.0, 12, 12",
    "1000, 10000.0, 60, 60"
  })
  @DisplayName("Should find a suitable loanPeriod based on input params")
  public void shouldFindASuitableLoanPeriodBasedOnParams(
      int creditModifier, float loanAmount, int loanPeriod, int expectedLoanPeriod) {
    var params = new LoadDecisionRuleInputParam(creditModifier, loanAmount, loanPeriod);

    var engineResult = new FindNewSuitablePeriodEngineRule(scoreService, params).process();

    assertTrue(engineResult.success());
    assertEquals(expectedLoanPeriod, engineResult.loanPeriod());
    assertEquals(loanAmount, engineResult.loanAmount());
  }

  @ParameterizedTest(
      name =
          "creditModifier {0}, loanAmount {1}, loanPeriod {2}=>{3} should return a negative decision")
  @CsvSource({"100, 6001.0, 12, 12", "100, 10000.0, 48, 48", "100, 10000.0, 60, 60"})
  @DisplayName("Should not find a suitable loanPeriod based on input params")
  public void shouldNotFindASuitableLoanPeriodBasedOnParams(
      int creditModifier, float loanAmount, int loanPeriod, int expectedLoanPeriod) {
    var params = new LoadDecisionRuleInputParam(creditModifier, loanAmount, loanPeriod);

    var engineResult = new FindNewSuitablePeriodEngineRule(scoreService, params).process();

    assertFalse(engineResult.success());
    assertEquals(expectedLoanPeriod, engineResult.loanPeriod());
    assertEquals(loanAmount, engineResult.loanAmount());
  }
}
