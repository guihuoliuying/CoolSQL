/*
 * Created on 2007-5-25
 */
package com.cattsoft.coolsql.system.menubuild.menuchecker;

import com.cattsoft.coolsql.system.menubuild.MenuItemEnableCheck;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *the checker of cut menu'availability
 */
public class CutMenuChecker implements MenuItemEnableCheck {

    /* (non-Javadoc)
     * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
     */
    public boolean check() {
    	if(!ViewManage.getInstance().getSqlEditor().getEditorPane().isFocusOwner())
    		return false;
        String text=ViewManage.getInstance().getSqlEditor().getEditorPane().getSelectedText();
        if(text==null||text.equals(""))
            return false;
        else
            return true;
    }

}
