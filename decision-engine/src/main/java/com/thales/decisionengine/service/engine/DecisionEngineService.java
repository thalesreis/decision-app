package com.thales.decisionengine.service.engine;

import com.thales.decisionengine.model.request.SimulationRequest;
import com.thales.decisionengine.model.response.SimulationResponse;
import com.thales.decisionengine.service.engine.rules.RulesEngineComposer;
import com.thales.decisionengine.service.engine.rules.impl.FindLargerSumEngineRule;
import com.thales.decisionengine.service.engine.rules.impl.FindNewSuitablePeriodEngineRule;
import com.thales.decisionengine.service.engine.rules.impl.UserHasDebtEngineRule;
import com.thales.decisionengine.service.engine.rules.model.LoadDecisionRuleInputParam;
import com.thales.decisionengine.service.external.userprofile.UserProfileMockedService;
import com.thales.decisionengine.service.score.ScoreService;
import org.springframework.stereotype.Service;

@Service
public class DecisionEngineService {

  private final ScoreService scoreService;
  private final UserProfileMockedService userProfileService;
  private final RulesEngineComposer rulesEngineComposer;

  public DecisionEngineService(
      ScoreService scoreService,
      UserProfileMockedService userProfileService,
      RulesEngineComposer rulesEngineComposer) {
    this.scoreService = scoreService;
    this.userProfileService = userProfileService;
    this.rulesEngineComposer = rulesEngineComposer;
  }

  public SimulationResponse simulate(SimulationRequest simulationRequest) {
    var userProfile =
        userProfileService.retrieveUserProfileByPersonalCode(simulationRequest.personalCode());

    var ruleParams =
        new LoadDecisionRuleInputParam(
            userProfile.creditModifier(),
            simulationRequest.loanAmount(),
            simulationRequest.loanPeriod());

    var engineResult =
        rulesEngineComposer
            .setRules(
                new UserHasDebtEngineRule(userProfile.hasDebt()),
                new FindLargerSumEngineRule(scoreService, ruleParams),
                new FindNewSuitablePeriodEngineRule(scoreService, ruleParams))
            .buildExecutor()
            .execute();

    return new SimulationResponse(
        engineResult.success(), engineResult.loanAmount(), engineResult.loanPeriod());
  }
}
