package org.architecture.statistic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.architecture.events.EventTypeEnum;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class SystemStep {

    private final float time;
    private final int stepCount;
    private final DeviceStepStatistic[] devices;
    private final String log;
    private final BufferStepStatistic buffer;
    private final EventTypeEnum eventType;
    private final Map<String, Integer> eventInformation;
}
