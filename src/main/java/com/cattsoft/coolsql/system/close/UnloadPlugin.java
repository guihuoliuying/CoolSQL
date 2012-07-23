/*
 * UnloadPlugin.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.system.close;

import com.cattsoft.coolsql.plugin.PluginManage;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.Task;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-9-13 create
 */
public class UnloadPlugin implements Task {

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#execute()
	 */
	public void execute() {
		PluginManage.getInstance().unloadPlugins();
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getDescribe()
	 */
	public String getDescribe() {
		return PublicResource.getString("system.closetask.unloadplugin.describe");
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getTaskLength()
	 */
	public int getTaskLength() {
		return 1;
	}

}
