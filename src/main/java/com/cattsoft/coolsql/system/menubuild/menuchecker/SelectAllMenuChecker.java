/*
 * Created on 2007-5-25
 */
package com.cattsoft.coolsql.system.menubuild.menuchecker;

import javax.swing.text.Document;

import com.cattsoft.coolsql.system.menubuild.MenuItemEnableCheck;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;

/**
 * @author liu_xlin the checker of selectAll menu'availability
 */
public class SelectAllMenuChecker implements MenuItemEnableCheck {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
	 */
	public boolean check() {
		if(!ViewManage.getInstance().getSqlEditor().getEditorPane().isFocusOwner())
    		return false;
		
		EditorPanel pane = ViewManage.getInstance().getSqlEditor().getEditorPane();
		Document doc = pane.getDocument();
		if (doc.getStartPosition().getOffset() == pane.getSelectionStart()
				&& (doc.getEndPosition().getOffset()-1) == pane.getSelectionEnd())
			return false;
		else
			return true;
	}

}
