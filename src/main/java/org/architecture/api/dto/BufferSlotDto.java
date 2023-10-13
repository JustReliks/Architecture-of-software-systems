package org.architecture.api.dto;

import lombok.Data;

@Data
public class BufferSlotDto {

    private boolean isFree;
    private int slotId;
    private int requestId;


}
