package com.nirmiteepublic.clink.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OtpResponse{

	@SerializedName("otpData")
	private List<OtpDataItem> otpData;

	@SerializedName("status")
	private String status;

	public List<OtpDataItem> getOtpData(){
		return otpData;
	}

	public String getStatus(){
		return status;
	}
}