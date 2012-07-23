/**
 * 
 */
package com.cattsoft.coolsql.system.menubuild.menuchecker;

import com.cattsoft.coolsql.system.menubuild.MenuItemEnableCheck;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-2 create
 */
public class BaseSqlEditorMenuItemChecker implements MenuItemEnableCheck {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
	 */
	public boolean check() {
		if(!ViewManage.getInstance().getSqlEditor().getEditorPane().isFocusOwner())
    		return false;
		else
			return true;
	}

}
