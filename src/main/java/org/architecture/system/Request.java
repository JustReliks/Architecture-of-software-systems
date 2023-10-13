package org.architecture.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request implements Cloneable {

    private int requestId;
    private final int sourceId;
    private float timeArrived;


    public Request(int source, float timeArrived) {
        this.sourceId = source;
        this.timeArrived = timeArrived;
    }

    @Override
    public Request clone() {
        try {
            return (Request) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
