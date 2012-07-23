/*
 * Created on 2007-5-25
 */
package com.cattsoft.coolsql.system.menubuild.menuchecker;

import com.cattsoft.coolsql.system.menubuild.MenuItemEnableCheck;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *the checker of redo menu'availability
 */
public class RedoMenuChecker implements MenuItemEnableCheck {

    /* (non-Javadoc)
     * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
     */
    public boolean check() {
    	if(!ViewManage.getInstance().getSqlEditor().getEditorPane().isFocusOwner())
    		return false;
        return ViewManage.getInstance().getSqlEditor().getEditorPane().getDocument().canRedo();
    }

}
