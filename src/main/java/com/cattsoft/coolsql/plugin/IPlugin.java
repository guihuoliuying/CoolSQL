/**
 * 
 */
package com.cattsoft.coolsql.plugin;

import java.io.File;

import com.cattsoft.coolsql.gui.property.PropertyFrame;

/**
 * @author Kenny liu
 *
 * 2008-1-10 create
 */
public interface IPlugin {

	/**
	 * This method will be Called before application started up.
	 */
	void load() throws PluginException;

	/**
	 * This method will be Called after application started.
	 */
	void initialize() throws PluginException;

	/**
	 * This method will be Called when application shuts down.
	 */
	void unload(); 

	/**
	 * Return the name of plugin that which is unique in the application scope, you can 
	 * use the return value to identify the plugin object.
	 */
	String getInternalName();

	/**
	 * Return the descriptive name for this plugin.
	 */
	String getDescriptiveName();

	/**
	 * Return the author's name.
	 */
	String getAuthor();

	/**
	 * Return a comma separated list of other contributors.
	 *
	 * @return	Contributors names.
	 */
	String getContributors();

	/**
	 * Return the home page for this plugin.
	 *
	 */
	String getWebSite();

	/**
	 * Return the current version of plugin.
	 *
	 */
	String getVersion();
	
	/**
	 *This method will be called when global setting frame is initializing.
	 * @param pf --global setting frame
	 */
	void buildSettingPanel(PropertyFrame pf);
	
	/**
	 * If plugin need manage some properties needed by it, you can implement this method.
	 * @return may by null.
	 */
	Resource getResource();
	
	/**
	 *To get the folder that contains the files recording the setting of plugin.
	 */
	File getPluginSettingFolder();
}
