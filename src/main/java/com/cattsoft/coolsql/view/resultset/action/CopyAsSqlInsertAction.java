/**
 * Create date:2008-4-29
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JComponent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.DataTransToSQL;
import com.cattsoft.coolsql.sql.SQLResultSetResults;
import com.cattsoft.coolsql.sql.SQLStandardResultSetResults;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.ColumnImpl;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-29 create
 */
public class CopyAsSqlInsertAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public CopyAsSqlInsertAction()
	{
		super();
		initMenuDefinitionById("CopyAsSqlInsertAction");
	}
	@Override
	public void executeAction(ActionEvent e)
	{
		JComponent component=ViewManage.getInstance().getResultView().getDisplayComponent();
		if(!(component instanceof DataSetPanel))
			return;
		DataSetPanel dsp=(DataSetPanel)component;
		JComponent content=dsp.getContent();
		if(!(content instanceof DataSetTable))
		{
			return;
		}
		DataSetTable table=(DataSetTable)content;
		int[] selectedRows=table.getSelectedRows();
		
		if(selectedRows==null||selectedRows.length==0)
			return;
		
		if(!(dsp.getSqlResult() instanceof SQLStandardResultSetResults))
			return ;
		SQLStandardResultSetResults results=(SQLStandardResultSetResults)dsp.getSqlResult();
		Entity[] entities=results.getEntities();
		if(entities.length!=1)
			return;
		
		Column[] columnInfo=new Column[table.getColumnCount()];
		for(int i=0;i<table.getColumnCount();i++)
		{
			Object ob=table.getColumnModel().getColumn(i).getHeaderValue();
			if(ob instanceof SQLResultSetResults.Column)
			{
				SQLResultSetResults.Column sc=(SQLResultSetResults.Column)ob;
				ColumnImpl column=new ColumnImpl();
				column.setName(sc.getName());
				column.setType(sc.getSqlType());
				column.setTypeName(sc.getType());
				
				columnInfo[i]=column;
			}
		}
		DataTransToSQL dtts=new DataTransToSQL(table,entities[0],columnInfo);
		String str;
		try {
			str=dtts.createInsertSQLS(true);
		} catch (UnifyException e1) {
			LogProxy.errorReport(e1);
			return;
		} catch (SQLException e1) {
			LogProxy.SQLErrorReport(e1.getMessage(),e1);
			return;
		}
		Clipboard clp = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection sel = new StringSelection(str);
		clp.setContents(sel, sel);
	}
}
