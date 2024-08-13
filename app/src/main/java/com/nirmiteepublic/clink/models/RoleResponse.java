package com.nirmiteepublic.clink.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RoleResponse{

	@SerializedName("role")
	private List<RoleItem> role;

	@SerializedName("status")
	private String status;

	public void setRole(List<RoleItem> role){
		this.role = role;
	}

	public List<RoleItem> getRole(){
		return role;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}