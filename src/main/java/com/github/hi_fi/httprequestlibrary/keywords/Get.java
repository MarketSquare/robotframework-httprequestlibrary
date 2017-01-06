package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Get {
	
	@RobotKeyword
	@ArgumentNames({"alias", "uri", "headers={}", "params={}", "allow_redirects=False", "timeout=0"})
	public void getRequest(String alias, String uri, String...params) {
		RestClient rc = new RestClient();
		Map<String, String> paramList = Robot.getParamsValue(params, 1, "{}");
		rc.makeGetRequest(alias, uri, paramList);
	}
}
