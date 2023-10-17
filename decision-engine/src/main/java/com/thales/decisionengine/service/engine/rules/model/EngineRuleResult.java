package com.thales.decisionengine.service.engine.rules.model;

public record EngineRuleResult(
    boolean success,
    boolean isFinalDecision,
    boolean keepRulesExecutionOnSuccess,
    float loanAmount,
    int loanPeriod) {}
