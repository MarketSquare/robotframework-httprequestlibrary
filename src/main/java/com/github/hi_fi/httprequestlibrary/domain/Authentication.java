package com.github.hi_fi.httprequestlibrary.domain;

import java.util.List;

public class Authentication {
	
	private String username;
	private String password;
	private String domain;
	private Type type;
	private Boolean authenticable = false;
	private Boolean preemptiveAuthentication = false;
	
	public enum Type {
		BASIC, DIGEST, NTLM
	}
	
	public static Authentication getAuthentication(List<String> auth) {
		switch (auth.size()) {
		case 2:
			return new Authentication(auth.get(0), auth.get(1));
		}
		return new Authentication();
	}
	
	public static Authentication getAuthentication(List<String> auth, Type type) {
		switch (auth.size()) {
		case 2:
			return new Authentication(auth.get(0), auth.get(1), type);
		case 3:
			return new Authentication(auth.get(1), auth.get(2), auth.get(3), type);
		}
		return new Authentication();
	}
	
	private Authentication() {
		
	}
	
	private Authentication(String user, String pw) {
		this(user, pw, Type.BASIC);
	}
	
	private Authentication(String user, String pw, Type type) {
		this(user, pw, null, type);
	}
	
	private Authentication(String user, String pw, String domain, Type type) {
		this.username= user;
		this.password = pw;
		this.domain = domain;
		this.type = type;
		this.authenticable = true;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDomain() {
		return domain;
	}

	public Type getType() {
		return type;
	}

	public Boolean isAuthenticable() {
		return authenticable;
	}

	public Boolean usePreemptiveAuthentication() {
		return preemptiveAuthentication;
	}

}
