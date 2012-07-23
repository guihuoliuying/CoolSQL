/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.favorite.FavoriteManage;

/**
 * @author kenny liu
 *
 * 2007-11-5 create
 */
public class CollectSQLAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public CollectSQLAction()
	{
		initMenuDefinitionById("CollectSQLAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		
		boolean isOk = false;
		while (!isOk) {
			String result=JOptionPane.showInputDialog(GUIUtil.getMainFrame(),PublicResource.getSystemString("system.menu.action.collectsql"));
		
			if (result != null && !result.trim().equals("")) {
				isOk=true;
				FavoriteManage.getInstance().addSQL(result);
			}else
			{
				if(result==null)
					isOk=true;		
			}
		}

	}

}
