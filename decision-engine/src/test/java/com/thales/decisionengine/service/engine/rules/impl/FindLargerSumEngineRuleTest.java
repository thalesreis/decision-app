package com.thales.decisionengine.service.engine.rules.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.thales.decisionengine.service.engine.rules.model.LoadDecisionRuleInputParam;
import com.thales.decisionengine.service.score.ScoreService;
import com.thales.decisionengine.service.score.impl.SimpleCreditScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class FindLargerSumEngineRuleTest {

  private final ScoreService scoreService = new SimpleCreditScoreService();

  @Test
  @DisplayName("Should check if ScoreService is being called")
  public void shouldCheckIfScoreServiceIsBeingCalled() {
    var params = new LoadDecisionRuleInputParam(300, 2000F, 12);
    var mockedScoreService = Mockito.mock(ScoreService.class);

    new FindLargerSumEngineRule(mockedScoreService, params).process();

    Mockito.verify(mockedScoreService, Mockito.atLeastOnce()).calculateCreditScore(300, 2000F, 12);
  }

  @Test
  @DisplayName("Should check if this rule is not a final decision")
  public void shouldCheckIfThisRuleIsNotAFinalDecision() {
    var params = new LoadDecisionRuleInputParam(300, 2000F, 12);
    var mockedScoreService = Mockito.mock(ScoreService.class);

    var ruleResult = new FindLargerSumEngineRule(mockedScoreService, params).process();
    assertFalse(ruleResult.isFinalDecision());
  }

  @Test
  @DisplayName("Should have a positive decision for a suitable loan value")
  public void shouldHaveAPositiveDecisionForASuitableLoanValue() {
    var params = new LoadDecisionRuleInputParam(300, 2000F, 12);
    var mockedScoreService = Mockito.mock(ScoreService.class);
    Mockito.when(mockedScoreService.calculateCreditScore(300, 2000F, 12)).thenReturn(1F);

    var ruleResult = new FindLargerSumEngineRule(mockedScoreService, params).process();

    assertTrue(ruleResult.success());
  }

  @Test
  @DisplayName("Should have a negative decision for a non-suitable loan value")
  public void shouldHaveANegativeDecisionForANonSuitableLoanValue() {
    var params = new LoadDecisionRuleInputParam(100, 10000F, 12);
    var mockedScoreService = Mockito.mock(ScoreService.class);
    Mockito.when(mockedScoreService.calculateCreditScore(100, 10000F, 12)).thenReturn(0.2F);

    var engineResult = new FindLargerSumEngineRule(mockedScoreService, params).process();

    assertFalse(engineResult.success());
  }

  @Test
  @DisplayName("Should find the largest sum amount even when a lower amount is requested")
  public void shouldFindTheLargestSumAmountForCreditModifier1000Amount2000Loan12() {
    var params = new LoadDecisionRuleInputParam(1000, 2000F, 12);

    var engineResult = new FindLargerSumEngineRule(scoreService, params).process();

    assertTrue(engineResult.success());
    assertEquals(12, engineResult.loanPeriod());
    assertEquals(10000F, engineResult.loanAmount());
  }

  @Test
  @DisplayName(
      "Should find the largest sum amount for a credit modifier 1000, loan amount 10000F, and loan period 12")
  public void shouldFindTheLargestSumAmountForCreditModifier1000Amount10000Loan12() {
    var params = new LoadDecisionRuleInputParam(1000, 10000F, 12);

    var engineResult = new FindLargerSumEngineRule(scoreService, params).process();

    assertTrue(engineResult.success());
    assertEquals(12, engineResult.loanPeriod());
    assertEquals(10000F, engineResult.loanAmount());
  }

  @Test
  @DisplayName(
      "Should find the largest sum amount for a credit modifier 300, loan amount 2000F, and loan period 12")
  public void shouldFindTheLargestSumAmountForCreditModifier300Amount2000Loan12() {
    var params = new LoadDecisionRuleInputParam(300, 2000F, 12);

    var engineResult = new FindLargerSumEngineRule(scoreService, params).process();

    assertTrue(engineResult.success());
    assertEquals(12, engineResult.loanPeriod());
    assertEquals(3600F, engineResult.loanAmount());
  }

  @Test
  @DisplayName(
      "Should find the largest sum amount for a credit modifier 300, loan amount 2000F, and loan period 48")
  public void shouldFindTheLargestSumAmountForCreditModifier300Amount2000Loan48() {
    var params = new LoadDecisionRuleInputParam(300, 2000F, 48);

    var engineResult = new FindLargerSumEngineRule(scoreService, params).process();

    assertTrue(engineResult.success());
    assertEquals(48, engineResult.loanPeriod());
    assertEquals(10000F, engineResult.loanAmount());
  }

  @Test
  @DisplayName(
      "Should find the largest sum amount for a credit modifier 100, loan amount 2000F, and loan period 60")
  public void shouldFindTheLargestSumAmountForCreditModifier100Amount2000Loan60() {
    var params = new LoadDecisionRuleInputParam(100, 2000F, 60);

    var engineResult = new FindLargerSumEngineRule(scoreService, params).process();

    assertTrue(engineResult.success());
    assertEquals(60, engineResult.loanPeriod());
    assertEquals(6000F, engineResult.loanAmount());
  }

  @Test
  @DisplayName(
      "Should find the largest sum amount for a credit modifier 100, loan amount 5000F, and loan period 24")
  public void shouldFindTheLargestSumAmountForCreditModifier100Amount5000Loan24() {
    var params = new LoadDecisionRuleInputParam(100, 5000F, 24);

    var engineResult = new FindLargerSumEngineRule(scoreService, params).process();

    assertTrue(engineResult.success());
    assertEquals(24, engineResult.loanPeriod());
    assertEquals(2400F, engineResult.loanAmount());
  }

  @Test
  @DisplayName(
      "Should not be able to find the largest sum amount for a credit modifier 100, loan amount 2000F, and loan period 12")
  public void shouldNotBeAbleToFindTheLargestSumAmountForCreditModifier100Amount2000Loan12() {
    var params = new LoadDecisionRuleInputParam(100, 2000F, 12);

    var engineResult = new FindLargerSumEngineRule(scoreService, params).process();

    assertFalse(engineResult.success());
    assertEquals(12, engineResult.loanPeriod());
    assertEquals(2000F, engineResult.loanAmount());
  }
}
