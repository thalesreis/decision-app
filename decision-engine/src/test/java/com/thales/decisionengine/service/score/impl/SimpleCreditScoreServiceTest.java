package com.thales.decisionengine.service.score.impl;

import static com.thales.decisionengine.TestsConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SimpleCreditScoreServiceTest {

  @Test
  @DisplayName("Should maintain formula consistency with positive values")
  void shouldMaintainFormulaConsistencyWithPositiveValues() {
    final SimpleCreditScoreService simpleScore = new SimpleCreditScoreService();

    assertThat(simpleScore.calculateCreditScore(2, 2F, 2), equalTo(2F));
    assertThat(simpleScore.calculateCreditScore(10, 5F, 5), equalTo(10F));
    assertThat(simpleScore.calculateCreditScore(1, 1F, 1), equalTo(1F));
  }

  @Test
  @DisplayName(
      "Should calculate positive score with modifier 100, loanAmount 1000.0, and loanPeriod 12")
  void shouldCalculatePositiveCreditScoreWithModifier100() {
    final SimpleCreditScoreService simpleScore = new SimpleCreditScoreService();

    final var score = simpleScore.calculateCreditScore(MODIFIER_100, 1000F, 12);

    assertThat(score, greaterThanOrEqualTo(POSITIVE_SCORE_VALUE));
  }

  @Test
  @DisplayName(
      "Should calculate positive score with modifier 300, loanAmount 1000.0, and loanPeriod 12")
  void shouldCalculatePositiveCreditScoreWithModifier300() {
    final SimpleCreditScoreService simpleScore = new SimpleCreditScoreService();

    final var score = simpleScore.calculateCreditScore(MODIFIER_300, 1000F, 12);

    assertThat(score, greaterThanOrEqualTo(POSITIVE_SCORE_VALUE));
  }

  @Test
  @DisplayName(
      "Should calculate positive score with modifier 1000, loanAmount 10000.0, and loanPeriod 24")
  void shouldCalculatePositiveCreditScoreWithModifier1000() {
    final SimpleCreditScoreService simpleScore = new SimpleCreditScoreService();

    final var score = simpleScore.calculateCreditScore(MODIFIER_1000, 10000F, 24);

    assertThat(score, greaterThanOrEqualTo(POSITIVE_SCORE_VALUE));
  }

  @Test
  @DisplayName(
      "Should calculate negative score with modifier 100, loanAmount 5000.0, and loanPeriod 12")
  void shouldCalculateNegativeCreditScoreWithModifier100() {
    final SimpleCreditScoreService simpleScore = new SimpleCreditScoreService();

    final var score = simpleScore.calculateCreditScore(MODIFIER_100, 5000F, 12);

    assertThat(score, lessThan(POSITIVE_SCORE_VALUE));
  }
}
