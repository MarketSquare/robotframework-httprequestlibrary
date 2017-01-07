package com.github.hi_fi.httprequestlibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;

import com.github.hi_fi.httprequestlibrary.domain.Session;

public class RestClient {

	private static Map<String, Session> sessions = new HashMap<String, Session>();

	public Session getSession(String alias) {
		return sessions.get(alias);
	}

	public void createSession(String alias, String url, String verify) {
		Session session = new Session();
		session.setContext(this.createContext());
		session.setClient(this.createHttpClient(verify));
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

	private HttpContext createContext() {
		HttpContext httpContext = new BasicHttpContext();
		CookieStore cookieStore = new BasicCookieStore();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		return httpContext;
	}

	private HttpClient createHttpClient(String verify) {
		Security security = new Security();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		Logger.logDebug(String.format("Verify's class is %s, and value %s", verify.getClass(), verify));
		if (new File(verify).exists()) {
			Logger.logDebug("Loading custom keystore");
			httpClientBuilder
					.setSSLSocketFactory(security.allowAllCertificates(security.createCustomKeyStore(verify.toString())));
		} else if (!Boolean.parseBoolean(verify.toString())) {
			Logger.logDebug("Allowing all certificates");
			httpClientBuilder.setSSLSocketFactory(security.allowAllCertificates(null));
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
