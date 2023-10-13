package org.architecture.statistic;

import lombok.Getter;
import org.architecture.system.SimulatedSystem;

import java.util.HashMap;
import java.util.Map;

public class SystemStatistic {

    private final SimulatedSystem simulatedSystem;
    private final Map<Integer, SystemStep> stepMap;
    @Getter
    private int stepCount;

    public SystemStatistic(SimulatedSystem simulatedSystem) {
        this.simulatedSystem = simulatedSystem;
        stepMap = new HashMap<>();
    }

    public void createStep() {
        DeviceStepStatistic[] deviceStepStatistic = new DeviceStepStatistic[simulatedSystem.getDevicesSize()];
        for (int i = 0; i < simulatedSystem.getDevicesSize(); i++) {
            deviceStepStatistic[i] = simulatedSystem.getDevices()[i].getDeviceStep();
        }
        SystemStep step = new SystemStep(simulatedSystem.getTime(),
                stepCount, deviceStepStatistic,
                simulatedSystem.getStepLog().toString(),
                simulatedSystem.getBuffer().getBufferStatistic(),
                simulatedSystem.getLastEvent().getType(),
                simulatedSystem.getLastEvent().getInformation());

        stepMap.put(stepCount++, step);
    }

    public void clear() {
        stepMap.clear();
        stepCount = 0;
    }

    public SystemStep getStep(int step) {
        return stepMap.get(step);
    }

}
