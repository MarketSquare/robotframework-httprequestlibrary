package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httpclient.domain.ResponseData;
import com.github.hi_fi.httpclient.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;

@RobotKeywords
public class Head {

	@RobotKeyword(" Send a HEAD request on the session object found using the\n\n"
			 + "given `alias`\n\n"
			 + "``alias`` that will be used to identify the Session object in the cache\n\n"
			 + "``uri`` to send the HEAD request to\n\n"
			 + "``headers`` a dictionary of headers to use with the request\n\n"
			 + "\n\n"
			 + "``allow_redirects`` Boolean. Set to True if redirect following is allowed.\n\n"
			 + "``timeout`` connection timeout")
	@ArgumentNames({ "alias", "uri", "headers=", "allow_redirects=False", "timeout=0" })
	public ResponseData headRequest(String alias, String uri, Map<String, Object> headersSetup, Boolean allowRedirects, Integer timeout) {
		RestClient rc = new RestClient();
		Map<String,String> headers = headersSetup != null ? headersSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
		rc.makeHeadRequest(alias, uri, headers, allowRedirects);
		return rc.getSession(alias).getResponseData();
	}
}
