package com.nirmiteepublic.clink.models;

public class MeetModel {
    private final String meetName;
    private final String meetDescription;
    private final String date;

    public MeetModel(String meetName, String meetDescription, String date, String time, String imageID, String id, String radioButtonValue, boolean isLive) {
        this.meetName = meetName;
        this.meetDescription = meetDescription;
        this.date = date;
        this.time = time;
        this.imageID = imageID;
        this.id = id;
        this.radioButtonValue = radioButtonValue;
        this.isLive = isLive;
    }

    private final String time;
    private final String imageID;
    private final String id;

    public String getRadioButtonValue() {
        return radioButtonValue;
    }

    private final String radioButtonValue;

    public void setLive(boolean live) {
        isLive = live;

    }

    private  boolean isLive;



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
