/*
 * DefaultSetting.java
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
import java.beans.PropertyChangeListener;
import java.io.IOException;

import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.lookandfeel.SystemLookAndFeel;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-9-13 create
 */
public class DefaultSetting implements ISetting {
	private ParamProperties props;
	
	private String fileName = null;
	
	private String encoding = null;
	public DefaultSetting() {
		
	}
	/**
	 * Load the properties from filename again, overlaying the old properties.
	 */
	public void reload() {
		loadProperties(this.fileName, this.encoding);
	}
	/**
	 * This method is invoked by the setting factory when setting object is constructed .
	 * @param encoding a encoding for propert file.
	 */
	void loadProperties(String fileName, String encoding) {
		if (props == null) {
			props = new ParamProperties();
		}
		this.fileName = fileName;
		this.encoding = encoding;
		if (this.fileName == null) {
			return;
		}
		try {
			props.loadTextFile(this.fileName, encoding);
		} catch (IOException e) {
		}
	}
	/**
	 * Save all properties into local file.
	 */
	public void saveSetting()
	{
		if (this.fileName == null) {
			return;
		}
		try
		{
			this.props.saveToFile(this.fileName);
		} catch (IOException e)
		{
			LogProxy.errorLog("Error saving Settings file '" + this.fileName + "'", e);
		}
		SystemLookAndFeel.getInstance().saveToFile();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#getBoolProperty(java.lang.String, boolean)
	 */
	public boolean getBoolProperty(String property, boolean defaultValue)
	{
		String sysValue = System.getProperty(property, null);
		if (sysValue != null)
		{
			return StringUtil.stringToBool(sysValue);
		}
		return this.props.getBoolProperty(property, defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#getColorProperty(java.lang.String, java.awt.Color)
	 */
	public Color getColorProperty(String aColorKey, Color defaultColor)
	{
		String value = this.getProperty(aColorKey, null);
		if (value == null) return defaultColor;
		String[] colors = value.split(",");
		if (colors.length != 3) return defaultColor;
		
		return getColorByString(value, defaultColor);

	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#getFontProperty(java.lang.String, java.awt.Font)
	 */
	public Font getFontProperty(String aFontName, Font f)
	{
		String sf=this.props.getProperty(aFontName);
		if(sf==null)
			return f;
		return transString2Font(sf);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#getIntProperty(java.lang.String, int)
	 */
	public int getIntProperty(String aProperty, int defaultValue)
	{
		String sysValue = System.getProperty(aProperty, null);
		if (sysValue != null)
		{
			return StringUtil.getIntValue(sysValue, defaultValue);
		}
		return this.props.getIntProperty(aProperty, defaultValue);
	}
	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#getLongProperty(java.lang.String, long)
	 */
	public long getLongProperty(String aProperty, long defaultValue)
	{
		String sysValue = System.getProperty(aProperty, null);
		if (sysValue != null)
		{
			return StringUtil.getLongValue(sysValue, defaultValue);
		}
		return this.props.getLongProperty(aProperty, defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#getProperty(java.lang.String, java.lang.String)
	 */
	public String getProperty(String aProperty, String aDefault)
	{
		return System.getProperty(aProperty, this.props.getProperty(aProperty, aDefault));
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#setBooleanProperty(java.lang.String, boolean)
	 */
	public Boolean setBooleanProperty(String property, boolean value)
	{
		Object oldValue = this.props.setProperty(property, value);
		if (oldValue != null) {
			return new Boolean(oldValue.toString());
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#setColorProperty(java.lang.String, java.awt.Color)
	 */
	public Color setColorProperty(String key, Color c)
	{
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		String value = Integer.toString(r) + "," + Integer.toString(g) + "," + Integer.toString(b);
		Object oldValue = this.setProperty(key, value);
		if (oldValue == null) {
			return null;
		} else {
			return getColorByString(oldValue.toString(), c);
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#setFontProperty(java.lang.String, java.awt.Font)
	 */
	public Font setFontProperty(String propertyName,Font f)
	{
		String str=font2String(f);
		Object oldValue = setProperty(propertyName, str);
		if (oldValue == null) {
			return null;
		} else {
			return transString2Font(oldValue.toString());
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#setIntProperty(java.lang.String, int)
	 */
	public Integer setIntProperty(String aProperty, int aValue)
	{
		Object oldValue = this.props.setProperty(aProperty, Integer.toString(aValue));
		if (oldValue != null) {
			return new Integer(oldValue.toString());
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#setLongProperty(java.lang.String, long)
	 */
	public Long setLongProperty(String aProperty, long aValue)
	{
		Object oldValue = this.props.setProperty(aProperty, Long.toString(aValue));
		if (oldValue != null) {
			return new Long(oldValue.toString());
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.ISetting#setProperty(java.lang.String, java.lang.String)
	 */
	public Object setProperty(String property, String aValue)
	{
		return this.props.setProperty(property, aValue);
	}
	/**
	 * Transfer the string to a color object. The specified string must match the format: "red,green,blue".
	 * @param colorStr a string value
	 * @param defaultValue return this value if the specified string value is llegal.
	 * @return return a color object. It'll return a default color value if the specified string value is llegal.
	 */
	private Color getColorByString(String colorStr, Color defaultValue) {
		if (colorStr == null) {
			return defaultValue;
		} else {
			String[] colors = colorStr.split(",");
			if (colors.length != 3) {
				return defaultValue;
			}
			try
			{
				int r = StringUtil.getIntValue(colors[0]);
				int g = StringUtil.getIntValue(colors[1]);
				int b = StringUtil.getIntValue(colors[2]);
				return new Color(r,g,b);
			}
			catch (Exception e)
			{
				return defaultValue;
			}
		}
			
	}
	public synchronized void addPropertyChangeListener(PropertyChangeListener l,String property)
	{
		this.props.addPropertyChangeListener(l, property);
	}
	public synchronized void addPropertyChangeListener(PropertyChangeListener l,String[] properties)
	{
		this.props.addPropertyChangeListener(l, properties);
	}
	public void removePropertyChangeLister(PropertyChangeListener l)
	{
		this.props.removePropertyChangeListener(l);
	}
	private Font transString2Font(String str)
	{
		str=StringUtil.trim(str);
		if(str.equals(""))
			return null;
		String[] s=str.split(",");
		if(s.length!=3)
			return null;
		int style=Font.PLAIN;
		try
		{
			style=Integer.parseInt(s[1]);
		}catch(Exception e){}
		int size=12;
		try
		{
			size=Integer.parseInt(s[2]);
		}catch(Exception e)
		{}
		return new Font(s[0],style,size);
	}
	private String font2String(Font f)
	{
		if(f==null)
			return "";
		return f.getFamily()+","+f.getStyle()+","+f.getSize();
	}
}
