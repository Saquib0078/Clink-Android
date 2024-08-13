package com.nirmiteepublic.clink.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NotificationModel{

	@SerializedName("notification")
	private List<NotificationItem> notification;

	@SerializedName("status")
	private String status;

	public void setNotification(List<NotificationItem> notification){
		this.notification = notification;
	}

	public List<NotificationItem> getNotification(){
		return notification;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}