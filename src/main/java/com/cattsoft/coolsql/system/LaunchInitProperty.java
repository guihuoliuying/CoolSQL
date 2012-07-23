/*
 * LaunchInitProperty.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.Properties;

/**
 * @author ��Т��(kenny liu)
 * 
 * 2008-6-2 create
 */
public class LaunchInitProperty implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String LAUNCH_LANGUAGE="language";
	
	private String language;

	LaunchInitProperty() {
		readInitProperty();
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	private void readInitProperty() {
		Properties pros=getLaunchParameter();
		String lan=pros.getProperty(LAUNCH_LANGUAGE);
		if(lan==null)
		{
			language=Locale.getDefault().getLanguage();
		}else
		{
			language=lan;
		}
	}
	private Properties getLaunchParameter() {
		Properties launchParams = new Properties();
		File launchFile = new File(SystemConstant.LAUNCH_INI);
		if (launchFile.exists()) {
			try {
				launchParams.load(launchFile.toURL().openStream());
			} catch (Exception e) {

			}
		}
		return launchParams;
	}
	void saveToFile()
	{
		Properties launchParams=new Properties();
		
		/**
		 * Put all launch parameters into propeties object.
		 */
		launchParams.put(LAUNCH_LANGUAGE, getLanguage());
		
		try {
			launchParams.store(new FileOutputStream(new File(SystemConstant.LAUNCH_INI)), "");
		} catch (Exception e) {
			
		}
	}
}
