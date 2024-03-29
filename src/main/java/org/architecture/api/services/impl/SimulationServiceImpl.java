package org.architecture.api.services.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.architecture.api.dto.SimulationEdgesDto;
import org.architecture.api.dto.SimulationNodesDto;
import org.architecture.api.dto.SystemStepSchemeDto;
import org.architecture.api.exceptions.SystemNotSimulatedException;
import org.architecture.api.exceptions.WrongStepException;
import org.architecture.api.services.SimulationService;
import org.architecture.report.ReportGenerator;
import org.architecture.statistic.Statistic;
import org.architecture.statistic.SystemStep;
import org.architecture.system.SimulatedSystem;
import org.architecture.system.SystemConfiguration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class SimulationServiceImpl implements SimulationService {

    private SimulatedSystem system;

    public Statistic simulate(SystemConfiguration configuration) {
        system = new SimulatedSystem(configuration);
        system.startSimulation(configuration.requestCount());

        return system.getStatistic();
    }

    public SystemStep getSimulationStep(int step) {
        if (system == null) throw new SystemNotSimulatedException();
        if (step >= system.getStatistic().getSystemStatistic().getStepCount()) throw new WrongStepException();

        return system.getStatistic().getSystemStatistic().getStep(step);
    }

    public ByteArrayOutputStream generateReport() throws IOException {
        if (system == null) throw new SystemNotSimulatedException();
        ReportGenerator generator = new ReportGenerator();
        try (Workbook wb = generator.generateXLSXReport(system.getStatistic());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            wb.write(outputStream);
            return outputStream;
        }
    }

    public SimulationEdgesDto getEdges() {
        if (system == null) throw new SystemNotSimulatedException();
        return SimulationEdgesDto.toDto(system);
    }

    public boolean clear() {
        if (system == null) return false;
        system = null;
        return true;
    }

    @Override
    public SimulationNodesDto getNodes(int step) {
        if (system == null) throw new SystemNotSimulatedException();
        if (step >= system.getStatistic().getSystemStatistic().getStepCount()) throw new WrongStepException();
        return SimulationNodesDto.toDto(system, step);
    }

    @Override
    public SystemStepSchemeDto getStepScheme(int step) {
        if (system == null) throw new SystemNotSimulatedException();
        if (step >= system.getStatistic().getSystemStatistic().getStepCount()) throw new WrongStepException();
        return SystemStepSchemeDto.toDto(system, step);
    }

}
