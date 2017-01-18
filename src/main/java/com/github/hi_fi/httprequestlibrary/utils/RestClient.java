package com.github.hi_fi.httprequestlibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;

import com.github.hi_fi.httprequestlibrary.domain.Authentication;
import com.github.hi_fi.httprequestlibrary.domain.Session;

public class RestClient {

	RobotLogger logger = new RobotLogger("RestClient");
	private static Map<String, Session> sessions = new HashMap<String, Session>();

	public Session getSession(String alias) {
		return sessions.get(alias);
	}

	public void createSession(String alias, String url, Map<String, String> headers, Authentication auth, String verify, Boolean debug) {
		if (debug) {
			System.setProperty("org.apache.commons.logging.Log",
					"com.github.hi_fi.httprequestlibrary.utils.RobotLogger");
			System.setProperty("org.apache.commons.logging.robotlogger.log.org.apache.http", "DEBUG");
		}
		HttpHost target;
		try {
			target = URIUtils.extractHost(new URI(url));
		} catch (URISyntaxException e) {
			throw new RuntimeException("Parsing of URL failed. Error message: " + e.getMessage());
		}
		Session session = new Session();
		session.setContext(this.createContext(auth, target));
		session.setClient(this.createHttpClient(auth, verify, target, false));
		session.setUrl(url);
		session.setHeaders(headers);
		session.setHttpHost(target);
		session.setVerify(verify);
		session.setAuthentication(auth);
		sessions.put(alias, session);
	}

	public void makeGetRequest(String alias, String uri, Map<String, String> headers, Map<String, String> parameters,
			boolean allowRedirects) {
		HttpGet getRequest = new HttpGet(this.buildUrl(alias, uri, parameters));
		getRequest = this.setHeaders(getRequest, headers);
		getRequest.setConfig(RequestConfig.custom().setRedirectsEnabled(allowRedirects).build());
		Session session = this.getSession(alias);
		this.makeRequest(getRequest, session);
	}

	public void makePostRequest(String alias, String uri, Object data, Map<String, String> parameters,
			Map<String, String> headers, Map<String, String> files, Boolean allowRedirects) {
		HttpPost postRequest = new HttpPost(this.buildUrl(alias, uri, parameters));
		postRequest = this.setHeaders(postRequest, headers);
		if (data.toString().length() > 0) {
			logger.debug(data);
			postRequest.setEntity(this.createDataEntity(data));
		}
		if (files.entrySet().size() > 0) {
			logger.debug(files);
			postRequest.setEntity(this.createFileEntity(files));
		}
		if (allowRedirects) {
			Session session = this.getSession(alias);
			session.setClient(this.createHttpClient(session.getAuthentication(), session.getVerify(),
					session.getHttpHost(), true));
		}
		Session session = this.getSession(alias);
		this.makeRequest(postRequest, session);
	}

	@SuppressWarnings("unchecked")
	private HttpEntity createDataEntity(Object data) {
		try {
			if (data instanceof Map) {
				List<NameValuePair> params = new ArrayList<NameValuePair>(0);
				for (Entry<String, Object> entry : ((Map<String, Object>) data).entrySet()) {
					params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
				}
				return new UrlEncodedFormEntity(params, "UTF-8");
			} else {
				return new StringEntity(data.toString());
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding noticed. Error message: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private HttpEntity createFileEntity(Object files) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for (Entry<String, Object> entry : ((Map<String, Object>) files).entrySet()) {
			if (new File(entry.getValue().toString()).exists()) {
				builder.addPart(entry.getKey(),
						new FileBody(new File(entry.getValue().toString()), ContentType.DEFAULT_BINARY));
			} else {
				builder.addPart(entry.getKey(),
						new StringBody(entry.getValue().toString(), ContentType.DEFAULT_TEXT));
			}
		}
		return builder.build();
	}

	private void makeRequest(HttpUriRequest request, Session session) {
		request = this.setHeaders(request, session.getHeaders());
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
		if (auth.usePreemptiveAuthentication()) {
			httpClientContext.setAuthCache(new Security().getAuthCache(auth, target));
		}
		return httpClientContext;
	}

	private HttpClient createHttpClient(Authentication auth, String verify, HttpHost target, Boolean postRedirects) {
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

		if (postRedirects) {
			httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
		}
		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build();
		httpClientBuilder.setDefaultRequestConfig(requestConfig);

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

	private <T> T setHeaders(T request, Map<String, String> headers) {
		for (Entry<String, String> entry : headers.entrySet()) {
			((HttpRequest) request).setHeader(entry.getKey(), entry.getValue());
		}
		return request;
	}

}
