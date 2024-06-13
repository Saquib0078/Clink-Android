package com.nirmiteepublic.clink.models;

import com.google.gson.annotations.SerializedName;

public class UserItem {

	@SerializedName("lName")
	private String lName;

	@SerializedName("regTime")
	private String regTime;

	@SerializedName("fName")
	private String fName;
	@SerializedName("dp")
	private String dp;

	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}

	@SerializedName("role")
	private int role;

	@SerializedName("num")
	private String num;

	@SerializedName("__v")
	private int v;

	@SerializedName("_id")
	private String id;

	@SerializedName("completed")
	private boolean completed;

	@SerializedName("owner")
	private String owner;

	@SerializedName("FrameName")
	private String frameName;

	@SerializedName("FrameAdd")
	private String frameAdd;

	@SerializedName("status")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@SerializedName("Image")
	private String image;

	public void setLName(String lName){
		this.lName = lName;
	}

	public String getLName(){
		return lName;
	}

	public void setRegTime(String regTime){
		this.regTime = regTime;
	}

	public String getRegTime(){
		return regTime;
	}

	public void setFName(String fName){
		this.fName = fName;
	}

	public String getFName(){
		return fName;
	}

	public void setRole(int role){
		this.role = role;
	}

	public int getRole(){
		return role;
	}

	public void setNum(String num){
		this.num = num;
	}

	public String getNum(){
		return num;
	}

	public void setV(int v){
		this.v = v;
	}

	public int getV(){
		return v;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCompleted(boolean completed){
		this.completed = completed;
	}

	public boolean isCompleted(){
		return completed;
	}

	public void setOwner(String owner){
		this.owner = owner;
	}

	public String getOwner(){
		return owner;
	}

	public void setFrameName(String frameName){
		this.frameName = frameName;
	}

	public String getFrameName(){
		return frameName;
	}

	public void setFrameAdd(String frameAdd){
		this.frameAdd = frameAdd;
	}

	public String getFrameAdd(){
		return frameAdd;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}
}