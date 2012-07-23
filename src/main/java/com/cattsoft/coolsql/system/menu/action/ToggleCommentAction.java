/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.sql.formater.TextCommenter;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;

/**
 * ��sql�༭���EditorPanel����ע��/����ע�͵Ĳ���������
 * 
 * @author kenny liu
 * 
 * 2007-12-5 create
 */
public class ToggleCommentAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public ToggleCommentAction() {
		initMenuDefinitionById("ToggleCommentAction");
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		EditorPanel pane = ViewManage.getInstance().getSqlEditor()
				.getEditorPane();
		TextCommenter tc = new TextCommenter(pane);
		tc.commentSelection();

	}

}
