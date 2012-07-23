/*
 * ISetting.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.system;

import java.awt.Color;
import java.awt.Font;

/**
 * The config setting interface.
 * @author ��Т��(kenny liu)
 *
 * 2008-9-13 create
 */
public interface ISetting {

	/**
	 * Save all properties into local file.
	 */
	public void saveSetting();
	/**
	 * Load the properties from filename again, overlaying the old properties.
	 */
	public void reload();
	/**
	 * Retrieve the string value from the setting according to the specified propert name : aProperty.
	 * The method will return the default value : aDefault if the property : aProperty doesn't exist in the setting.
	 * @param aProperty  property name.
	 * @param aDefault   default value.
	 * @return a string value, it'll return the default string value: aDefault if the specified property doesn't exist in the setting.
	 */
	public String getProperty(String aProperty, String aDefault);
	
	/**
	 * Retrieve the boolean value from the setting according to the specified propert name : aProperty.
	 * The method will return the default boolean value : aDefault if the property : aProperty doesn't 
	 * exist in the setting.
	 * @param property  property name.
	 * @param defaultValue default booelan value.
	 * @return a boolean value, it'll return the default boolean value: aDefault if the specified property 
	 * 			doesn't exist in the setting.
	 */
	public boolean getBoolProperty(String property, boolean defaultValue);
	
	/**
	 * Retrieve the color value from the setting according to the specified propert name : aProperty.
	 * The method will return the default color value : aDefault if the property : aProperty doesn't 
	 * exist in the setting.
	 * @param property  property name.
	 * @param defaultColor default color value.
	 * @return a boolean value, it'll return the default color value: aDefault if the specified property 
	 * 			doesn't exist in the setting.
	 */
	public Color getColorProperty(String aColorKey, Color defaultColor);
	/**
	 * Retrieve the int value from the setting according to the specified propert name : aProperty.
	 * The method will return the default int value : aDefault if the property : aProperty doesn't 
	 * exist in the setting.
	 * @param property  property name.
	 * @param defaultValue default int value.
	 * @return a int value, it'll return the default int value: aDefault if the specified property 
	 * 			doesn't exist in the setting.
	 */
	public int getIntProperty(String aProperty, int defaultValue);
	/**
	 * Retrieve the font value from the setting according to the specified propert name : aProperty.
	 * The method will return the default font value : aDefault if the property : aProperty doesn't 
	 * exist in the setting.
	 * @param property  property name.
	 * @param defaultValue default font value.
	 * @return a font value, it'll return the default font value: aDefault if the specified property 
	 * 			doesn't exist in the setting.
	 */
	public Font getFontProperty(String aFontName, Font f);
	/**
	 * Retrieve the long value from the setting according to the specified propert name : aProperty.
	 * The method will return the default long value : aDefault if the property : aProperty doesn't 
	 * exist in the setting.
	 * @param property  property name.
	 * @param defaultValue default long value.
	 * @return a long value, it'll return the default long value: aDefault if the specified property 
	 * 			doesn't exist in the setting.
	 */
	public long getLongProperty(String aProperty, long defaultValue);
	/**
	 * Set the string value for the specified property. And return the old string value. 
	 * @param property  property name.
	 * @param aValue property value .
	 * @return return the old value according to property. It'll return null if there is no value set in the setting. 
	 */
	public Object setProperty(String property, String aValue);
	/**
	 * Set the boolean value for the specified property. And return the old boolean value. 
	 * @param property  property name.
	 * @param aValue property value .
	 * @return return the old value according to property. It'll return null if there is no value set in the setting. 
	 */
	public Boolean setBooleanProperty(String property, boolean value);
	/**
	 * Set the int value for the specified property. And return the old int value. 
	 * @param property  property name.
	 * @param aValue property value .
	 * @return return the old value according to property. It'll return null if there is no value set in the setting. 
	 */
	public Integer setIntProperty(String aProperty, int aValue);
	/**
	 * Set the long value for the specified property. And return the old string value. 
	 * @param property  property name.
	 * @param aValue property value .
	 * @return return the old value according to property. It'll return null if there is no value set in the setting. 
	 */
	public Long setLongProperty(String aProperty, long aValue);
	/**
	 * Set the color value for the specified property. And return the old color value. 
	 * @param property  property name.
	 * @param aValue property value .
	 * @return return the old value according to property. It'll return null if there is no value set in the setting. 
	 */
	public Color setColorProperty(String aProperty, Color aValue);
	/**
	 * Set the font value for the specified property. And return the old font value. 
	 * @param property  property name.
	 * @param aValue property value .
	 * @return return the old value according to property. It'll return null if there is no value set in the setting. 
	 */
	public Font setFontProperty(String aProperty, Font aValue);
}
