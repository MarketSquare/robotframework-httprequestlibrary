package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.HashMap;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.domain.ResponseData;
import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;
import com.github.hi_fi.httprequestlibrary.utils.RobotLogger;

@RobotKeywords
public class Delete {
	
	@RobotKeyword
	@ArgumentNames({"alias", "uri", "data={}", "params={}", "headers={}", "allow_redirects=False", "timeout=0"})
	public ResponseData deleteRequest(String alias, String uri, String...params) {
		RestClient rc = new RestClient();
		Object dataList = (String) Robot.getParamsValue(params, 0, "");
		if (Robot.isDictionary(dataList.toString())) {
			dataList = (Map<String, String>) Robot.getParamsValue(params, 0, (Map<String, String>) new HashMap<String, String>());
		} 
		Map<String, String> paramList = Robot.getParamsValue(params, 1, (Map<String, String>) new HashMap<String, String>());
		Map<String, String> headers = Robot.getParamsValue(params, 2, (Map<String, String>) new HashMap<String, String>());
		Boolean allowRedirects = Boolean.parseBoolean(Robot.getParamsValue(params, 3, "true"));
		rc.makeDeleteRequest(alias, uri, dataList, paramList, headers, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
