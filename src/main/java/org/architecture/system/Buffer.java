package org.architecture.system;

import lombok.Getter;
import org.architecture.events.BufferRejectEvent;
import org.architecture.exceptions.SystemSimulatingException;
import org.architecture.statistic.BufferStepStatistic;


@Getter
public class Buffer {

    private final Request[] requests;
    private int pointer;
    private final int bufferSize;
    private int lastAdded;

    private final SimulatedSystem system;

    private int countRequest;

    public Buffer(int bufferSize, SimulatedSystem system) {
        requests = new Request[bufferSize];
        this.system = system;
        pointer = 0;
        this.bufferSize = bufferSize;
        countRequest = 0;
    }

    public int addRequest(Request request) {
        system.addToLog("[%f] Request %d put in buffer\n",
                request.getTimeArrived(), request.getRequestId());

        int freePos = findFreePosition();
        boolean reject = freePos == -1;
        freePos = reject ? lastAdded : freePos;
        if (reject) {
            system.putEvent(new BufferRejectEvent(requests[freePos], request.getTimeArrived(), freePos));
        } else {
            countRequest++;
        }
        requests[freePos] = request;
        lastAdded = freePos;
        pointer = getNextPosition(freePos);

        return freePos;
    }

    public Request freeRequest() {
        int nextPosition = pointer;

        boolean start = true;
        while (start || nextPosition != pointer) {
            if (requests[nextPosition] != null) {
                Request request = requests[nextPosition];
                requests[nextPosition] = null;
                pointer = getNextPosition(nextPosition);
                countRequest--;
                return request;
            }
            start = false;
            nextPosition = getNextPosition(nextPosition);
        }
        throw new SystemSimulatingException("Try to free request from empty buffer");
    }

    private int findFreePosition() {
        boolean start = true;
        for (int pos = pointer; pos != pointer || start; pos = getNextPosition(pos)) {
            start = false;
            if (requests[pos] == null) {
                return pos;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return countRequest == 0;
    }

    public int getNextPosition(int pos) {
        if (pos == bufferSize - 1) {
            return 0;
        }
        return pos + 1;
    }

    public int getPrevPosition(int pos) {
        if (pos == 0) {
            return bufferSize - 1;
        }
        return pos - 1;
    }

    public void clear() {
        for (int i = 0; i < bufferSize; i++) {
            requests[i] = null;
        }
        pointer = 0;
        lastAdded = 0;
        countRequest = 0;

    }

    public BufferStepStatistic getBufferStatistic() {
        Request[] copyRequests = new Request[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            if (requests[i] != null) {
                copyRequests[i] = requests[i].clone();
            }
        }
        return new BufferStepStatistic(copyRequests, pointer, bufferSize, lastAdded, countRequest);
    }
}
