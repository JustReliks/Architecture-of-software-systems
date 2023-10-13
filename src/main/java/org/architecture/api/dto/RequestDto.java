package org.architecture.api.dto;

import lombok.Data;
import org.architecture.system.Request;

@Data
public class RequestDto {

    private int requestId;
    private int sourceId;
    private float timeArrived;

    public static RequestDto toDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestId(request.getRequestId());
        requestDto.setSourceId(request.getSourceId());
        requestDto.setTimeArrived(request.getTimeArrived());

        return requestDto;
    }


}
