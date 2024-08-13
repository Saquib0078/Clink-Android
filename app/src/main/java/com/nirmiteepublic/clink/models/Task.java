package com.nirmiteepublic.clink.models;

import java.util.List;

public class Task{
	private String date;
	private int comments;
	private String dp;

	public String getRadioButtonValue() {
		return radioButtonValue;
	}

	public void setRadioButtonValue(String radioButtonValue) {
		this.radioButtonValue = radioButtonValue;
	}

	private String radioButtonValue;


	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	private String taskUrl;

	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}

	private String taskDescription;
	private String createdAt;
	private String createdBy;
	private int v;
	private String taskName;
	private List<Object> completedUsers;
	private String id;
	private String time;
	private String status;
	private String updatedAt;
	private String imageID;

	public String getImageID() {
		return imageID;
	}

	public void setImageID(String imageID) {
		this.imageID = imageID;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setComments(int comments){
		this.comments = comments;
	}

	public int getComments(){
		return comments;
	}



	public void setTaskDescription(String taskDescription){
		this.taskDescription = taskDescription;
	}

	public String getTaskDescription(){
		return taskDescription;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setV(int v){
		this.v = v;
	}

	public int getV(){
		return v;
	}

	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getTaskName(){
		return taskName;
	}

	public void setCompletedUsers(List<Object> completedUsers){
		this.completedUsers = completedUsers;
	}

	public List<Object> getCompletedUsers(){
		return completedUsers;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}