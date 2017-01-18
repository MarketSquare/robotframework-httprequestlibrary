package com.github.hi_fi.httprequestlibrary.domain;

import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.gson.Gson;

public class ResponseData {

	public int status_code;
	public String text;
	public String content;
	@SuppressWarnings("rawtypes")
	public Map json;


	public int getStatusCode() {
		return status_code;
	}


	public void setStatusCode(int status_code) {
		this.status_code = status_code;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
		this.content = text;
		try {
			this.json = new Gson().fromJson(this.text, Map.class);
		} catch (Exception e) {
		}
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}

}
