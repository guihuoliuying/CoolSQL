/**
 * 
 */
package com.cattsoft.coolsql.system.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;

/**
 * global setting for system
 * @author ��Т��(kenny liu)
 *
 * 2008-1-22 create
 */
public class GlobalSettingAction extends CsAction {

	private static final long serialVersionUID = 1L;
	
	public GlobalSettingAction()
	{
		super();
		initMenuDefinitionById("GlobalSettingAction");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void executeAction(ActionEvent e) {
		new GlobalSettingCommand(null).exectue();

	}

}
