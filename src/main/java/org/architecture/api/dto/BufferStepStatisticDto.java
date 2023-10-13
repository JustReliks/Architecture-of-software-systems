package org.architecture.api.dto;

import lombok.Data;
import org.architecture.statistic.BufferStepStatistic;

@Data
public class BufferStepStatisticDto {

    private BufferSlotDto[] requests;
    private int pointer;
    private int bufferSize;
    private int lastAdded;
    private int countRequest;

    public static BufferStepStatisticDto toDto(BufferStepStatistic step) {
        BufferStepStatisticDto dto = new BufferStepStatisticDto();
        dto.setBufferSize(step.getBufferSize());
        dto.setPointer(step.getPointer());
        dto.setCountRequest(step.getCountRequest());
        dto.setLastAdded(step.getLastAdded());
        BufferSlotDto[] requestDtos = new BufferSlotDto[step.getRequests().length];
        for (int i = 0; i < step.getRequests().length; i++) {
            BufferSlotDto bufferSlotDto = new BufferSlotDto();
            bufferSlotDto.setSlotId(i);
            boolean isFree = step.getRequests()[i] == null;
            bufferSlotDto.setFree(isFree);
            bufferSlotDto.setRequestId(isFree ? -1 : step.getRequests()[i].getRequestId());
            requestDtos[i] = bufferSlotDto;
        }
        dto.setRequests(requestDtos);
        return dto;
    }


}
