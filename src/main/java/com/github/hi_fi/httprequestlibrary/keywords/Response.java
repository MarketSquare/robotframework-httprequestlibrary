package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.Map;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import com.github.hi_fi.httprequestlibrary.domain.Session;
import com.github.hi_fi.httprequestlibrary.utils.RobotLogger;
import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RobotKeywords
public class Response {
	
	@RobotKeyword
	public Map getJsonResponse(String alias) {
		RestClient rc = new RestClient();
		Session session = rc.getSession(alias);
		Map jsonJavaRootObject = new Gson().fromJson(session.getResponseBody(), Map.class);
		return jsonJavaRootObject;
	}
	
	@RobotKeyword
	public Integer getResponseStatusCode(String alias) {
		return new RestClient().getSession(alias).getResponse().getStatusLine().getStatusCode();
	}

}
