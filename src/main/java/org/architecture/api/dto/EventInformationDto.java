package org.architecture.api.dto;

import lombok.Data;
import org.architecture.events.EventTypeEnum;
import org.architecture.statistic.SystemStep;

import java.util.Map;

@Data
public class EventInformationDto {

    private EventTypeEnum eventType;
    private Map<String, Integer> information;

    public static EventInformationDto toDto(SystemStep step) {
        EventInformationDto dto = new EventInformationDto();
        dto.setEventType(step.getEventType());
        dto.setInformation(step.getEventInformation());

        return dto;
    }

}
