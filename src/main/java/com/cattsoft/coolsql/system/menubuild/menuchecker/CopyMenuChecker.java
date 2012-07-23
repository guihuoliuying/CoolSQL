/*
 * Created on 2007-5-18
 */
package com.cattsoft.coolsql.system.menubuild.menuchecker;

import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *the checker of copy menu'availability
 */
public class CopyMenuChecker extends BaseSqlEditorMenuItemChecker {

    /* (non-Javadoc)
     * @see com.coolsql.system.menubar.MenuItemEnableCheck#check()
     */
    public boolean check() {
    	if(!super.check())
    		return false;
        String text=ViewManage.getInstance().getSqlEditor().getEditorPane().getSelectedText();
        if(text==null||text.equals(""))
            return false;
        else
            return true;
    }

}
