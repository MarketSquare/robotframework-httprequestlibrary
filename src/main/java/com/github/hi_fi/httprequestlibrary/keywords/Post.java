package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.HashMap;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Post {
	
	@RobotKeyword
	@ArgumentNames({"alias", "uri", "data={}", "params={}", "headers={}", "files=", "allow_redirects=False", "timeout=0"})
	public void postRequest(String alias, String uri, String...params) {
		RestClient rc = new RestClient();
		Object dataList = "";
		try {
			dataList = Robot.getParamsValue(params, 0, (Map<String, String>) new HashMap<String, String>());
		} catch (Exception e) {
			dataList = Robot.getParamsValue(params, 0, "");
		}
		Map<String, String> paramList = Robot.getParamsValue(params, 1, (Map<String, String>) new HashMap<String, String>());
		rc.makePostRequest(alias, uri, dataList, paramList);
	}
}
