package org.architecture.events;

import org.architecture.exceptions.SystemSimulatingException;
import org.architecture.system.Device;
import org.architecture.system.Request;
import org.architecture.system.SimulatedSystem;

import java.util.Optional;

public class GenerateRequestEvent extends Event {

    private final int sourceId;

    public GenerateRequestEvent(Request request, float timeArrival, int sourceId) {
        super(request, timeArrival, EventTypeEnum.GENERATE_REQUEST);
        this.sourceId = sourceId;
    }

    @Override
    public void onEvent(SimulatedSystem system) {
        getRequest().setRequestId(system.getStatistic().getGenerated());
        system.addToLog("[%f] Source %d generated request %d\n",
                getTimeArrival(), sourceId, getRequest().getRequestId());
        system.getStatistic().addGenerated(sourceId);
        getInformation().put("sourceId", sourceId);
        if (sourceId != getRequest().getSourceId()) {
            throw new SystemSimulatingException("Different sources");
        }
        Optional<Device> freeDevice = system.getFreeDevice();
        freeDevice.ifPresentOrElse(device -> {
                    device.putRequest(getRequest());
                    getInformation().put("deviceId", device.getDeviceId());
                },
                () ->
                {
                    int i = system.getBuffer().addRequest(getRequest());
                    getInformation().put("bufferId", i);
                    system.getStatistic().addBufferedRequest(sourceId);
                    system.getStatistic().getSourcesStatistic()[sourceId].addWaitingTime(getRequest().getRequestId(), 0);
                });
        float timeNextEvent = getTimeNextEvent(system);
        system.putEvent(new GenerateRequestEvent(new Request(getRequest().getSourceId(), timeNextEvent), timeNextEvent, sourceId));

    }

    private float getTimeNextEvent(SimulatedSystem system) {
        return getTimeNextEvent(system, getTimeArrival());
    }

    public static float getTimeNextEvent(SimulatedSystem system, float currentTime) {
        return (float) (currentTime + -1.0 / (system.getAverageRequestCount()) * Math.log(Math.random()));
    }

}
