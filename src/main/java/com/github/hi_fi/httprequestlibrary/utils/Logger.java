package com.github.hi_fi.httprequestlibrary.utils;

public class Logger {
	
	public static void logHTML(Object log) {
		System.out.println("*HTML* "+log);
	}
	
	public static void logError(Object log) {
		System.out.println("*ERROR* "+log);
	}
	
	public static void logDebug(Object log) {
		System.out.println("*DEBUG* "+log);
	}
	
	public static void logTrace(Object log) {
		System.out.println("*TRACE* "+log);
	}
	
	public static void log(Object log) {
		System.out.println("*INFO* "+log);
	}
}
