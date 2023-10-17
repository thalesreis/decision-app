package com.thales.decisionengine.service.engine.rules.impl;

import com.thales.decisionengine.service.engine.rules.EngineRule;
import com.thales.decisionengine.service.engine.rules.model.EngineRuleResult;
import com.thales.decisionengine.service.engine.rules.model.LoadDecisionRuleInputParam;
import com.thales.decisionengine.service.score.ScoreService;

public class FindLargerSumEngineRule implements EngineRule {

  private static final float MIN_LOAN_VALUE = 2000F;
  private static final float MAX_LOAN_VALUE = 10000F;

  private final ScoreService scoreService;
  private final int creditModifier;
  private final float loanAmount;
  private final int loanPeriod;

  public FindLargerSumEngineRule(
      ScoreService scoreService, LoadDecisionRuleInputParam loadDecisionRuleInputParam) {
    this.scoreService = scoreService;
    this.creditModifier = loadDecisionRuleInputParam.creditModifier();
    this.loanAmount = loadDecisionRuleInputParam.loanAmount();
    this.loanPeriod = loadDecisionRuleInputParam.loanPeriod();
  }

  @Override
  public EngineRuleResult process() {
    float calculatedScore =
        scoreService.calculateCreditScore(creditModifier, loanAmount, loanPeriod);

    float virtualMinLoanAmount = (calculatedScore >= 1F) ? loanAmount : MIN_LOAN_VALUE;
    float maxSuccessLoanAmount = findMaxLoanAmount(virtualMinLoanAmount);

    boolean success = maxSuccessLoanAmount >= MIN_LOAN_VALUE;
    maxSuccessLoanAmount = success ? maxSuccessLoanAmount : loanAmount;

    return new EngineRuleResult(success, false, false, maxSuccessLoanAmount, loanPeriod);
  }

  private float findMaxLoanAmount(float startingLoanAmount) {
    float minLoanAmount = startingLoanAmount;
    float maxLoanAmount = MAX_LOAN_VALUE;
    float maxSuccessLoanAmount = 0;

    while (minLoanAmount <= maxLoanAmount) {
      float midLoanAmount = (float) Math.ceil((minLoanAmount + maxLoanAmount) / 2);

      boolean success =
          scoreService.calculateCreditScore(creditModifier, midLoanAmount, loanPeriod) >= 1F;

      if (success) {
        maxSuccessLoanAmount = midLoanAmount;
        minLoanAmount = midLoanAmount + 1;
      } else {
        maxLoanAmount = midLoanAmount - 1;
      }
    }

    return maxSuccessLoanAmount;
  }
}
