package org.architecture.api.dto;

import lombok.Data;
import org.architecture.events.EventTypeEnum;
import org.architecture.statistic.SystemStep;
import org.architecture.system.SimulatedSystem;

import java.util.List;

@Data
public class SystemStepSchemeDto {

    private EventTypeEnum eventType;
    private String log;
    private float stepTime;
    private int step;
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;

    public static SystemStepSchemeDto toDto(SimulatedSystem system, int step) {
        SystemStepSchemeDto scheme = new SystemStepSchemeDto();
        scheme.setNodes(SimulationNodesDto.toDto(system, step).getNodes());
        scheme.setEdges(SimulationEdgesDto.toDto(system).getEdges());

        SystemStep systemStep = system.getStatistic().getSystemStatistic().getStep(step);
        scheme.setLog(systemStep.getLog());
        scheme.setStepTime(systemStep.getTime());
        scheme.setEventType(systemStep.getEventType());
        scheme.setStep(step);

        return scheme;
    }
}
