package com.thales.decisionengine.model.response;

public record SimulationResponse(boolean success, float decisionAmount, int loanPeriod) {}
