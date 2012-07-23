package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.ShortcutEditor;

/**
 * Edit shortcut 
 * @author ��Т��(kenny liu)
 *
 * 2008-4-16 create
 */

public class EditShortcutsAction
	extends CsAction
{
	private static final long serialVersionUID = 1L;

	public EditShortcutsAction()
	{
		super();
		initMenuDefinitionById("EditShortcutsAction");
	}

	public void executeAction(ActionEvent e)
	{
		ShortcutEditor editor = new ShortcutEditor(GUIUtil.getMainFrame());
		editor.showWindow();
	}
}