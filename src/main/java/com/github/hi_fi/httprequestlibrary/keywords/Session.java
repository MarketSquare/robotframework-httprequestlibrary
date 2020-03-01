package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httpclient.domain.Authentication;
import com.github.hi_fi.httpclient.domain.Proxy;
import com.github.hi_fi.httpclient.RestClient;
import com.github.hi_fi.httprequestlibrary.utils.Robot;
import com.github.hi_fi.httprequestlibrary.utils.RobotLogger;

@RobotKeywords
public class Session {

	@RobotKeyword("Create a HTTP session to a server\n\n"
			 + "``url`` Base url of the server\n\n"
			 + "``alias`` Robot Framework alias to identify the session\n\n"
			 + "``headers`` Dictionary of default headers\n\n"
			 + "``auth`` List of username & password for HTTP Basic Auth\n\n"
			 + "``timeout`` Connection timeout\n\n"
			 + "\n\n"
			 + "``proxy`` Dictionary that contains proxy information. Only one proxy supported per session. Dictionary should contain at least following keys: *protocol*, *host* and *port* of proxy. It can also contain *username* and *password*\n\n"
			 + "``verify`` Whether the SSL cert will be verified. A CA_BUNDLE path can also be provided.\n\n"
			 + "``debug`` Enable http verbosity option more information. Note that this is not bound to session, but for the state of system. So if another session with debug=False is created, the earlier with debug=True is not printing debug from HTTPclient anymore\n\n")
	@ArgumentNames({ "alias", "url", "headers=", "cookies=", "auth=", "timeout=0", "proxy=",
			"verify=False", "debug=False" })
	public void createSession(String alias, String url, Map<String, Object> headersSetup, String cookies, List<String> authConfig, Integer timeout, Map<String, Object> proxySetup, String verify, Boolean debug) {
		RestClient rc = new RestClient();
		Map<String,String> proxyConfig = proxySetup != null ? proxySetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
		Map<String,String> headers = headersSetup != null ? headersSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
		authConfig = authConfig != null ? authConfig : new ArrayList<String>();
		Proxy proxy = new Proxy(proxyConfig);
		RobotLogger.setDebugToAll(debug);
		Authentication auth = Authentication
				.getAuthentication(authConfig);

		rc.createSession(alias, url, headers, auth, verify, debug, proxy);
	}

	@RobotKeyword("Create a HTTP session to a server\n\n"
			 + "``url`` Base url of the server\n\n"
			 + "``alias`` Robot Framework alias to identify the session\n\n"
			 + "``headers`` Dictionary of default headers\n\n"
			 + "``auth`` List of username & password for HTTP Digest Auth\n\n"
			 + "``timeout`` Connection timeout\n\n"
			 + "\n\n"
			 + "``proxy`` Dictionary that contains proxy information. Only one proxy supported per session. Dictionary should contain at least following keys: *protocol*, *host* and *port* of proxy. It can also contain *username* and *password*\n\n"
			 + "``verify`` Whether the SSL cert will be verified. A CA_BUNDLE path can also be provided.\n\n"
			 + "``debug`` Enable http verbosity option more information. Note that this is not bound to session, but for the state of system. So if another session with debug=False is created, the earlier with debug=True is not printing debug from HTTPclient anymore\n\n")
	@ArgumentNames({ "alias", "url", "headers=", "cookies=", "auth=", "timeout=0", "proxy=",
			"verify=False", "debug=False" })
	public void createDigestSession(String alias, String url, Map<String, Object> headersSetup, String cookies, List<String> authConfig, Integer timeout, Map<String, Object> proxySetup, String verify, Boolean debug) {
		RestClient rc = new RestClient();
		Map<String,String> proxyConfig = proxySetup != null ? proxySetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
        Map<String,String> headers = headersSetup != null ? headersSetup.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue())): new HashMap<String, String>();
        authConfig = authConfig != null ? authConfig : new ArrayList<String>();
		Proxy proxy = new Proxy(proxyConfig);
		RobotLogger.setDebugToAll(debug);
		Authentication auth = Authentication
				.getAuthentication(authConfig, Authentication.Type.DIGEST);
		rc.createSession(alias, url, headers, auth, verify, debug, proxy);
	}

}
