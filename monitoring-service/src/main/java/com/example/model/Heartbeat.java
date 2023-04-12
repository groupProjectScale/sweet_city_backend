package com.example.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Heartbeat {
    private String service;

    private boolean isRunning;

    private long timeStamp;

    public Heartbeat() {}

    public Heartbeat(String service, boolean isRunning, long timeStamp) {
        this.service = service;
        this.isRunning = isRunning;
        this.timeStamp = timeStamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Heartbeat{"
                + "service='"
                + service
                + '\''
                + ", isRunning="
                + isRunning
                + ", timeStamp="
                + timeStamp
                + '}';
    }
}
