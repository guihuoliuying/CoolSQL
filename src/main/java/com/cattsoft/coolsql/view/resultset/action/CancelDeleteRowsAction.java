package com.cattsoft.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author (kenny liu) 2009-4-4
 */
public class CancelDeleteRowsAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public CancelDeleteRowsAction() {
		super();
		initMenuDefinitionById("CancelDeleteRowsAction");
	}
	@Override
	public void executeAction(ActionEvent e) {
		JComponent component = ViewManage.getInstance().getResultView()
				.getDisplayComponent();
		if (!(component instanceof DataSetPanel))
			return;
		DataSetPanel dsp = (DataSetPanel) component;
		JComponent content = dsp.getContent();
		if (!(content instanceof DataSetTable)) {
			return;
		}
		DataSetTable table = (DataSetTable) content;
		int[] selectedRows = table.getSelectedRows();

		if (selectedRows == null || selectedRows.length == 0)
			return;

		table.cancelDeletedRow(selectedRows);
	}
}
