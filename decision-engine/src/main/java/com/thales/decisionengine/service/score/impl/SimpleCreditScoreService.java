package com.thales.decisionengine.service.score.impl;

import com.thales.decisionengine.service.score.ScoreService;
import org.springframework.stereotype.Service;

@Service
public class SimpleCreditScoreService implements ScoreService {
  @Override
  public float calculateCreditScore(int modifier, float loanAmount, int loanPeriod) {
    return (modifier / loanAmount) * loanPeriod;
  }
}
