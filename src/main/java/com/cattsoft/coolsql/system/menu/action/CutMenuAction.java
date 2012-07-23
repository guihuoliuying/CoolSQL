/*
 * Created on 2007-5-25
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *sql�༭����ļ��в˵�������
 */
public class CutMenuAction extends BaseEditorSelectAction {

	private static final long serialVersionUID = 1L;
	
	public CutMenuAction()
	{
		super();
		initMenuDefinitionById("CutMenuAction");
	}

	/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        ViewManage.getInstance().getSqlEditor().getEditorPane().cut();
        ViewManage.getInstance().getSqlEditor().getEditorPane().requestFocusInWindow();
    }

}
