/*
 * Created on 2007-5-25
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *�����˵��¼�����,�ô������sql�༭���
 */
public class RedoMenuAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public RedoMenuAction()
	{
		super();
		initMenuDefinitionById("RedoMenuAction");
	}
	/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        ViewManage.getInstance().getSqlEditor().getEditorPane().redo();
        ViewManage.getInstance().getSqlEditor().getEditorPane().requestFocusInWindow();
    }

}
