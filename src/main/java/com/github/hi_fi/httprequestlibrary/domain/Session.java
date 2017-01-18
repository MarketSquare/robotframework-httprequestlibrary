package com.github.hi_fi.httprequestlibrary.domain;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;

public class Session {
	private String alias;
	private String url;
	private HttpClientContext context;
	private HttpClient client;
	private ResponseData responseData = new ResponseData();
	private HttpResponse response;
	private Authentication authentication;
	private HttpHost httpHost;
	private String verify;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public HttpClientContext getContext() {
		return context;
	}

	public void setContext(HttpClientContext context) {
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
			this.responseData.setStatusCode(response.getStatusLine().getStatusCode());
		} catch (ParseException e) {
			throw new RuntimeException("Parsing exception. Message: "+e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("IO exception. Message: "+e.getMessage());
		}
	}

	public String getResponseBody() {
		return responseData.getText();
	}

	private void setResponseBody(String responseBody) {
		this.responseData.setText(responseBody);
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

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public HttpHost getHttpHost() {
		return httpHost;
	}

	public void setHttpHost(HttpHost httpHost) {
		this.httpHost = httpHost;
	}
	
	public ResponseData getResponseData() {
		return this.responseData;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}
}
