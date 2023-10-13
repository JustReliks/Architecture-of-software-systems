package org.architecture.api.controllers;

import jakarta.validation.constraints.Min;
import org.architecture.api.dto.StatisticDto;
import org.architecture.api.dto.SystemConfigurationDto;
import org.architecture.api.dto.SystemStepDto;
import org.architecture.api.exceptions.SystemNotSimulatedException;
import org.architecture.api.exceptions.WrongStepException;
import org.architecture.api.services.SimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SimulationController {


    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/simulate")
    public ResponseEntity<StatisticDto> simulateSystem(@RequestBody SystemConfigurationDto configurationDto) {
        try {
            return ResponseEntity.ok(StatisticDto.toDto(simulationService.simulate(configurationDto.fromDto())));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    @GetMapping("/simulate/statistic")
    public ResponseEntity<SystemStepDto> getSystemStep(@RequestParam(name = "step") @Min(0) int step) {
        try {
            return ResponseEntity.ok(SystemStepDto.toDto(simulationService.getSimulationStep(step)));
        } catch (SystemNotSimulatedException simulatedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (WrongStepException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/simulate/clear")
    public boolean clearSystem() {
        return simulationService.clear();
    }


}
