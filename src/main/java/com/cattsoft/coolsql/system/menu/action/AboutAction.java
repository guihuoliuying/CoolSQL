/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.gui.AboutDialog;
import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * ���ڲ˵���Ĵ�����
 * @author kenny liu
 *
 * 2007-12-6 create
 */
public class AboutAction extends CsAction {
	private static final long serialVersionUID = 1L;

	public AboutAction()
	{
		initMenuDefinitionById("AboutAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		AboutDialog about=new AboutDialog(GUIUtil.getMainFrame());
		about.setVisible(true);

	}

}
