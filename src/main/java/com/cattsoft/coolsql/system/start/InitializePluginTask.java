/**
 * 
 */
package com.cattsoft.coolsql.system.start;

import com.cattsoft.coolsql.api.ApplicationAPI;
import com.cattsoft.coolsql.plugin.PluginManage;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * Initialize plugins.
 * @author (kenny liu)
 * 
 *         2008-1-12 create
 */
public class InitializePluginTask implements Task {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.Task#execute()
	 */
	public void execute() {
		//The API object should be initialized before initializing plugins.
		ApplicationAPI.getApplication();
		
		try {
			PluginManage.getInstance().initializePlugins();
		} catch (Exception e) {
			LogProxy.errorReport(
					"Initializing plugin failed:" + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.Task#getDescribe()
	 */
	public String getDescribe() {
		return PublicResource.getString("system.launch.initplugininfo");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.Task#getTaskLength()
	 */
	public int getTaskLength() {
		return 1;
	}

}
