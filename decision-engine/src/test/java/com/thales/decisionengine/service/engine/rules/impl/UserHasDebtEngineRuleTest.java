package com.thales.decisionengine.service.engine.rules.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserHasDebtEngineRuleTest {

  @Test
  @DisplayName("Should check if this is rule is a finalDecision")
  public void shouldCheckIfThisRuleIsAFinalDecision() {
    var ruleResult = new UserHasDebtEngineRule(false).process();
    assertTrue(ruleResult.isFinalDecision());
  }

  @Test
  @DisplayName("Should have a positive decision for a user without debts")
  public void shouldHaveAPositiveDecisionForAUserWithoutDebts() {
    var ruleResult = new UserHasDebtEngineRule(false).process();
    assertTrue(ruleResult.success());
  }

  @Test
  @DisplayName("Should have a negative decision for a user with debts")
  public void shouldHaveANegativeDecisionForAUserWithDebts() {
    var ruleResult = new UserHasDebtEngineRule(true).process();
    assertFalse(ruleResult.success());
  }
}
