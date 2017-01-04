package com.github.hi_fi.httprequestlibrary.keywords;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.utils.RestClient;

@RobotKeywords
public class Assert {
	
	@RobotKeyword
	public void responseCodeShouldBe(String alias, int expectedCode) {
		RestClient rc = new RestClient();
		int actualStatusCode = rc.getSession(alias).getResponse().getStatusLine().getStatusCode();
		if (actualStatusCode != expectedCode) {
			throw new RuntimeException(String.format("Status code was not as expected. Expected %s, but got %s", expectedCode, actualStatusCode));
		}
	}
	
	@RobotKeyword
	public void responseShouldContain(String alias, String expectedContent) {
		RestClient rc = new RestClient();
		String actualContent = rc.getSession(alias).getResponseBody();
		if (!actualContent.contains(expectedContent)) {
			throw new RuntimeException(String.format("Expected content was not in response. Expected %s in response, but it was %s", expectedContent, actualContent));
		}
	}

}
