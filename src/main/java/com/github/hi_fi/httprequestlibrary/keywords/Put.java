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
			 + "``allow_redirects`` Boolean. Set to False if redirect following is not allowed.\n\n"
			 + "``timeout`` connection timeout")
	@ArgumentNames({ "alias", "uri", "data=''", "params=", "headers=", "files=", "allow_redirects=True",
			"timeout=0" })
	public ResponseData putRequest(String alias, String uri, Object dataList, Map<String, Object> paramSetup, Map<String, Object> headerSetup, Map<String, Object> filesSetup, Boolean allowRedirects, Integer timeout) {
		RestClient rc = new RestClient();
		Map<String,String> params = paramSetup != null ? paramSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
	    Map<String,String> headers = headerSetup != null ? headerSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
        Map<String,String> files = filesSetup != null ? filesSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
		if (Robot.isDictionary(dataList.toString())) {
			dataList = (Map<String, Object>) Robot.parseRobotDictionary(dataList.toString());
		}
		rc.makePutRequest(alias, uri, dataList, params, headers, files, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
