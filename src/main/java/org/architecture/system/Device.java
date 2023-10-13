package org.architecture.system;

import lombok.Getter;
import lombok.Setter;
import org.architecture.events.DeviceFreeEvent;
import org.architecture.exceptions.SystemSimulatingException;
import org.architecture.statistic.DeviceStepStatistic;

import java.util.Optional;


@Getter
@Setter
public class Device {

    private final int deviceId;
    private Optional<Integer> requestId;
    private boolean isFree = true;
    private final SimulatedSystem system;

    public Device(int deviceId, SimulatedSystem system) {
        this.deviceId = deviceId;
        this.system = system;
        requestId = Optional.empty();
    }

    public void putRequest(Request request) {
        if (!isFree) throw new SystemSimulatingException("Device is busy");
        system.addToLog("[%f] Device %d get request %d in work\n", request.getTimeArrived(), deviceId, request.getRequestId());
        isFree = false;
        requestId = Optional.of(request.getRequestId());
        system.getEvents().add(new DeviceFreeEvent(request, request.getTimeArrived() + getNewTime(), deviceId));

    }

    private float getNewTime() {
        return (float) (Math.random() * (system.getBetta() - system.getAlpha()) + system.getAlpha());
    }

    public void takeRequest() {
        requestId = Optional.empty();
        isFree = true;
    }

    public DeviceStepStatistic getDeviceStep() {
        return new DeviceStepStatistic(deviceId, requestId, isFree);
    }

}
