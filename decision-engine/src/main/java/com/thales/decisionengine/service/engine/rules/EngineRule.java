package com.thales.decisionengine.service.engine.rules;

import com.thales.decisionengine.service.engine.rules.model.EngineRuleResult;

public interface EngineRule {

  EngineRuleResult process();
}
