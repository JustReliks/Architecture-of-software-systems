package org.architecture.api.dto;

import lombok.Data;

@Data
public class NodeDto {

    private String id;
    private DataDto data;
    private PositionDto position = PositionDto.EMPTY_POSITION;

}
