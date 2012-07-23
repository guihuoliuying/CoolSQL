package com.cattsoft.coolsql.pub.display;

public interface PropertyStorage
{
	Object setProperty(String property, String value);
	Object setProperty(String property, int value);
	Object setProperty(String property, boolean value);
	boolean getBoolProperty(String property, boolean defaultValue);
	int getIntProperty(String property, int defaultValue);
	String getProperty(String property, String defaultValue);
}
