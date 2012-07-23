/*
 * �������� 2006-7-7
 *
 */
package com.cattsoft.coolsql.action.sqleditormenu;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 * 
 */
public class AutoSelectAction extends CsAction {
	private static final long serialVersionUID = 1L;
	public AutoSelectAction() {
		super();
		this.initMenuDefinitionById("autoselectAction");
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void executeAction(ActionEvent e) {
		ViewManage.getInstance().getSqlEditor().autoSelect();
	}
}
