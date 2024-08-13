package com.nirmiteepublic.clink.models;

import com.google.gson.annotations.SerializedName;

public class RoleResponseItem{

	@SerializedName("__v")
	private int v;

	@SerializedName("name")
	private String name;

	@SerializedName("_id")
	private String id;

	public void setV(int v){
		this.v = v;
	}

	public int getV(){
		return v;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}