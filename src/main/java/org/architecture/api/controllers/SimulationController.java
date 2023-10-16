package org.architecture.api.controllers;

import jakarta.validation.constraints.Min;
import org.architecture.api.dto.*;
import org.architecture.api.exceptions.SystemNotSimulatedException;
import org.architecture.api.exceptions.WrongStepException;
import org.architecture.api.services.SimulationService;
import org.architecture.api.services.impl.SimulationServiceImpl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping
public class SimulationController {


    private final SimulationService simulationService;

    public SimulationController(SimulationServiceImpl simulationService) {
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

    @GetMapping("/simulate/edges")
    public ResponseEntity<SimulationEdgesDto> getSimulationEdges() {
        try {
            return ResponseEntity.ok(simulationService.getEdges());

        } catch (SystemNotSimulatedException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/simulate/scheme")
    public ResponseEntity<SystemStepSchemeDto> getStepScheme(@RequestParam(name = "step") @Min(0) int step) {
        try {
            return ResponseEntity.ok(simulationService.getStepScheme(step));
        } catch (SystemNotSimulatedException simulatedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (WrongStepException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/simulate/nodes")
    public ResponseEntity<SimulationNodesDto> getSimulationNodes(@RequestParam(name = "step") @Min(0) int step) {
        try {
            return ResponseEntity.ok(simulationService.getNodes(step));

        } catch (SystemNotSimulatedException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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

    @GetMapping(value = "/simulate/generate-report")
    public ResponseEntity<ByteArrayResource> generateStatistic() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Report.xlsx");

        try {
            return new ResponseEntity<>(new ByteArrayResource(simulationService.generateReport().toByteArray()),
                    headers, HttpStatus.CREATED);
        } catch (SystemNotSimulatedException simulatedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (WrongStepException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }


    @PostMapping("/simulate/clear")
    public boolean clearSystem() {
        return simulationService.clear();
    }


}
