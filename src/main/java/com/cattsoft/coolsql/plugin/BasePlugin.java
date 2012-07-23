/**
 * 
 */
package com.cattsoft.coolsql.plugin;

import java.io.File;

import com.cattsoft.coolsql.gui.property.PropertyFrame;
import com.cattsoft.coolsql.system.SystemConstant;

/**
 * base class of all plugin
 * @author ��Т��(kenny liu)
 *
 * 2008-1-12 create
 */
public abstract class BasePlugin implements IPlugin {


	/* (non-Javadoc)
	 * @see com.coolsql.plugin.IPlugin#getContributors()
	 */
	public String getContributors() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.plugin.IPlugin#getVersion()
	 */
	public String getVersion() {
		return "1.0";
	}

	/* (non-Javadoc)
	 * @see com.coolsql.plugin.IPlugin#getWebSite()
	 */
	public String getWebSite() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.plugin.IPlugin#load()
	 */
	public void load() throws PluginException {

	}
	public void buildSettingPanel(PropertyFrame pf)
	{
		
	}
	public Resource getResource()
	{
		return null;
	}
	public File getPluginSettingFolder()
	{
		return new File(SystemConstant.PLUGIN_FOLDER);
	}
}
