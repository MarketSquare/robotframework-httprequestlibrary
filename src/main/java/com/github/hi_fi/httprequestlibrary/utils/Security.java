package com.github.hi_fi.httprequestlibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;

public class Security {
	
	protected AuthCache getAuthCache(HttpHost target) {
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
        authCache.put(target, basicAuth);
        return authCache;
	}
	
	protected CredentialsProvider getCredentialsProvider(String username, String password, HttpHost target) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials(username, password));
        return credsProvider;
	}
	
	protected KeyStore createCustomKeyStore(String path) {
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

	protected SSLConnectionSocketFactory allowAllCertificates(KeyStore keyStore) {
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

}
