package com.nirmiteepublic.clink.models;

import com.google.gson.annotations.SerializedName;

public class OtpDataItem{

	@SerializedName("num")
	private String num;

	@SerializedName("__v")
	private int v;

	@SerializedName("otp")
	private String otp;

	@SerializedName("_id")
	private String id;

	@SerializedName("time")
	private String time;

	public String getNum(){
		return num;
	}

	public int getV(){
		return v;
	}

	public String getOtp(){
		return otp;
	}

	public String getId(){
		return id;
	}

	public String getTime(){
		return time;
	}
}