package org.architecture.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionDto {

    public static final PositionDto EMPTY_POSITION = new PositionDto(0, 0);


    private double x;
    private double y;

}
