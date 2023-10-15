package org.architecture.api.services;

import org.architecture.api.dto.SimulationEdgesDto;
import org.architecture.api.dto.SimulationNodesDto;
import org.architecture.api.dto.SystemStepSchemeDto;
import org.architecture.statistic.Statistic;
import org.architecture.statistic.SystemStep;
import org.architecture.system.SystemConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface SimulationService {

    Statistic simulate(SystemConfiguration configuration);

    SystemStep getSimulationStep(int step);

    ByteArrayOutputStream generateReport() throws IOException;

    SimulationEdgesDto getEdges();

    boolean clear();

    SimulationNodesDto getNodes(int step);

    SystemStepSchemeDto getStepScheme(int step);
}
