package org.architecture.events;

import lombok.Getter;
import org.architecture.system.Request;
import org.architecture.system.SimulatedSystem;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Event {

    private final Request request;
    private final float timeArrival;
    private final EventTypeEnum type;
    private final Map<String, Integer> information;

    protected Event(Request request, float timeArrival, EventTypeEnum type) {
        this.type = type;
        this.request = request;
        this.timeArrival = timeArrival;

        information = new HashMap<>();
    }

    public abstract void onEvent(SimulatedSystem system);


    public static class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event o1, Event o2) {
            return Float.compare(o1.getTimeArrival(), o2.getTimeArrival());
        }
    }

}
