package com.thales.decisionengine.service.engine.rules.impl;

import com.thales.decisionengine.service.engine.rules.EngineRule;
import com.thales.decisionengine.service.engine.rules.model.EngineRuleResult;

public class UserHasDebtEngineRule implements EngineRule {

  private final boolean userHasDebts;

  public UserHasDebtEngineRule(boolean userHasDebts) {
    this.userHasDebts = userHasDebts;
  }

  @Override
  public EngineRuleResult process() {
    return new EngineRuleResult(!userHasDebts, true, true, 0F, 0);
  }
}
