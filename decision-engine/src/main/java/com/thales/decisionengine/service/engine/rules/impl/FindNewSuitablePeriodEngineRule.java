package com.thales.decisionengine.service.engine.rules.impl;

import com.thales.decisionengine.service.engine.rules.EngineRule;
import com.thales.decisionengine.service.engine.rules.model.EngineRuleResult;
import com.thales.decisionengine.service.engine.rules.model.LoadDecisionRuleInputParam;
import com.thales.decisionengine.service.score.ScoreService;

public class FindNewSuitablePeriodEngineRule implements EngineRule {

  private static final int MIN_MONTH = 12;
  private static final int MAX_MONTH = 60;
  private final ScoreService scoreService;
  private final int creditModifier;
  private final float loanAmount;
  private final int loanPeriod;

  public FindNewSuitablePeriodEngineRule(
      ScoreService scoreService, LoadDecisionRuleInputParam loadDecisionRuleInputParam) {
    this.scoreService = scoreService;
    this.creditModifier = loadDecisionRuleInputParam.creditModifier();
    this.loanAmount = loadDecisionRuleInputParam.loanAmount();
    this.loanPeriod = loadDecisionRuleInputParam.loanPeriod();
  }

  @Override
  public EngineRuleResult process() {
    float creditScore = scoreService.calculateCreditScore(creditModifier, loanAmount, loanPeriod);

    int finalLoanPeriod = loanPeriod;

    if (creditScore < 1F) {
      finalLoanPeriod = findSuitableLoanPeriod(loanPeriod);
    }

    boolean isLoanApproved = finalLoanPeriod > 0;
    finalLoanPeriod = isLoanApproved ? finalLoanPeriod : loanPeriod;

    return new EngineRuleResult(isLoanApproved, false, false, loanAmount, finalLoanPeriod);
  }

  private int findSuitableLoanPeriod(int startingMonth) {
    int minLoanPeriod = startingMonth;
    int maxLoanPeriod = MAX_MONTH;
    int maxSuccessLoanPeriod = -1;

    while (minLoanPeriod <= maxLoanPeriod) {
      int midIndexLoanPeriod = (minLoanPeriod + maxLoanPeriod) / 2;

      if (scoreService.calculateCreditScore(creditModifier, loanAmount, midIndexLoanPeriod) >= 1F) {
        maxSuccessLoanPeriod = midIndexLoanPeriod;
        maxLoanPeriod = midIndexLoanPeriod - 1;
      } else {
        minLoanPeriod = midIndexLoanPeriod + 1;
      }
    }

    return maxSuccessLoanPeriod;
  }
}
