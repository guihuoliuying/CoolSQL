/**
 * Create date:2008-5-18
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.AutoCsAction;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-18 create
 */
public class ShowColumnSelectDialogAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeAction(ActionEvent e)
	{
		Component component=ViewManage.getInstance().getResultView().getDisplayComponent();
    	if(component instanceof DataSetPanel)
    	{
    		DataSetPanel dsPanel=(DataSetPanel)component;
    		dsPanel.popupColumnsSelectionDialog();
    	}
	}
}
