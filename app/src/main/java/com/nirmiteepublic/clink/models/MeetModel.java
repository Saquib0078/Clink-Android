package com.nirmiteepublic.clink.models;

public class MeetModel {
    private final String meetName, meetDescription, date, time, imageID,id;
    private final boolean isLive;

    public MeetModel(String meetName, String meetDescription, String date, String time, String imageID, String id, boolean isLive) {
        this.meetName = meetName;
        this.meetDescription = meetDescription;
        this.date = date;
        this.time = time;
        this.imageID = imageID;
        this.id = id;
        this.isLive = isLive;
    }

    public String getMeetName() {
        return meetName;
    }

    public String getMeetDescription() {
        return meetDescription;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImageID() {
        return imageID;
    }

    public String getId() {
        return id;
    }

    public boolean isLive() {
        return isLive;
    }
}
