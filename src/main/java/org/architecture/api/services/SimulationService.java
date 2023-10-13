package org.architecture.api.services;

import org.architecture.api.exceptions.SystemNotSimulatedException;
import org.architecture.api.exceptions.WrongStepException;
import org.architecture.statistic.Statistic;
import org.architecture.statistic.SystemStep;
import org.architecture.system.SimulatedSystem;
import org.architecture.system.SystemConfiguration;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

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

    public boolean clear() {
        if(system == null) return false;
        system = null;
        return true;
    }

}
