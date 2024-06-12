package com.nirmiteepublic.clink.models;

public class CompleteduserModel {
    private String fName;
    private String lName;
    private String num;
    private String dp;


    public CompleteduserModel(String fName, String lName, String num, String dp) {
        this.fName = fName;
        this.lName = lName;
        this.num = num;
        this.dp = dp;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
