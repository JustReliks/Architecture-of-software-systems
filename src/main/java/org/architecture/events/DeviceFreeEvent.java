package org.architecture.events;

import lombok.Getter;
import org.architecture.statistic.DeviceStatistic;
import org.architecture.statistic.SourceStatistic;
import org.architecture.system.Request;
import org.architecture.system.SimulatedSystem;

@Getter
public class DeviceFreeEvent extends Event {

    private final int deviceId;

    public DeviceFreeEvent(Request request, float timeArrival, int deviceId) {
        super(request, timeArrival, EventTypeEnum.DEVICE_FREE);
        this.deviceId = deviceId;
    }

    @Override
    public void onEvent(SimulatedSystem system) {
        int sourceId = getRequest().getSourceId();
        int requestId = getRequest().getRequestId();

        system.getStatistic().addCompleted(sourceId);
        float time = getTimeArrival() - getRequest().getTimeArrived();
        SourceStatistic sourceStatistic = system.getStatistic().getSourcesStatistic()[sourceId];
        sourceStatistic.addServiceTime(requestId, time);
        DeviceStatistic deviceStatistic = system.getStatistic().getDevicesStatistic()[deviceId];
        deviceStatistic.addBusyTime(time);
        system.getDevices()[deviceId].takeRequest();
        getInformation().put("deviceId", deviceId);


        system.addToLog("[%f] Device %d is free, completed request %d\n",
                getTimeArrival(), getDeviceId(), requestId);
        if (!system.getBuffer().isEmpty()) {
            Request request = system.getBuffer().freeRequest();
            getInformation().put("bufferId", system.getBuffer()
                    .getPrevPosition(system.getBuffer().getPointer()));

            system.getStatistic().getSourcesStatistic()[request.getSourceId()].addWaitingTime(request.getRequestId(), getTimeArrival() - request.getTimeArrived());
            request.setTimeArrived(getTimeArrival());
            system.addToLog("[%f] Request %d was free from buffer\n", getTimeArrival(), request.getRequestId());
            system.getDevices()[deviceId].putRequest(request);
        }
    }
}
