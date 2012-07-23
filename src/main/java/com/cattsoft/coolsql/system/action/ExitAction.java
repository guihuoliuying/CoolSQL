/*
 * Created on 2007-5-16
 */
package com.cattsoft.coolsql.system.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.main.frame.MainFrame;
import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author liu_xlin
 *�˳�ϵͳ�������¼�����
 */
public class ExitAction extends CsAction {

	private static final long serialVersionUID = 1L;
	
	public ExitAction()
	{
		super();
		initMenuDefinitionById("ExitAction");
	}
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	@Override
    public void executeAction(ActionEvent e) {
        ((MainFrame)GUIUtil.getMainFrame()).closeSystem();;
    }

}
