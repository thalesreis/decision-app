package com.thales.decisionengine.service.engine.rules;

import com.thales.decisionengine.service.engine.rules.model.EngineRuleResult;
import java.util.List;

public record RulesEngineExecutor(List<EngineRule> rules) {

  public EngineRuleResult execute() {
    EngineRuleResult finalResult = new EngineRuleResult(false, false, false, 0F, 0);

    for (EngineRule rule : rules) {
      EngineRuleResult ruleResult = rule.process();

      if (isSuccessAndShouldBreak(ruleResult) || isFailureAndShouldBreak(ruleResult)) {
        finalResult = ruleResult;
        break;
      }

      finalResult = ruleResult;
    }

    return finalResult;
  }

  private boolean isSuccessAndShouldBreak(EngineRuleResult ruleResult) {
    return ruleResult.success() && !ruleResult.keepRulesExecutionOnSuccess();
  }

  private boolean isFailureAndShouldBreak(EngineRuleResult ruleResult) {
    return !ruleResult.success() && ruleResult.isFinalDecision();
  }
}
