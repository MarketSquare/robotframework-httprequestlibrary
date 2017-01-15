package com.github.hi_fi.httprequestlibrary.utils;

import java.io.Serializable;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;


public class RobotLogger implements Log, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3269522167427538736L;

	public static void logHTML(Object log) {
		System.out.println("*HTML* "+log);
	}
	
	public void error(Object log) {
		System.out.println("*ERROR* "+log);
	}
	
	public void debug(Object log) {
		System.out.println("*DEBUG* "+log);
	}
	
	public void trace(Object log) {
		System.out.println("*TRACE* "+log);
	}
	
	public void info(Object log) {
		System.out.println("*INFO* "+log);
	}

	public void debug(Object message, Throwable t) {
		System.out.println("*DEBUG* "+message);
		System.out.println("*DEBUG* "+ExceptionUtils.getStackTrace(t));
	}

	public void error(Object message, Throwable t) {
		System.out.println("*ERROR* "+message);
		System.out.println("*ERROR* "+ExceptionUtils.getStackTrace(t));
	}

	public void fatal(Object message) {
		this.error(message);
	}

	public void fatal(Object message, Throwable t) {
		this.error(message, t);
	}

	public void info(Object message, Throwable t) {
		System.out.println("*INFO* "+message);
		System.out.println("*INFO* "+ExceptionUtils.getStackTrace(t));
	}

	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isErrorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isFatalEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isInfoEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTraceEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWarnEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void trace(Object message, Throwable t) {
		System.out.println("*TRACE* "+message);
		System.out.println("*TRACE* "+ExceptionUtils.getStackTrace(t));
	}

	public void warn(Object message) {
		System.out.println("*WARN* "+message);
	}

	public void warn(Object message, Throwable t) {
		System.out.println("*WARN* "+message);
		System.out.println("*WARN* "+ExceptionUtils.getStackTrace(t));
		
	}
}
