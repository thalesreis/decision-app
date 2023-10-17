package com.thales.decisionengine.service.engine.rules;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * The RulesEngineComposer is to orchestrate the engine rules in additional to abstract the
 * executor's constructor method
 */
@Component
public class RulesEngineComposer {

  private List<EngineRule> rules = new ArrayList<>();

  public RulesEngineComposer setRules(EngineRule... rules) {
    this.rules = List.of(rules);

    return this;
  }

  public RulesEngineExecutor buildExecutor() {
    return new RulesEngineExecutor(rules);
  }
}
