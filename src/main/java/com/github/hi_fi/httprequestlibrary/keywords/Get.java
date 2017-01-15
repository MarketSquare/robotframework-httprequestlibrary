package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.HashMap;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Get {
	
	@RobotKeyword
	@ArgumentNames({"alias", "uri", "headers={}", "params={}", "allow_redirects=true", "timeout=0"})
	public void getRequest(String alias, String uri, String...params) {
		RestClient rc = new RestClient();
		Boolean allowRedirects = Boolean.parseBoolean(Robot.getParamsValue(params, 2, "true"));
		Map<String, String> paramList = Robot.getParamsValue(params, 1, (Map<String, String>) new HashMap<String, String>());
		rc.makeGetRequest(alias, uri, paramList, allowRedirects);
	}
}
