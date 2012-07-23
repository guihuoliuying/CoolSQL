/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-4 create
 */
public class ShowSystemStatusBarAction extends CsAction {

	private static final long serialVersionUID = 1L;
	
	public ShowSystemStatusBarAction()
	{
		initMenuDefinitionById("ShowSystemStatusBarAction");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		Object ob = e.getSource();
		if (ob instanceof JCheckBoxMenuItem) {
			JCheckBoxMenuItem checkItem=(JCheckBoxMenuItem)ob;
			if(checkItem.isSelected())
			{
				GUIUtil.showStatusBarOfMainFrame();
			}else
			{
				GUIUtil.hideStatusBarOfMainFrame();
			}
		}

	}

}
