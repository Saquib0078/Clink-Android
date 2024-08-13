package com.nirmiteepublic.clink.models;

public class UserModel {
    private String username, dp, address, _id,num;
    private boolean isSelected;
    private boolean online;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public UserModel() {
    }

    public UserModel(String username, String dp, String address, String _id, String num, boolean isSelected) {
        this.username = username;
        this.dp = dp;
        this.address = address;
        this._id = _id;
        this.num = num;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public UserModel(String username, String dp, String address, String _id) {
        this.username = username;
        this.dp = dp;
        this.address = address;
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}