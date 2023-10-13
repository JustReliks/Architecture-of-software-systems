package org.architecture.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.architecture.system.Request;

@Getter
@AllArgsConstructor
public class BufferStepStatistic {

    private final Request[] requests;
    private int pointer;
    private final int bufferSize;
    private int lastAdded;
    private int countRequest;


}
