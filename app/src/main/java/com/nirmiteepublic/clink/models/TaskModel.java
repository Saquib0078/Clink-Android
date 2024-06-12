package com.nirmiteepublic.clink.models;

public class TaskModel {
    private final String taskName, taskDescription, date, time, imageID,id;
    String createdBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public TaskModel(String taskName, String taskDescription, String date, String time, String imageID, String id) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.date = date;
        this.time = time;
        this.imageID = imageID;
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
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
}
