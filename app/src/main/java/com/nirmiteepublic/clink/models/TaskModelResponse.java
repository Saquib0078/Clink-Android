package com.nirmiteepublic.clink.models;

public class TaskModelResponse{
	private Task task;
	private String status;

	public void setTask(Task task){
		this.task = task;
	}

	public Task getTask(){
		return task;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
