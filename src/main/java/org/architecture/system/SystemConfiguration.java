package org.architecture.system;

public record SystemConfiguration(int bufferSize, float averageRequestCount, int sourcesSize, int devicesSize,
                                  float alpha, float betta, int requestCount) {

}
