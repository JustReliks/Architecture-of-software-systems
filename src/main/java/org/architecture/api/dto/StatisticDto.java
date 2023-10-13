package org.architecture.api.dto;

import lombok.Data;
import org.architecture.statistic.Statistic;

@Data
public class StatisticDto {

    private int rejected;
    private int generated;
    private int completed;
    private int stepCount;
    private float fullTime;

    public static StatisticDto toDto(Statistic statistic) {
        StatisticDto dto = new StatisticDto();
        dto.setCompleted(statistic.getCompleted());
        dto.setGenerated(statistic.getGenerated());
        dto.setRejected(statistic.getRejected());
        dto.setStepCount(statistic.getSystemStatistic().getStepCount());
        dto.setFullTime(statistic.getFullTime());

        return dto;
    }

}
