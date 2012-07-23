package com.cattsoft.coolsql.view.sqleditor;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.view.ViewManage;
/**
 * ��ʽ��������
 * 
 * @author kenny liu
 * 
 * 2007-12-5 create
 */
public class FormatSQLAction extends CsAction {
	private static final long serialVersionUID = 1L;

	public FormatSQLAction() {
		initMenuDefinitionById("FormatSQLAction");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		ViewManage.getInstance().getSqlEditor().getEditorPane().reformatSql();
	}

}