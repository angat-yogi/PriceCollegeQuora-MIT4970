package com.angat.askmeanything.model.comment;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CommentResponse{

	@SerializedName("comments")
	private List<CommentsItem> comments;

	@SerializedName("message")
	private String message;

	public CommentResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}

	@SerializedName("status")
	private int status;

	public void setComments(List<CommentsItem> comments){
		this.comments = comments;
	}

	public List<CommentsItem> getComments(){
		return comments;
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