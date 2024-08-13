package com.nirmiteepublic.clink.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

	@SerializedName("user")
	private List<UserItem> user;

	@SerializedName("status")
	private String status;

	public void setUser(List<UserItem> user){
		this.user = user;
	}

	public List<UserItem> getUser(){
		return user;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}