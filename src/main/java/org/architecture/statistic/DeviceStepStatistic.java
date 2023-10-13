package org.architecture.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class DeviceStepStatistic {

    private final int deviceId;
    private Optional<Integer> requestId;
    private boolean isFree = true;


}
