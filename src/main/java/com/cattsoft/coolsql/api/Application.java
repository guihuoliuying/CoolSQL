/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-6-4
 */
package com.cattsoft.coolsql.api;

import javax.swing.JFrame;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;


/**
 * @author Kenny Liu
 *
 * 2009-6-4 create
 */
public class Application implements IApplication{

	private IApplicationMenuBarAPI menuBarApi;
	
	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplication#getApplicationMenuBarAPI()
	 */
	public IApplicationMenuBarAPI getApplicationMenuBarAPI() {
		return menuBarApi;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplication#getApplicationViewsAPI()
	 */
	public IApplicationViewsAPI getApplicationViewsAPI() {
		throw new UnsupportedOperationException("I'm sorry! This api hasn't been implemented.");
	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplication#getMainFrame()
	 */
	public JFrame getMainFrame() {
		return GUIUtil.getMainFrame();
	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplication#getVersoin()
	 */
	public String getVersoin() {
		return PublicResource.getUtilString("system.version.no");
	}

	/**
	 * @param menuBarApi the menuBarApi to set
	 */
	void setMenuBarApi(IApplicationMenuBarAPI menuBarApi) {
		this.menuBarApi = menuBarApi;
	}

}
