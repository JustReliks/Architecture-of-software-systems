package org.architecture.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDto {

    private String label;
    private String color;

    public void appendLabel(String append) {
        label += "\n" + append;
    }

}
