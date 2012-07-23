/**
 * 
 */
package com.cattsoft.coolsql.system.close;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author kenny liu
 *
 * 2007-10-30 create
 */
public class SaveSystemPropertyTask implements Task {

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#execute()
	 */
	public void execute() {
		try {
			PropertyManage.saveSystemProperty();
		} catch (XMLException e) {
			LogProxy.errorMessage(e.getMessage());
		}
		try
		{
			Setting.getInstance().saveSetting();
		}catch(Exception e)
		{
			LogProxy.errorMessage(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getDescribe()
	 */
	public String getDescribe() {
		return PublicResource.getString("system.closetask.savesystemproperties.describe");
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getTaskLength()
	 */
	public int getTaskLength() {
		return 2;
	}

}
