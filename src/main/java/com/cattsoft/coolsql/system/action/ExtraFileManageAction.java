/*
 * Created on 2007-5-16
 */
package com.cattsoft.coolsql.system.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.AutoCsAction;
import com.cattsoft.coolsql.gui.ExtraJarsManageDialog;

/**
 * @author liu_xlin
 *�˳�ϵͳ�������¼�����
 */
public class ExtraFileManageAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;
	
	public ExtraFileManageAction()
	{
		super();
	}
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	@Override
    public void executeAction(ActionEvent e) {
        new ExtraJarsManageDialog().setVisible(true);
    }

}
