package com.nirmiteepublic.clink.models;

public class BroadcastModel {
    private String type, broadcastID, num, profileDP, username, description, time, pinned;
    private int likes, comments;
    private boolean isLiked;


    public BroadcastModel(String type) {
        this.type = type;
    }

    public BroadcastModel() {
    }

    public String getPinned() {
        return pinned;
    }

    public void setPinned(String pinned) {
        this.pinned = pinned;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public boolean isImage() {
        return type.equals("png") || type.equals("jpeg") || type.equals("jpg");
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBroadcastID() {
        return broadcastID;
    }

    public void setBroadcastID(String broadcastID) {
        this.broadcastID = broadcastID;
    }

    public String getProfileDP() {
        return profileDP;
    }

    public void setProfileDP(String profileDP) {
        this.profileDP = profileDP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLikes() {
        return String.valueOf(likes);
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void addLike() {
        likes++;
    }

    public void removeLike() {
        likes--;
    }

    public String getComments() {
        return String.valueOf(comments);
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
