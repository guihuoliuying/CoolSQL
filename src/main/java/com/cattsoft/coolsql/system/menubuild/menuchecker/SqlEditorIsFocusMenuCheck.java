/**
 * 
 */
package com.cattsoft.coolsql.system.menubuild.menuchecker;

import com.cattsoft.coolsql.system.menubuild.MenuItemEnableCheck;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * used to check whether sql editor has focus.
 * @author ��Т��
 *
 * 2008-1-7 create
 */
public class SqlEditorIsFocusMenuCheck implements MenuItemEnableCheck {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
	 */
	public boolean check() {
		return ViewManage.getInstance().getSqlEditor().getEditorPane().isFocusOwner();
	}


}
