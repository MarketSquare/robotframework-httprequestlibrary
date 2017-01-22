package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.HashMap;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.domain.ResponseData;
import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Options {
	
	@RobotKeyword
	@ArgumentNames({"alias", "uri", "headers={}", "allow_redirects=False", "timeout=0"})
	public ResponseData optionsRequest(String alias, String uri, String...params) {
		RestClient rc = new RestClient();
		Map<String, String> headers = Robot.getParamsValue(params, 0, (Map<String, String>) new HashMap<String, String>());
		Boolean allowRedirects = Boolean.parseBoolean(Robot.getParamsValue(params, 1, "true"));
		rc.makeOptionsRequest(alias, uri, headers, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
