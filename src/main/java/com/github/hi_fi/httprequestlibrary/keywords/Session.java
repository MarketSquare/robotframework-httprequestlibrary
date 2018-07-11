package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
	@ArgumentNames({ "alias", "url", "headers={}", "cookies=None", "auth=None", "timeout=None", "proxy=None",
			"verify=False", "debug=False" })
	public void createSession(String alias, String url, String... params) {
		RestClient rc = new RestClient();
		Map<String, String> headers = Robot.getParamsValue(params, 0, new HashMap<String, String>());
		Proxy proxy = new Proxy(Robot.getParamsValue(params, 4, new HashMap<String, String>()));
		String verify = Robot.getParamsValue(params, 5, "False");
		Boolean debug = Boolean.parseBoolean(Robot.getParamsValue(params, 6, "False"));
		RobotLogger.setDebugToAll(debug);
		Authentication auth = Authentication
				.getAuthentication(Robot.getParamsValue(params, 2, (List<String>) new ArrayList<String>()));
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
	@ArgumentNames({ "alias", "url", "headers={}", "cookies=None", "auth=None", "timeout=None", "proxy=None",
			"verify=False", "debug=False" })
	public void createDigestSession(String alias, String url, String... params) {
		RestClient rc = new RestClient();
		Map<String, String> headers = Robot.getParamsValue(params, 0, new HashMap<String, String>());
		Proxy proxy = new Proxy(Robot.getParamsValue(params, 4, new HashMap<String, String>()));
		String verify = Robot.getParamsValue(params, 5, "False");
		Boolean debug = Boolean.parseBoolean(Robot.getParamsValue(params, 6, "False"));
		RobotLogger.setDebugToAll(debug);
		Authentication auth = Authentication.getAuthentication(
				Robot.getParamsValue(params, 2, (List<String>) new ArrayList<String>()), Authentication.Type.DIGEST);
		rc.createSession(alias, url, headers, auth, verify, debug, proxy);
	}

}
