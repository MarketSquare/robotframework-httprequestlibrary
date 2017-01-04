package com.github.hi_fi.httprequestlibrary.domain;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class Session {
	private String alias;
	private String url;
	private HttpContext context;
	private HttpClient client;
	private HttpResponse response;
	private String responseBody;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public HttpContext getContext() {
		return context;
	}

	public void setContext(HttpContext context) {
		this.context = context;
	}

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
		try {
			this.setResponseBody(EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (ParseException e) {
			throw new RuntimeException("Parsing exception. Message: "+e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("IO exception. Message: "+e.getMessage());
		}
	}

	public String getResponseBody() {
		return responseBody;
	}

	private void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		if (url.endsWith("/")) {
			this.url = url.substring(0, url.length()-1);
		}
	}
}
