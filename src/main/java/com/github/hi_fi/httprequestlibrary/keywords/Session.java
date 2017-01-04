package com.github.hi_fi.httprequestlibrary.keywords;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.utils.RestClient;

@RobotKeywords
public class Session {
	
	@RobotKeyword
	@ArgumentNames({"alias", "url", "headers={}", "cookies=None", "auth=None", "timeout=None", "proxies=None", "verify=False", "debug=0", "max_retries=3", "backoff_factor=0.1", "disable_warnings=0"})
	public void createSession(String alias, String url, String...params) {
		RestClient rc = new RestClient();
		rc.createSession(alias, url);
	}

}
