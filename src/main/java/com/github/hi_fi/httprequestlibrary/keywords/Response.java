package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import com.github.hi_fi.httprequestlibrary.domain.Session;
import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RobotKeywords
public class Response {

	@SuppressWarnings("rawtypes")
	@RobotKeyword("Gets latest response in JSON format from given alias.")
	@ArgumentNames({"alias"})
	public Map getJsonResponse(String alias) {
		RestClient rc = new RestClient();
		Session session = rc.getSession(alias);
		return this.toJson(session.getResponseBody());
	}

	@SuppressWarnings("rawtypes")
	@RobotKeyword("Convert a string to a JSON object.")
	@ArgumentNames({"content"})
	public Map toJson(String data) {
		return new Gson().fromJson(data.replace("u'", "'"), Map.class);
	}
	
	@RobotKeyword("Prints out given string as pretty printed JSON.")
	@ArgumentNames({ "content"})
	public String prettyPrintJson(String content) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this.toJson(content));
	}

	@RobotKeyword("Returns HTTP status code of latest response for given alias.")
	@ArgumentNames({"alias"})
	public Integer getResponseStatusCode(String alias) {
		return new RestClient().getSession(alias).getResponse().getStatusLine().getStatusCode();
	}

}
