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
public class Delete {

	@RobotKeyword(" Send a DELETE request on the session object found using the\n\n"
			 + "given `alias`\n\n"
			 + "``alias`` that will be used to identify the Session object in the cache\n\n"
			 + "``uri`` to send the DELETE request to\n\n"
			 + "``data`` a dictionary of key-value pairs that will be urlencoded and sent as DELETE data or binary data that is sent as the raw body content\n\n"
			 + "``params`` url parameters to append to the uri\n\n"
			 + "``headers`` a dictionary of headers to use with the request\n\n"
			 + "\n\n"
			 + "``allow_redirects`` Boolean. Set to False if redirect following is not allowed.\n\n"
			 + "``timeout`` connection timeout")
	@ArgumentNames({ "alias", "uri", "data=''", "params=", "headers=", "allow_redirects=True", "timeout=0" })
	public ResponseData deleteRequest(String alias, String uri, Object dataList, Map<String, Object> paramSetup, Map<String, Object> headersSetup, Boolean allowRedirects, Integer timeout) {
		RestClient rc = new RestClient();
		Map<String,String> headers = headersSetup != null ? headersSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
        Map<String,String> paramList = paramSetup != null ? paramSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
		if (Robot.isDictionary(dataList.toString())) {
			dataList = (Map<String, Object>) Robot.parseRobotDictionary(dataList.toString());
		}
		rc.makeDeleteRequest(alias, uri, dataList, paramList, headers, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
