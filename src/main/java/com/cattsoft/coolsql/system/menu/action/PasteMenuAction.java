/*
 * Created on 2007-5-25
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *sql�༭�����ճ����
 */
public class PasteMenuAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public PasteMenuAction()
	{
		super();
		initMenuDefinitionById("PasteMenuAction");
	}
	/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        ViewManage.getInstance().getSqlEditor().getEditorPane().paste();
        ViewManage.getInstance().getSqlEditor().getEditorPane().requestFocusInWindow();
    }

}
