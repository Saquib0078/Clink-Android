package com.nirmiteepublic.clink.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NotificationItem{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("regTime")
	private String regTime;

	@SerializedName("__v")
	private int v;

	@SerializedName("_id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("body")
	private String body;

	@SerializedName("phoneNumbers")
	private List<String> phoneNumbers;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setRegTime(String regTime){
		this.regTime = regTime;
	}

	public String getRegTime(){
		return regTime;
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

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return body;
	}

	public void setPhoneNumbers(List<String> phoneNumbers){
		this.phoneNumbers = phoneNumbers;
	}

	public List<String> getPhoneNumbers(){
		return phoneNumbers;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}