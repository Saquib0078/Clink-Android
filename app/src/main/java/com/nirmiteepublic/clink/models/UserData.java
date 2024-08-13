package com.nirmiteepublic.clink.models;

// Model class for user data
public class UserData {
    private final String userName;
    private final String userLocation;

    public UserData(String userName, String userLocation) {
        this.userName = userName;
        this.userLocation = userLocation;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserLocation() {
        return userLocation;
    }
}



