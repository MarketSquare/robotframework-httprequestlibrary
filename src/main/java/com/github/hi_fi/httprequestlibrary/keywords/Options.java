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
public class Options {

	@RobotKeyword(" Send a OPTIONS request on the session object found using the\n\n"
			 + "given `alias`\n\n"
			 + "``alias`` that will be used to identify the Session object in the cache\n\n"
			 + "``uri`` to send the OPTIONS request to\n\n"
			 + "``headers`` a dictionary of headers to use with the request\n\n"
			 + "\n\n"
			 + "``allow_redirects`` Boolean. Set to True if redirect following is allowed.\n\n"
			 + "``timeout`` connection timeout")
	@ArgumentNames({ "alias", "uri", "headers={}", "allow_redirects=False", "timeout=0" })
	public ResponseData optionsRequest(String alias, String uri, String... params) {
		RestClient rc = new RestClient();
		Map<String, String> headers = Robot.getParamsValue(params, 0,
				(Map<String, String>) new HashMap<String, String>());
		Boolean allowRedirects = Boolean.parseBoolean(Robot.getParamsValue(params, 1, "true"));
		rc.makeOptionsRequest(alias, uri, headers, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
