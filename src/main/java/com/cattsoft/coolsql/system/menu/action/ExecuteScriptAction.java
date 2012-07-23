/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.commonoperator.SQLScriptExecuteOperator;
import com.cattsoft.coolsql.sql.execute.IMultiStatementExecute;
import com.cattsoft.coolsql.sql.execute.ScriptStatementExecute;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.view.SqlEditorView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.FileLogger;
import com.cattsoft.coolsql.view.log.ILogger;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-1 create
 */
public class ExecuteScriptAction extends CsAction {

	private static final long serialVersionUID = 1L;
	private IMultiStatementExecute executer;
	public ExecuteScriptAction()
	{
		initMenuDefinitionById("ExecuteScriptAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		String importDataPath=PropertyManage.getSystemProperty().getSelectFile_importData();
		File[] file=GUIUtil.selectFileNoFilter(GUIUtil.findLikelyOwnerWindow(),importDataPath,true,true,false);
		if(file==null||file.length==0)
		{
			return;
		}
		importDataPath=file[0].getParent();
		PropertyManage.getSystemProperty().setSelectFile_importData(importDataPath);
		
		ILogger log=new FileLogger(new File(importDataPath,"script.log"));
		executer=new ScriptStatementExecute(BookmarkManage.getInstance().getDefaultBookmark(),file);
		executer.setExecuteLogger(log);

		Operatable operator;
		try {
			operator=OperatorFactory.getOperator(SQLScriptExecuteOperator.class);
		} catch (Exception e1) {
			LogProxy.errorReport("create sqlOperator error!"+e1.getMessage(), e1);
			return;
		}
		try {
			operator.operate(executer);
		} catch (UnifyException e2) {
            LogProxy.errorReport(e2);
        } catch (SQLException e2) {
            LogProxy.SQLErrorReport(e2);
        }

	}

}
