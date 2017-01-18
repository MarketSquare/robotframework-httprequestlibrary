package com.github.hi_fi.httprequestlibrary.utils;

import java.util.List;
import java.util.Map;
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
		dictionary = StringEscapeUtils.escapeJava(dictionary);
		logger.debug("Escaped dictionary going to be parsed to Map: " + dictionary);
		Map<String, Object> json = new Gson().fromJson(dictionary.replace("u'", "'"), Map.class);
		PythonInterpreter py = new PythonInterpreter();
		for (Object key : json.keySet()) {
			String newKey = py.eval("'"+key+"'.decode('utf-8')").toString();
			String newValue = py.eval("'"+json.get(key)+"'.decode('utf-8')").toString();
			json.remove(key);
			json.put(newKey, newValue);
		}
		return json;
	}
	
	public static List<String> parseRobotList(String list) {
		logger.debug("List going to be parsed: " + list);
		return new Gson().fromJson(list.replace("u'", "'"), List.class);
	}

}
