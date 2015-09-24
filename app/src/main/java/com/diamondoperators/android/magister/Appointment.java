package com.diamondoperators.android.magister;


public class Appointment {

    private long startTime, endTime;
    private String name;

    public Appointment(String name, long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
