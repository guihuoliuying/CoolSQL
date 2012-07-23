/**
 * CoolSQL 2009 copyright.
 * You should confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-4-3
 */
package com.cattsoft.coolsql.system.start;

import com.cattsoft.coolsql.plugin.PluginManage;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author Kenny Liu
 *
 * 2009-4-3 create
 */
public class LoadPluginTask implements Task{

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#execute()
	 */
	public void execute() {
		try {
			PluginManage.getInstance().loadAllPlugin();
		} catch (Exception e) {
			LogProxy.errorReport("load plugin error:"+e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getDescribe()
	 */
	public String getDescribe() {
		return PublicResource.getString("system.launch.loadplugininfo");
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getTaskLength()
	 */
	public int getTaskLength() {
		return 1;
	}

}
