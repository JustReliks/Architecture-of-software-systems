package org.architecture.statistic;

import lombok.Getter;

@Getter
public class DeviceStatistic {

    private float busyTime;

    public void addBusyTime(float time) {
        busyTime += time;
    }

    public float getBusyRate(float fullTime) {
        return busyTime / fullTime;
    }

}
