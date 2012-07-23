/*
 * SettingFactory.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.system;

/**
 * This factory is used to create some setting object with the specified property file.
 * @author Kenny Liu
 *
 * 2008-9-13 create
 */
public class SettingFactory {

	/**
	 * Create a setting object which is constructed by loading the specified property file.
	 * @param propertyFile a property file.
	 * @return a setting object.
	 */
	public static ISetting createSettingFactory(String propertyFile) {
		return createSettingFactory(propertyFile, null);
	}
	/**
	 * Create a setting object which is constructed by loading the specified property file with enconding.
	 * @param propertyFile a property file.
	 * @return a setting object.
	 */
	public static ISetting createSettingFactory(String propertyFile, String encoding) {
		DefaultSetting setting = new DefaultSetting();
		setting.loadProperties(propertyFile, encoding);
		
		return setting;
	}
}
