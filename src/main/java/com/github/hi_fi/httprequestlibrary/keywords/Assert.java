package com.github.hi_fi.httprequestlibrary.keywords;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Assert {

	@RobotKeyword("Compares status code from latest response from given connection with given expected value.")
	@ArgumentNames({"alias","expected HTTP status code"})
	public void responseCodeShouldBe(String alias, int expectedCode) {
		RestClient rc = new RestClient();
		int actualStatusCode = rc.getSession(alias).getResponse().getStatusLine().getStatusCode();
		if (actualStatusCode != expectedCode) {
			throw new RuntimeException(String.format("Status code was not as expected. Expected %s, but got %s",
					expectedCode, actualStatusCode));
		}
	}

	@RobotKeyword("Checks that latest response from given connection contains given expected value. Default check is case sensitive.")
	@ArgumentNames({"alias","expected content", "case_sensitive=False"})
	public void responseShouldContain(String alias, String expectedContent, String... params) {
		RestClient rc = new RestClient();
		Boolean caseSensitive = Boolean.parseBoolean(Robot.getParamsValue(params, 0, "false"));
		String actualContent = rc.getSession(alias).getResponseBody();
		String testActualContent = caseSensitive ? actualContent : actualContent.toUpperCase();
		String testExpectedContent = caseSensitive ? expectedContent : expectedContent.toUpperCase();
		if (!testActualContent.contains(testExpectedContent)) {
			throw new RuntimeException(
					String.format("Expected content was not in response. Expected %s in response, but it was %s",
							expectedContent, actualContent));
		}
	}

}
