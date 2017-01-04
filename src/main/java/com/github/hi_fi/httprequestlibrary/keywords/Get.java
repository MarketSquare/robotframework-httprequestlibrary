package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.utils.Logger;
import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.google.gson.Gson;

@RobotKeywords
public class Get {
	
	@RobotKeyword
	@ArgumentNames({"alias", "uri", "headers={}", "params={}", "allow_redirects=False", "timeout=0"})
	public void getRequest(String alias, String uri, String...params) {
		RestClient rc = new RestClient();
		Map<String, String> paramList = this.getParamsValue(params, 1, "{}");
		rc.makeGetRequest(alias, uri, paramList);
	}
	
	private Map<String, String> getParamsValue(String[] params, int index, String defaultValue) {
		String value = defaultValue;
		if (params.length > index) {
			value = params[index];
		}
		return this.parseRobotDictionary(value);
	}
	
	private Map<String, String> parseRobotDictionary(String dictionary) {
		return new Gson().fromJson(dictionary.replace("u'", "'"), Map.class);
	}

}
