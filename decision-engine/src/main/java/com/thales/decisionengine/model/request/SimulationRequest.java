package com.thales.decisionengine.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SimulationRequest(
    String personalCode,
    @DecimalMin("2000.0") @DecimalMax("10000.0") float loanAmount,
    @Min(12) @Max(60) int loanPeriod) {}
