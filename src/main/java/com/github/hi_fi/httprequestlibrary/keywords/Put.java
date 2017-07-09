package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.HashMap;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httpclient.domain.ResponseData;
import com.github.hi_fi.httpclient.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Put {

	@RobotKeyword(" Send a PUT request on the session object found using the\n\n"
			 + "given `alias`\n\n"
			 + "``alias`` that will be used to identify the Session object in the cache\n\n"
			 + "``uri`` to send the PUT request to\n\n"
			 + "``data`` a dictionary of key-value pairs that will be urlencoded and sent as PUT data or binary data that is sent as the raw body content\n\n"
			 + "``params`` url parameters to append to the uri\n\n"
			 + "``headers`` a dictionary of headers to use with the request\n\n"
			 + "``files`` a dictionary of file names containing file data to PUT to the server\n\n"
			 + "\n\n"
			 + "``allow_redirects`` Boolean. Set to True if redirect following is allowed.\n\n"
			 + "``timeout`` connection timeout")
	@ArgumentNames({ "alias", "uri", "data={}", "params={}", "headers={}", "files=", "allow_redirects=False",
			"timeout=0" })
	public ResponseData putRequest(String alias, String uri, String... params) {
		RestClient rc = new RestClient();
		Object dataList = (String) Robot.getParamsValue(params, 0, "");
		if (Robot.isDictionary(dataList.toString())) {
			dataList = (Map<String, String>) Robot.getParamsValue(params, 0,
					(Map<String, String>) new HashMap<String, String>());
		}
		Map<String, String> paramList = Robot.getParamsValue(params, 1,
				(Map<String, String>) new HashMap<String, String>());
		Map<String, String> headers = Robot.getParamsValue(params, 2,
				(Map<String, String>) new HashMap<String, String>());
		Map<String, String> files = Robot.getParamsValue(params, 3,
				(Map<String, String>) new HashMap<String, String>());
		Boolean allowRedirects = Boolean.parseBoolean(Robot.getParamsValue(params, 4, "true"));
		rc.makePutRequest(alias, uri, dataList, paramList, headers, files, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
