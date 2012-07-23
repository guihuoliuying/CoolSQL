package com.cattsoft.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author (kenny liu) 2009-4-4
 */
public class DeleteRowsAction extends CsAction {

	private static final StringManager stringMgr = StringManagerFactory.getStringManager(DeleteRowsAction.class);
	
	private static final long serialVersionUID = 1L;

	public DeleteRowsAction() {
		super();
		initMenuDefinitionById("DeleteRowsAction");
	}
	@Override
	public void executeAction(ActionEvent e) {
		JComponent component = ViewManage.getInstance().getResultView()
				.getDisplayComponent();
		if (!(component instanceof DataSetPanel))
			return;
		DataSetPanel dsp = (DataSetPanel) component;
		
		if ((dsp.getKeyList() == null || dsp.getKeyList().size() == 0) && dsp.isFirstTime()) {
			boolean result = GUIUtil
			.getYesNo(
					GUIUtil.findLikelyOwnerWindow(),
					stringMgr
							.getString("resultset.action.deleteRow.confirmkeylist"));
			if (result) {
				dsp.popupColumnsSelectionDialog();
			}
		}
		
		JComponent content = dsp.getContent();
		if (!(content instanceof DataSetTable)) {
			return;
		}
		DataSetTable table = (DataSetTable) content;
		int[] selectedRows = table.getSelectedRows();

		if (selectedRows == null || selectedRows.length == 0)
			return;
		
		table.markDeletedRow(selectedRows);
	}
}
