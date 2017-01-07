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
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		Logger.logDebug(String.format("Verify's class is %s, and value %s", verify.getClass(), verify));
		if (new File(verify).exists()) {
			Logger.logDebug("Loading custom keystore");
			httpClientBuilder
					.setSSLSocketFactory(this.allowAllCertificates(this.createCustomKeyStore(verify.toString())));
		} else if (!Boolean.parseBoolean(verify.toString())) {
			Logger.logDebug("Allowing all certificates");
			httpClientBuilder.setSSLSocketFactory(this.allowAllCertificates(null));
		}
		return httpClientBuilder.build();
	}

	private KeyStore createCustomKeyStore(String path) {
		KeyStore trustStore;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null);
			int i = 0;
			for (X509Certificate cert : this.getCertificatesFromFile(path)) {
				trustStore.setCertificateEntry("Custom_entry_" + i, cert);
				i++;
			}
			Logger.logDebug("Certificates in trustStore: "+(i));
			return trustStore;
		} catch (KeyStoreException e) {
			throw new RuntimeException(String.format("%s occurred. Error message: %s", e.getClass(), e.getMessage()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(String.format("%s occurred. Error message: %s", e.getClass(), e.getMessage()));
		} catch (CertificateException e) {
			throw new RuntimeException(String.format("%s occurred. Error message: %s", e.getClass(), e.getMessage()));
		} catch (IOException e) {
			throw new RuntimeException(String.format("%s occurred. Error message: %s", e.getClass(), e.getMessage()));
		}

	}

	private List<X509Certificate> getCertificatesFromFile(String path) {
		List<X509Certificate> certificateList = new ArrayList<X509Certificate>();
		try {
			String[] certificates = FileUtils.readFileToString(new File(path), "UTF-8")
					.split("-----BEGIN CERTIFICATE-----");
			certificates = Arrays.copyOfRange(certificates, 1, certificates.length);
			for (String certificate : certificates) {
				certificate = "-----BEGIN CERTIFICATE-----" + certificate.split("-----END CERTIFICATE-----")[0]
						+ "-----END CERTIFICATE-----";
				Logger.logTrace(certificate);
				certificateList.add(this.generateCertificateFromDER(certificate.getBytes()));
			}
			return certificateList;

		} catch (IOException e) {
			throw new RuntimeException("Couldn't read certificates. Error: " + e.getMessage());
		} catch (CertificateException e) {
			throw new RuntimeException("Certificate generation failed. Error: " + e.getMessage());
		}
	}

	private X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
		CertificateFactory factory = CertificateFactory.getInstance("X.509");

		return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
	}

	private SSLConnectionSocketFactory allowAllCertificates(KeyStore keyStore) {
		SSLContextBuilder sshbuilder = new SSLContextBuilder();
		TrustStrategy trustStrategy = new TrustSelfSignedStrategy();
		HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
		if (keyStore != null) {
			trustStrategy = null;
			hostnameVerifier = null;
		}
		try {
			sshbuilder.loadTrustMaterial(keyStore, trustStrategy);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(String.format("%s occurred. Error message: %s", e.getClass(), e.getMessage()));
		} catch (KeyStoreException e) {
			throw new RuntimeException(String.format("%s occurred. Error message: %s", e.getClass(), e.getMessage()));
		}
		try {
			return new SSLConnectionSocketFactory(sshbuilder.build(), hostnameVerifier);
		} catch (KeyManagementException e) {
			throw new RuntimeException(String.format("%s occurred. Error message: %s", e.getClass(), e.getMessage()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(String.format("%s occurred. Error message: %s", e.getClass(), e.getMessage()));
		}
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
