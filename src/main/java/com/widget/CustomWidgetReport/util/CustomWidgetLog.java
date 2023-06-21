package com.widget.CustomWidgetReport.util;

public class CustomWidgetLog {
	
	public static String getCurrentClassAndMethodName() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		final String s = e.getClassName();
		return s.substring(s.lastIndexOf('.') + 1, s.length()) + "." + e.getMethodName();
		}
}
