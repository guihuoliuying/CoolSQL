/**
 * 
 */
package com.cattsoft.coolsql.system.menu.loadlistener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menubuild.IMenuLoadListener;

/**
 * Check the setting state when loading menu reponsible for Showing/hiding
 * system status bar
 * 
 * @author ��Т��(kenny liu)
 * 
 * 2008-4-4 create
 */
public class ShowSystemStatusBarMenuLoadListener implements IMenuLoadListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action(javax.swing.JMenuItem)
	 */
	public void action(JMenuItem item) {
		if (!(item instanceof JCheckBoxMenuItem)) {
			return;
		}
		JCheckBoxMenuItem checkItem = (JCheckBoxMenuItem) item;
		checkItem.setSelected(Setting.getInstance().getBoolProperty(
				PropertyConstant.PROPERTY_SYSTEM_STATUSBAR_DISPLAY, true));

	}

}
