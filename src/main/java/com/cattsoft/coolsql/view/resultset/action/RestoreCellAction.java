/**
 * Create date:2008-5-18
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.table.TableColumnModel;

import com.cattsoft.coolsql.action.framework.AutoCsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-18 create
 */
public class RestoreCellAction extends AutoCsAction {

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(RestoreCellAction.class);
	private static final long serialVersionUID = 1L;

	@Override
	public void executeAction(ActionEvent e)
	{
		Component com=ViewManage.getInstance().getResultView().getDisplayComponent();
		if(com instanceof DataSetPanel)
		{
			final DataSetTable dsTable=(DataSetTable)((DataSetPanel)com).getContent();
			final int[] rows=dsTable.getSelectedRows();
			final int[] columns=dsTable.getSelectedColumns();
			if(rows.length>0)
			{
				boolean result=GUIUtil.getYesNo(GUIUtil.findLikelyOwnerWindow(),
						stringMgr.getString("resultset.action.instantmodify.restoreselectedcells.confirm",rows.length*columns.length));
				if(!result)
					return;
			}else
				return;
			GUIUtil.processOnSwingEventThread(new Runnable()
			{
				public void run() {
					TableColumnModel model=dsTable.getColumnModel();
					for(int i=0;i<rows.length;i++)
					{
						for(int j=0;j<columns.length;j++)
						{
							/** First give up edting*/
							if(dsTable.isCellEditable(rows[i], columns[j]))
							{
								model.getColumn(columns[j]).getCellEditor().cancelCellEditing();
							}
							dsTable.restoreCell(rows[i], columns[j]);
						}
					}
				}
				
			}
			);
			
		}
	}
}
