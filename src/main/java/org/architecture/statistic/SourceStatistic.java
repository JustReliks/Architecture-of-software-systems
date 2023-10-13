package org.architecture.statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SourceStatistic {

    private int generatedRequests;

    private int rejectedRequests;

    private int completedRequests;

    private int bufferedRequests;

    private final Map<Integer, Float> serviceTimeMap;
    private final Map<Integer, Float> waitingTimeMap;

    public SourceStatistic() {
        waitingTimeMap = new HashMap<>();
        serviceTimeMap = new HashMap<>();
    }


    public float getProbabilityOfReject() {
        if (generatedRequests == 0) return 0;
        return (float) rejectedRequests / (float) generatedRequests;
    }

    public float getAverageServiceTime() {
        if (completedRequests == 0) return 0;
        return getFullServiceTime() / ((float) completedRequests);
    }

    public float getAverageWaitingTime() {
        if (bufferedRequests == 0) return 0;
        return getFullWaitingTime() / (float) bufferedRequests;
    }

    public float getFullServiceTime() {
        return serviceTimeMap.values().stream().reduce(0f, Float::sum);
    }

    public float getFullWaitingTime() {
        return waitingTimeMap.values().stream().reduce(0f, Float::sum);
    }

    public void addServiceTime(int request, float time) {
        serviceTimeMap.put(request, time);
    }

    public void addWaitingTime(int request, float time) {
        waitingTimeMap.put(request, time);
    }

    public float getAverageRequestExistingTime() {
        return getAverageServiceTime() + getAverageWaitingTime();
    }

    public float getWaitingTimeDispersion() {
        float averageWaitingTime = getAverageWaitingTime();
        if (waitingTimeMap.size() == 0) {
            return 0;
        }
        return (1.0f / waitingTimeMap.size()) * waitingTimeMap.values().
                stream().map(x -> (float) Math.pow(x - averageWaitingTime, 2)).reduce(0f, Float::sum);
    }

    public float getServiceTimeDispersion() {
        float averageServiceTime = getAverageServiceTime();
        if (serviceTimeMap.size() == 0) {
            return 0;
        }
        return (1.0f / serviceTimeMap.size()) * serviceTimeMap.values().
                stream().map(x -> (float) Math.pow(x - averageServiceTime, 2)).reduce(0f, Float::sum);

    }
}
