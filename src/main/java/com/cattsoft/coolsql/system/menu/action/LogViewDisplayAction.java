/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author ��Т��
 *
 * 2008-1-6 create
 */
public class LogViewDisplayAction extends CsAction{

	private static final long serialVersionUID = 1L;

	public LogViewDisplayAction()
	{
		initMenuDefinitionById("LogViewDisplayAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		Object ob=e.getSource();
		if(ob instanceof JCheckBoxMenuItem)
		{
			JCheckBoxMenuItem item=(JCheckBoxMenuItem)ob;
			if(item.isSelected())
			{
				View view=ViewManage.getInstance().getLogView();
//				GUIUtil.controlSplit(view,false);
				if(!view.isViewVisible())
					view.showPanel(false);

			}else
			{
				View view=ViewManage.getInstance().getLogView();
				
				if(view.isViewVisible())
					view.hidePanel(false);
			}
			Setting.getInstance().setBooleanProperty(
					PropertyConstant.PROPERTY_VIEW_LOG_ISDISPLAY, item.isSelected());
		}

	}

}
