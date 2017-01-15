package com.github.hi_fi.httprequestlibrary.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Robot {

	static RobotLogger logger = new RobotLogger();
	
	public static <T> T getParamsValue(String[] params, int index, T defaultValue) {
		T value = defaultValue;
		String givenValue = null;
		if (params.length > index) {
			givenValue = params[index];
			logger.debug("Value from arguments: " + givenValue);
		}

		if (givenValue != null && !givenValue.equals("None")) {
			if (defaultValue instanceof Map) {
				value = (T) parseRobotDictionary(givenValue);
			} else if (defaultValue instanceof String) {
				value = (T) givenValue;
			} else if (defaultValue instanceof List) {
				value = (T) parseRobotList(givenValue);
			}
			
		}
		return value;
	}

	public static Map<String, String> parseRobotDictionary(String dictionary) {
		return new Gson().fromJson(dictionary.replace("u'", "'"), Map.class);
	}
	
	public static List<String> parseRobotList(String list) {
		return new Gson().fromJson(list.replace("u'", "'"), List.class);
	}

}
