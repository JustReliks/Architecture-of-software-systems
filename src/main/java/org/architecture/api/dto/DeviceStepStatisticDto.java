package org.architecture.api.dto;

import lombok.Data;
import org.architecture.statistic.DeviceStepStatistic;

@Data
public class DeviceStepStatisticDto {

    private int deviceId;
    private int requestId;
    private boolean isFree;

    public static DeviceStepStatisticDto toDto(DeviceStepStatistic step) {
        DeviceStepStatisticDto dto = new DeviceStepStatisticDto();
        dto.setDeviceId(step.getDeviceId());
        step.getRequestId().ifPresent(dto::setRequestId);
        dto.setFree(step.isFree());

        return dto;
    }


}
