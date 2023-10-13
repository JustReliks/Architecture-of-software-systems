package org.architecture.api.dto;

import lombok.Data;
import org.architecture.statistic.SystemStep;

@Data
public class SystemStepDto {

    private float time;
    private int stepCount;
    private EventInformationDto eventInformation;
    private BufferStepStatisticDto buffer;
    private DeviceStepStatisticDto[] devices;
    private String log;


    public static SystemStepDto toDto(SystemStep step) {
        SystemStepDto dto = new SystemStepDto();
        dto.setEventInformation(EventInformationDto.toDto(step));
        dto.setTime(step.getTime());
        dto.setStepCount(step.getStepCount());
        dto.setLog(step.getLog());
        dto.setBuffer(BufferStepStatisticDto.toDto(step.getBuffer()));
        DeviceStepStatisticDto[] deviceDtos = new DeviceStepStatisticDto[step.getDevices().length];
        for (int i = 0; i < step.getDevices().length; i++) {
            deviceDtos[i] = DeviceStepStatisticDto.toDto(step.getDevices()[i]);
        }
        dto.setDevices(deviceDtos);

        return dto;
    }

}
