package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.domain.Authentication;
import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Session {

	@RobotKeyword
	@ArgumentNames({ "alias", "url", "headers={}", "cookies=None", "auth=None", "timeout=None", "proxies=None",
			"verify=False", "debug=False", "max_retries=3", "backoff_factor=0.1", "disable_warnings=0" })
	public void createSession(String alias, String url, String... params) {
		RestClient rc = new RestClient();
		Map<String, String> headers = Robot.getParamsValue(params, 0, new HashMap<String, String>());
		String verify = Robot.getParamsValue(params, 5, "False");
		Boolean debug = Boolean.parseBoolean(Robot.getParamsValue(params, 6, "False"));
		Authentication auth = Authentication
				.getAuthentication(Robot.getParamsValue(params, 2, (List<String>) new ArrayList<String>()));
		rc.createSession(alias, url, headers, auth, verify, debug);
	}

	@RobotKeyword
	@ArgumentNames({ "alias", "url", "headers={}", "cookies=None", "auth=None", "timeout=None", "proxies=None",
			"verify=False", "debug=False", "max_retries=3", "backoff_factor=0.1", "disable_warnings=0" })
	public void createDigestSession(String alias, String url, String... params) {
		RestClient rc = new RestClient();
		Map<String, String> headers = Robot.getParamsValue(params, 0, new HashMap<String, String>());
		String verify = Robot.getParamsValue(params, 5, "False");
		Boolean debug = Boolean.parseBoolean(Robot.getParamsValue(params, 6, "False"));
		Authentication auth = Authentication.getAuthentication(
				Robot.getParamsValue(params, 2, (List<String>) new ArrayList<String>()), Authentication.Type.DIGEST);
		rc.createSession(alias, url, headers, auth, verify, debug);
	}

}
