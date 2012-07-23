/**
 * 
 */
package com.cattsoft.coolsql.view.sqleditor.action;

import java.sql.SQLException;
import java.util.List;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.commonoperator.SQLScriptExecuteOperator;
import com.cattsoft.coolsql.sql.execute.IMultiStatementExecute;
import com.cattsoft.coolsql.sql.execute.MultiStatementExecute;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * execute sqls which are considered as script.
 * @author ��Т��(kenny liu)
 *
 * 2008-4-1 create
 */
public class MultiStatementExecuteCommand implements ActionCommand {

	private IMultiStatementExecute executer;
	public MultiStatementExecuteCommand(Bookmark bookmark,List<String> sqls)
	{
		executer=new MultiStatementExecute(bookmark,sqls);
	}
	/* (non-Javadoc)
	 * @see com.coolsql.action.common.ActionCommand#exectue()
	 */
	public void exectue() throws Exception {
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
