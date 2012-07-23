/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-1 create
 */
public class UpperSelectedTextAction extends BaseEditorSelectAction {
	
	private static final long serialVersionUID = 1L;
	public UpperSelectedTextAction()
	{
		super();
		initMenuDefinitionById("UpperSelectedTextAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		EditorPanel editor=ViewManage.getInstance().getSqlEditor().getEditorPane();
		editor.toUpperCase();
	}

}
