package org.architecture.api.dto;

import lombok.Data;
import org.architecture.system.SystemConfiguration;

@Data
public class SystemConfigurationDto {

    private int requestCount;
    private int bufferSize;
    private int sourcesSize;
    private int devicesSize;
    private float alpha;
    private float betta;
    private float averageRequestCount;


    public SystemConfiguration fromDto() {
        return new SystemConfiguration(bufferSize, averageRequestCount, sourcesSize, devicesSize, alpha, betta, requestCount);
    }

}
