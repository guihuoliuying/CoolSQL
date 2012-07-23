/**
 * Create date:2008-5-17
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;

import com.cattsoft.coolsql.action.framework.CheckCsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-17 create
 */
public class ChangeStatusOfDataSetPanelAction extends CheckCsAction {

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(ChangeStatusOfDataSetPanelAction.class);
	
	private static final long serialVersionUID = 1L;

	public ChangeStatusOfDataSetPanelAction()
	{
	}
	@Override
	public void executeAction(ActionEvent e)
	{
		Object ob=e.getSource();
		if(!(ob instanceof JCheckBoxMenuItem)&&!(ob instanceof JCheckBox))
		{
			return;
		}
		JCheckBoxMenuItem checkItem=(JCheckBoxMenuItem)ob;
		Component com=ViewManage.getInstance().getResultView().getDisplayComponent();
		if(com instanceof DataSetPanel)
		{
			DataSetTable dsTable=(DataSetTable)((DataSetPanel)com).getContent();
			if(dsTable.hasModified()&&!checkItem.isSelected())
			{
				int result=JOptionPane.showConfirmDialog(GUIUtil.findLikelyOwnerWindow(),
						stringMgr.getString("resultset.action.instantmodify.quitmodifyconfirm"),
						"Status switch",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
				if(result==JOptionPane.NO_OPTION)
				{
					setSelected(true);//restore the selection value, because selection value has been modified before executing this method.
					return;
				}
			}
			((DataSetPanel)com).setAllowEdit(checkItem.isSelected());
		}
	}
}
