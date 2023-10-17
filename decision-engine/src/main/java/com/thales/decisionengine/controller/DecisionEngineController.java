package com.thales.decisionengine.controller;

import com.thales.decisionengine.model.request.SimulationRequest;
import com.thales.decisionengine.model.response.SimulationResponse;
import com.thales.decisionengine.service.engine.DecisionEngineService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/decision-engine")
public class DecisionEngineController {

  private final DecisionEngineService engineService;
  private static final Logger log = LogManager.getLogger(DecisionEngineController.class);

  public DecisionEngineController(DecisionEngineService engineService) {
    this.engineService = engineService;
  }

  @PostMapping("/simulate")
  ResponseEntity<SimulationResponse> test(@Valid @RequestBody SimulationRequest request) {
    log.info("Simulation requested: {}", request);
    return ResponseEntity.ok(engineService.simulate(request));
  }
}
