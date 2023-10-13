package org.architecture.system;

import lombok.Getter;
import org.architecture.events.Event;
import org.architecture.events.GenerateRequestEvent;
import org.architecture.statistic.Statistic;

import java.util.*;

@Getter
public class SimulatedSystem {

    private final Buffer buffer;
    private final float averageRequestCount;

    private final Device[] devices;

    private final PriorityQueue<Event> events;

    private final int devicesSize;
    private final int sourcesSize;
    private final Random random;
    private boolean simulating;
    private final Statistic statistic;

    private final float alpha;
    private final float betta;
    private float time;
    private final List<StringBuilder> log;
    private StringBuilder stepLog;

    private Event lastEvent;


    public SimulatedSystem(int bufferSize, float averageRequestCount, int sourcesSize, int devicesSize, float alpha, float betta) {
        this.averageRequestCount = averageRequestCount;
        this.devicesSize = devicesSize;
        this.sourcesSize = sourcesSize;
        this.alpha = alpha;
        this.betta = betta;
        log = new ArrayList<>();
        this.random = new Random();
        statistic = new Statistic(this);
        this.buffer = new Buffer(bufferSize, this);
        events = new PriorityQueue<>(new Event.EventComparator());
        this.devices = new Device[devicesSize];
        for (int i = 0; i < devicesSize; i++) {
            devices[i] = new Device(i, this);
        }
    }

    public SimulatedSystem(SystemConfiguration configuration) {
        this(configuration.bufferSize(), configuration.averageRequestCount(), configuration.sourcesSize(), configuration.devicesSize(), configuration.alpha(), configuration.betta());
    }

    public void startSimulation(int maxGeneratedRequests) {
        simulating = true;
        init();
        while (!events.isEmpty() && simulating) {
            stepLog = new StringBuilder();
            Event poll = events.poll();
            if (statistic.getGenerated() < maxGeneratedRequests || !(poll instanceof GenerateRequestEvent)) {
                poll.onEvent(this);
                time = poll.getTimeArrival();
                lastEvent = poll;
                statistic.getSystemStatistic().createStep();
            }
            if (events.isEmpty()) {
                statistic.setFullTime(poll.getTimeArrival());
            }
        }
        addToLog("Simulation finished at %d event. Events remaining in queue: %d\n",
                statistic.eventsCount(), events.size());
        addToLog("Statistic: generated requests: %d; Rejected requests: %d; Completed requests: %d\n",
                statistic.getGenerated(), statistic.getRejected(), statistic.getCompleted());
    }

    private void init() {
        log.clear();
        events.clear();
        buffer.clear();
        statistic.clear();
        stepLog = null;
        time = 0;
        for (int i = 0; i < devicesSize; i++) {
            devices[i] = new Device(i, this);
        }

        for (int i = 0; i < sourcesSize; i++) {
            float timeNextEvent = GenerateRequestEvent.getTimeNextEvent(this, 0);
            events.add(new GenerateRequestEvent(new Request(i, timeNextEvent), timeNextEvent, i));
        }

    }

    public Optional<Device> getFreeDevice() {
        for (int i = 0; i < devicesSize; i++) {
            if (devices[i].isFree()) {
                return Optional.ofNullable(devices[i]);
            }
        }
        return Optional.empty();
    }

    public void putEvent(Event event) {
        events.add(event);
    }

    public void addToLog(String str, Object... params) {
        String formatted = String.format(str, params);
        if (log.isEmpty()) {
            log.add(new StringBuilder());
        }
        StringBuilder builder = log.get(log.size() - 1);
        if (builder.length() >= Integer.MAX_VALUE / 2 - 1) {
            builder = new StringBuilder();
            log.add(builder);
        }
        builder.append(formatted);
        stepLog.append(formatted);
    }


}
