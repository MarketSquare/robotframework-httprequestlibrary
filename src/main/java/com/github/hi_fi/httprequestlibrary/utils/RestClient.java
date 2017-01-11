package com.github.hi_fi.httprequestlibrary.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;

import com.github.hi_fi.httprequestlibrary.domain.Authentication;
import com.github.hi_fi.httprequestlibrary.domain.Session;

public class RestClient {

	RobotLogger logger = new RobotLogger();
	private static Map<String, Session> sessions = new HashMap<String, Session>();

	public Session getSession(String alias) {
		return sessions.get(alias);
	}

	public void createSession(String alias, String url, Authentication auth, String verify) {
		HttpHost target;
		try {
			target = URIUtils.extractHost(new URI(url));
		} catch (URISyntaxException e) {
			throw new RuntimeException("Parsing of URL failed. Error message: "+e.getMessage());
		}
		Session session = new Session();
		session.setContext(this.createContext(auth, target));
		session.setClient(this.createHttpClient(auth, verify, target));
		session.setUrl(url);
		sessions.put(alias, session);
	}

	public void makeGetRequest(String alias, String uri, Map<String, String> parameters) {
		HttpGet getRequest = new HttpGet(this.buildUrl(alias, uri, parameters));
		Session session = this.getSession(alias);
		this.makeRequest(getRequest, session);
	}

	private void makeRequest(HttpUriRequest request, Session session) {
		try {
			session.setResponse(session.getClient().execute(request, session.getContext()));
		} catch (ClientProtocolException e) {
			throw new RuntimeException("Client protocol Exception. Message: " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("IO exception. Message: " + e.getMessage());
		}
	}

	private HttpClientContext createContext(Authentication auth, HttpHost target) {
		HttpClientContext httpClientContext = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		httpClientContext.setCookieStore(cookieStore);
		if (auth.isAuthenticable()) {
			httpClientContext.setAuthCache(new Security().getAuthCache(auth, target));
		}
		return httpClientContext;
	}

	private HttpClient createHttpClient(Authentication auth, String verify, HttpHost target) {
		Security security = new Security();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		logger.debug("Verify value: " + verify);
		logger.debug((new File(verify).getAbsolutePath()));

		if (new File(verify).exists()) {
			logger.debug("Loading custom keystore");
			httpClientBuilder.setSSLSocketFactory(
					security.allowAllCertificates(security.createCustomKeyStore(verify.toString())));
		} else if (!Boolean.parseBoolean(verify.toString())) {
			logger.debug("Allowing all certificates");
			httpClientBuilder.setSSLSocketFactory(security.allowAllCertificates(null));
		}
		
		if (auth.isAuthenticable()) {
			httpClientBuilder.setDefaultCredentialsProvider(security.getCredentialsProvider(auth, target));
		}
		return httpClientBuilder.build();
	}

	private String buildUrl(String alias, String uri, Map<String, String> parameters) {
		String url = this.getSession(alias).getUrl();
		if (uri.length() > 0) {
			String separator = uri.startsWith("/") ? "" : "/";
			url = url + separator + uri;
		}
		String parameterString = "";
		for (String key : parameters.keySet()) {
			try {
				parameterString += key + "=" + URLEncoder.encode(parameters.get(key), "UTF-8") + "&";
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Unsupported encoding noticed. Error message: " + e.getMessage());
			}
		}
		url += parameterString.length() > 1 ? "?" + parameterString.substring(0, parameterString.length() - 1) : "";
		return url;
	}

}
