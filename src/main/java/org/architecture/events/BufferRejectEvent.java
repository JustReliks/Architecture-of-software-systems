package org.architecture.events;

import org.architecture.statistic.SourceStatistic;
import org.architecture.system.Request;
import org.architecture.system.SimulatedSystem;

public class BufferRejectEvent extends Event {

    private final int bufferSlot;

    public BufferRejectEvent(Request request, float timeArrived, int bufferSlot) {
        super(request, timeArrived, EventTypeEnum.REJECT);
        this.bufferSlot = bufferSlot;
    }

    @Override
    public void onEvent(SimulatedSystem system) {
        system.getStatistic().addRejected(getRequest().getSourceId());
        getInformation().put("requestId", getRequest().getRequestId());
        getInformation().put("bufferId", bufferSlot);

        SourceStatistic sourceStatistic = system.getStatistic().getSourcesStatistic()[getRequest().getSourceId()];
        sourceStatistic
                .addWaitingTime(getRequest().getRequestId(), getTimeArrival() - getRequest().getTimeArrived());
        sourceStatistic.addServiceTime(getRequest().getRequestId(), 0);
        system.addToLog("[%f] Request %d from %d source was rejected\n",
                getTimeArrival(), getRequest().getRequestId(), getRequest().getSourceId());
    }
}
