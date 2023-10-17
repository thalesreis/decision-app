package com.thales.decisionengine.service.score;

public interface ScoreService {
  float calculateCreditScore(int modifier, float loanAmount, int loanPeriod);
}
