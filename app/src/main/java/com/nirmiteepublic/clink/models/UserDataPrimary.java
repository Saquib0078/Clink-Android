package com.nirmiteepublic.clink.models;

public class UserDataPrimary{
	private String owner;
	private String lName;
	private int role;
	private String num;
	private boolean completed;
	private String image;
	private String regTime;
	private String fName;
	private String frameName;
	private int v;
	private String frameAdd;
	private String id;
	private String status;

	public void setOwner(String owner){
		this.owner = owner;
	}

	public String getOwner(){
		return owner;
	}

	public void setLName(String lName){
		this.lName = lName;
	}

	public String getLName(){
		return lName;
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

	public void setCompleted(boolean completed){
		this.completed = completed;
	}

	public boolean isCompleted(){
		return completed;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
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

	public void setFrameName(String frameName){
		this.frameName = frameName;
	}

	public String getFrameName(){
		return frameName;
	}

	public void setV(int v){
		this.v = v;
	}

	public int getV(){
		return v;
	}

	public void setFrameAdd(String frameAdd){
		this.frameAdd = frameAdd;
	}

	public String getFrameAdd(){
		return frameAdd;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
