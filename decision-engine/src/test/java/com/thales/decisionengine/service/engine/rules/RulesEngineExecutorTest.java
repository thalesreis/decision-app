package com.thales.decisionengine.service.engine.rules;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.thales.decisionengine.service.engine.rules.model.EngineRuleResult;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RulesEngineExecutorTest {

  private RulesEngineExecutor rulesEngineExecutor;
  private List<EngineRule> rules;

  @BeforeEach
  void setUp() {
    rules = Arrays.asList(mock(EngineRule.class), mock(EngineRule.class), mock(EngineRule.class));
    rulesEngineExecutor = new RulesEngineExecutor(rules);
  }

  @Test
  @DisplayName("Should return final success when all rules are successful")
  void shouldReturnFinalSuccessWhenAllRulesAreSuccessful() {
    when(rules.get(0).process()).thenReturn(new EngineRuleResult(true, false, false, 0F, 0));
    when(rules.get(1).process()).thenReturn(new EngineRuleResult(true, false, false, 0F, 0));
    when(rules.get(2).process()).thenReturn(new EngineRuleResult(true, false, false, 0F, 0));

    EngineRuleResult result = rulesEngineExecutor.execute();

    assertTrue(result.success());
    assertFalse(result.isFinalDecision());
  }

  @Test
  @DisplayName("Should return final success when the first rule is successful")
  void shouldReturnFinalSuccessWhenFirstRuleIsSuccessful() {
    when(rules.get(0).process()).thenReturn(new EngineRuleResult(true, false, false, 0F, 0));
    when(rules.get(1).process()).thenReturn(new EngineRuleResult(false, false, false, 0F, 0));
    when(rules.get(2).process()).thenReturn(new EngineRuleResult(false, false, false, 0F, 0));

    EngineRuleResult result = rulesEngineExecutor.execute();

    assertTrue(result.success());
    assertFalse(result.isFinalDecision());
  }

  @Test
  @DisplayName("Should return final failure when the first rule fails with final decision")
  void shouldReturnFinalFailureWhenFirstRuleFails() {
    when(rules.get(0).process()).thenReturn(new EngineRuleResult(false, true, true, 0F, 0));

    EngineRuleResult result = rulesEngineExecutor.execute();

    assertFalse(result.success());
    assertTrue(result.isFinalDecision());
  }
}
