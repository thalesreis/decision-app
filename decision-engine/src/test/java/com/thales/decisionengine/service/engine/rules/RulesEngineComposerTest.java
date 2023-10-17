package com.thales.decisionengine.service.engine.rules;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RulesEngineComposerTest {

  private RulesEngineComposer rulesEngineComposer;

  @BeforeEach
  void setUp() {
    rulesEngineComposer = new RulesEngineComposer();
  }

  @Test
  @DisplayName("Should set and build executor with a single rule")
  void shouldSetAndBuildExecutorWithSingleRule() {
    EngineRule mockRule = mock(EngineRule.class);
    rulesEngineComposer.setRules(mockRule);

    RulesEngineExecutor executor = rulesEngineComposer.buildExecutor();

    assertNotNull(executor);
    assertEquals(1, executor.rules().size());
    assertEquals(mockRule, executor.rules().get(0));
  }

  @Test
  @DisplayName("Should set and build executor with multiple rules")
  void shouldSetAndBuildExecutorWithMultipleRules() {
    EngineRule rule1 = mock(EngineRule.class);
    EngineRule rule2 = mock(EngineRule.class);
    EngineRule rule3 = mock(EngineRule.class);

    rulesEngineComposer.setRules(rule1, rule2, rule3);

    RulesEngineExecutor executor = rulesEngineComposer.buildExecutor();

    assertNotNull(executor);
    assertEquals(3, executor.rules().size());
    assertTrue(executor.rules().contains(rule1));
    assertTrue(executor.rules().contains(rule2));
    assertTrue(executor.rules().contains(rule3));
  }

  @Test
  @DisplayName("Should build executor with an empty list of rules when not set")
  void shouldBuildExecutorWithEmptyListOfRulesWhenNotSet() {
    RulesEngineExecutor executor = rulesEngineComposer.buildExecutor();

    assertNotNull(executor);
    assertTrue(executor.rules().isEmpty());
  }
}
