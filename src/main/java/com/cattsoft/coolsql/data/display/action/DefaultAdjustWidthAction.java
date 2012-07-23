/*
 * Created on 2007-3-9
 */
package com.cattsoft.coolsql.data.display.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.cattsoft.coolsql.pub.display.BaseTable;

/**
 * The default action used to adjust the width of table columns.
 * 
 * @author Kenny liu
 */
public class DefaultAdjustWidthAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private BaseTable table;
	public DefaultAdjustWidthAction(BaseTable table) {
		this.table = table;
	}
	/*
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int[] selectColumns = table.getSelectedColumns();
		if (selectColumns == null || selectColumns.length == 0)
			return;

		table.columnsToFitWidth(selectColumns);

	}

}
