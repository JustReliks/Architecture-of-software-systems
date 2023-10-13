package org.architecture.api.dto;

import lombok.Data;
import org.architecture.statistic.BufferStepStatistic;

@Data
public class BufferStepStatisticDto {

    private RequestDto[] requests;
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
        RequestDto[] requestDtos = new RequestDto[step.getRequests().length];
        for (int i = 0; i < step.getRequests().length; i++) {
            if(step.getRequests()[i] != null) {
                requestDtos[i] = RequestDto.toDto(step.getRequests()[i]);
            }
        }
        dto.setRequests(requestDtos);
        return dto;
    }


}
