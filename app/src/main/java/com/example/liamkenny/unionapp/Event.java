package com.example.liamkenny.unionapp;

/**
 * Created by Robbie on 27/05/2018.
 */

class Event {
    private String name;
    private String info;
    private String socID;
    private String startTime;
    private String endTime;

    public Event(String name, String info, String socID, String startTime, String endTime, String location) {
        this.name = name;
        this.info = info;
        this.socID = socID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSocID() {
        return socID;
    }

    public void setSocID(String socID) {
        this.socID = socID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
