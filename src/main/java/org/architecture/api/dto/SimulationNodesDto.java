package org.architecture.api.dto;

import lombok.Data;
import org.architecture.events.EventTypeEnum;
import org.architecture.statistic.BufferStepStatistic;
import org.architecture.statistic.DeviceStepStatistic;
import org.architecture.statistic.SystemStep;
import org.architecture.system.Request;
import org.architecture.system.SimulatedSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SimulationNodesDto {

    private List<NodeDto> nodes;

    public static SimulationNodesDto toDto(SimulatedSystem system, int step) {
        SimulationNodesDto simulationNodesDto = new SimulationNodesDto();
        List<NodeDto> nodes = new ArrayList<>();


        SystemStep systemStep = system.getStatistic().getSystemStatistic().getStep(step);
        EventTypeEnum eventType = systemStep.getEventType();

        Map<String, Integer> eventInformation = systemStep.getEventInformation();
        for (int i = 0; i < system.getSourcesSize(); i++) {
            NodeDto nodeDto = new NodeDto();
            nodeDto.setId("s" + i);
            DataDto data = new DataDto();
            data.setLabel("Источник №" + i);
            if (eventInformation.containsKey("sourceId") && eventInformation.get("sourceId") == i) {
                data.setColor("#32b32b");
                data.setDescription("Новая заявка: " + eventInformation.get("requestId"));
            }
            nodeDto.setData(data);
            nodes.add(nodeDto);
        }

        NodeDto dp = new NodeDto();
        dp.setId("dp");
        DataDto dataDp = new DataDto();
        dataDp.setLabel("ДП");
        dp.setData(dataDp);
        nodes.add(dp);

        NodeDto reject = new NodeDto();
        reject.setId("rej");
        DataDto dataRej = new DataDto();
        dataRej.setLabel("Отказ");

        if (eventType == EventTypeEnum.REJECT) {
            dataRej.setColor("#CC3300");
            dataRej.setDescription("Заявка: " + eventInformation.get("requestId"));
        }
        reject.setData(dataRej);

        nodes.add(reject);

        BufferStepStatistic buffer = systemStep.getBuffer();
        for (int i = 0; i < buffer.getBufferSize(); i++) {
            Request request = buffer.getRequests()[i];
            NodeDto nodeDto = new NodeDto();
            nodeDto.setId("b" + i);
            DataDto data = new DataDto();
            data.setLabel("Буфер №" + i);
            if (buffer.getPointer() == i) {
                data.setLabel(data.getLabel() + " (*)");
            }
            if (eventInformation.containsKey("bufferId")
                    && eventInformation.get("bufferId") == i) {
                data.setColor("#32b32b");
            }
            if (request != null) {
                data.setDescription("Заявка: " + request.getRequestId());
            } else {
                data.setDescription("Пустой");
            }
            nodeDto.setData(data);
            nodes.add(nodeDto);
        }

        for (int i = 0; i < systemStep.getDevices().length; i++) {
            DeviceStepStatistic device = systemStep.getDevices()[i];
            NodeDto nodeDto = new NodeDto();
            nodeDto.setId("d" + i);
            DataDto data = new DataDto();
            data.setLabel("Прибор №" + i);
            if (eventInformation.containsKey("deviceId") && eventInformation.get("deviceId") == i) {
                data.setColor("#32b32b");
            }
            if (device.isFree()) {
                data.setDescription("Свободен");
            } else {
                data.setDescription("Заявка: " + device.getRequestId().get());
            }
            nodeDto.setData(data);
            nodes.add(nodeDto);
        }

        NodeDto dv = new NodeDto();
        dv.setId("dv");
        DataDto dataDv = new DataDto();
        dataDv.setLabel("ДВ");
        dv.setData(dataDv);
        nodes.add(dv);


        simulationNodesDto.setNodes(nodes);
        return simulationNodesDto;
    }

}
