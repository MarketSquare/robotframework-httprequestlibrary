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
public class Patch {

	@RobotKeyword(" Send a PATCH request on the session object found using the\n\n"
			 + "given `alias`\n\n"
			 + "``alias`` that will be used to identify the Session object in the cache\n\n"
			 + "``uri`` to send the PATCH request to\n\n"
			 + "``data`` a dictionary of key-value pairs that will be urlencoded and sent as PATCH data or binary data that is sent as the raw body content\n\n"
			 + "``headers`` a dictionary of headers to use with the request\n\n"
			 + "``files`` a dictionary of file names containing file data to PATCH to the server\n\n"
			 + "\n\n"
			 + "``allow_redirects`` Boolean. Set to True if redirect following is allowed.\n\n"
			 + "``timeout`` connection timeout")
	@ArgumentNames({ "alias", "uri", "data={}", "headers={}", "files={}", "allow_redirects=False", "timeout=0" })
	public ResponseData patchRequest(String alias, String uri, String... params) {
		RestClient rc = new RestClient();
		Object dataList = (String) Robot.getParamsValue(params, 0, "");
		if (Robot.isDictionary(dataList.toString())) {
			dataList = (Map<String, String>) Robot.getParamsValue(params, 0,
					(Map<String, String>) new HashMap<String, String>());
		}
		Map<String, String> headers = Robot.getParamsValue(params, 1,
				(Map<String, String>) new HashMap<String, String>());
		Map<String, String> files = Robot.getParamsValue(params, 2,
				(Map<String, String>) new HashMap<String, String>());
		Boolean allowRedirects = Boolean.parseBoolean(Robot.getParamsValue(params, 3, "false"));
		rc.makePatchRequest(alias, uri, dataList, headers, files, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
