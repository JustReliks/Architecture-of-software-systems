package org.architecture.api.dto;

import lombok.Data;
import org.architecture.system.SimulatedSystem;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimulationEdgesDto {

    private List<EdgeDto> edges;

    public static SimulationEdgesDto toDto(SimulatedSystem system) {
        SimulationEdgesDto edgesDto = new SimulationEdgesDto();
        List<EdgeDto> edges = new ArrayList<>();
        for (int i = 0; i < system.getSourcesSize(); i++) {
            EdgeDto edgeDto = new EdgeDto();
            edgeDto.setId(String.format("es%d_dp", i));
            edgeDto.setSource("s" + i);
            edgeDto.setTarget("dp");
            edges.add(edgeDto);
        }
        EdgeDto reject = new EdgeDto();
        reject.setId("edp_rej");
        reject.setSource("dp");
        reject.setTarget("rej");
        edges.add(reject);

        for (int i = 0; i < system.getBuffer().getBufferSize(); i++) {
            EdgeDto fromDp = new EdgeDto();
            fromDp.setId(String.format("edp_b%d", i));
            fromDp.setSource("dp");
            fromDp.setTarget("b" + i);
            edges.add(fromDp);
            EdgeDto toDv = new EdgeDto();
            toDv.setId(String.format("eb%d_dv", i));
            toDv.setSource("b" + i);
            toDv.setTarget("dv");
            edges.add(toDv);
        }
        for (int i = 0; i < system.getDevicesSize(); i++) {
            EdgeDto fromDv = new EdgeDto();
            fromDv.setId(String.format("edv_d%d", i));
            fromDv.setSource("dv");
            fromDv.setTarget("d" + i);
            edges.add(fromDv);
        }


        edgesDto.setEdges(edges);
        return edgesDto;
    }

}
