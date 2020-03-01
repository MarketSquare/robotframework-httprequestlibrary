package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
			 + "``allow_redirects`` Boolean. Set to False if redirect following is not allowed.\n\n"
			 + "``timeout`` connection timeout")
	@ArgumentNames({ "alias", "uri", "data=", "headers=", "files=", "allow_redirects=True", "timeout=0" })
	public ResponseData patchRequest(String alias, String uri, Object dataList, Map<String, Object> headersSetup, Map<String, Object> filesSetup, Boolean allowRedirects, Integer timeout) {
		RestClient rc = new RestClient();
		Map<String,String> headers = headersSetup != null ? headersSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
        Map<String,String> files = filesSetup != null ? filesSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
		if (Robot.isDictionary(dataList.toString())) {
			dataList = (Map<String, Object>) Robot.parseRobotDictionary(dataList.toString());
		}
		rc.makePatchRequest(alias, uri, dataList, headers, files, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
