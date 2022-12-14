package com.angat.askmeanything.model;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse{

	@SerializedName("message")
	private String message;

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@SerializedName("extra")
	private String extra;

	@SerializedName("status")
	private int status;

    public GeneralResponse(String message, int status) {
		this.message=message;
		this.status=status;
    }

    public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}