package edu.unicauca.mongorester.miscellaneus;

import com.google.gson.Gson;

public class BackResponse {

	private int code;
	private String comments;
	
	public BackResponse(int code, String comments) {
		super();
		this.code = code;
		this.comments = comments;
	}
	public BackResponse() {
		super();
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String to_json(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
