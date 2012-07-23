/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;

/**
 * @author kenny liu
 *
 * 2007-12-6 create
 */
public class ShowSqlEditorLineNumberAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public ShowSqlEditorLineNumberAction()
	{
		initMenuDefinitionById("ShowSqlEditorLineNumberAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		JCheckBoxMenuItem item=(JCheckBoxMenuItem)e.getSource();
		Setting.getInstance().setBooleanProperty(PropertyConstant.PROPERTY_SHOW_LINE_NUMBERS, item.isSelected());

	}

}
