package org.architecture.statistic;

import lombok.Getter;
import lombok.Setter;
import org.architecture.system.SimulatedSystem;

@Getter
@Setter
public class Statistic {

    private int rejected;
    private int generated;
    private int completed;

    private final SourceStatistic[] sourcesStatistic;
    private final DeviceStatistic[] devicesStatistic;

    private final SystemStatistic systemStatistic;

    private float fullTime;

    private final SimulatedSystem system;


    public Statistic(SimulatedSystem system) {
        this.system = system;
        sourcesStatistic = new SourceStatistic[system.getSourcesSize()];
        for (int i = 0; i < system.getSourcesSize(); i++) {
            sourcesStatistic[i] = new SourceStatistic();
        }
        devicesStatistic = new DeviceStatistic[system.getDevicesSize()];
        for (int i = 0; i < system.getDevicesSize(); i++) {
            devicesStatistic[i] = new DeviceStatistic();
        }
        systemStatistic = new SystemStatistic(system);
    }

    public void clear() {
        fullTime = 0;
        rejected = 0;
        generated = 0;
        completed = 0;
        for (int i = 0; i < system.getSourcesSize(); i++) {
            sourcesStatistic[i] = new SourceStatistic();
        }
        for (int i = 0; i < system.getDevicesSize(); i++) {
            devicesStatistic[i] = new DeviceStatistic();
        }
        systemStatistic.clear();
    }

    public void addRejected(int sourceId) {
        sourcesStatistic[sourceId].setRejectedRequests(sourcesStatistic[sourceId].getRejectedRequests() + 1);
        rejected++;
    }

    public void addCompleted(int sourceId) {
        sourcesStatistic[sourceId].setCompletedRequests(sourcesStatistic[sourceId].getCompletedRequests() + 1);
        completed++;
    }

    public void addBufferedRequest(int sourceId) {
        sourcesStatistic[sourceId].setBufferedRequests(sourcesStatistic[sourceId].getBufferedRequests() + 1);
    }

    public void addGenerated(int sourceId) {
        sourcesStatistic[sourceId].setGeneratedRequests(sourcesStatistic[sourceId].getGeneratedRequests() + 1);
        generated++;
    }


    public int eventsCount() {
        return rejected + generated + completed;
    }

    public float getRejectProbability() {
        return (float) rejected / (float) generated;
    }


}
