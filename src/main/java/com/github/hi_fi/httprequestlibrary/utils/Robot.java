package com.github.hi_fi.httprequestlibrary.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.python.util.PythonInterpreter;
import com.google.gson.Gson;

public class Robot {

	static RobotLogger logger = new RobotLogger("Robot");
	
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

	@SuppressWarnings({ "unchecked", "resource" })
	public static Map<String, Object> parseRobotDictionary(String dictionary) {
		logger.debug("Dictionary going to be parsed to Map: " + dictionary);
		
		PythonInterpreter py = new PythonInterpreter();
		py.exec("import json");
		Map<String, Object> json = new Gson().fromJson(py.eval("json.dumps("+dictionary+")").toString(), Map.class);
		return json;
	}
	
	public static List<String> parseRobotList(String list) {
		logger.debug("List going to be parsed: " + list);
		return new Gson().fromJson(list.replace("u'", "'"), List.class);
	}

}
