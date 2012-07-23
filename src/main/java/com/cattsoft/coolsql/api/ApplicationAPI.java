/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-6-4
 */
package com.cattsoft.coolsql.api;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author Kenny Liu
 *
 * 2009-6-4 create
 */
public class ApplicationAPI {

	public static IApplication getApplication() {
		return getEntry().application;
	}
	
	private static ApplicationAPI apiEntry;
	
	private static ApplicationAPI getEntry() {
		if (apiEntry == null) {
			apiEntry = new ApplicationAPI();
		}
		
		return apiEntry;
	}
	
	private IApplication application;
	
	private ApplicationAPI() {
		JMenuBar menuBar = GUIUtil.getMainFrame().getJMenuBar();
		JMenu helpMenu = menuBar.getMenu(6);
		
		ApplicationMenuBarAPI menuBarAPI = new ApplicationMenuBarAPI();
		menuBarAPI.setHelpMenu(helpMenu);
		menuBarAPI.setMenuBar(menuBar);
		
		application = new Application();
		((Application)application).setMenuBarApi(menuBarAPI);
	}
}
