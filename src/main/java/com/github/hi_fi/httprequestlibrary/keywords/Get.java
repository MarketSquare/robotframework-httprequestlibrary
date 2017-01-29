package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.HashMap;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httpclient.RestClient;
import com.github.hi_fi.httpclient.domain.ResponseData;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Get {

	@RobotKeyword(" Send a GET request on the session object found using the\n\n"
			 + "given `alias`\n\n"
			 + "``alias`` that will be used to identify the Session object in the cache\n\n"
			 + "``uri`` to send the GET request to\n\n"
			 + "``params`` url parameters to append to the uri\n\n"
			 + "``headers`` a dictionary of headers to use with the request\n\n"
			 + "\n\n"
			 + "``allow_redirects`` Boolean. Set to True if redirect following is allowed.\n\n"
			 + "``timeout`` connection timeout")
	@ArgumentNames({ "alias", "uri", "headers={}", "params={}", "allow_redirects=true", "timeout=0" })
	public ResponseData getRequest(String alias, String uri, String... params) {
		RestClient rc = new RestClient();
		Boolean allowRedirects = Boolean.parseBoolean(Robot.getParamsValue(params, 2, "true"));
		Map<String, String> paramList = Robot.getParamsValue(params, 1,
				(Map<String, String>) new HashMap<String, String>());
		Map<String, String> headers = Robot.getParamsValue(params, 0,
				(Map<String, String>) new HashMap<String, String>());
		rc.makeGetRequest(alias, uri, headers, paramList, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
