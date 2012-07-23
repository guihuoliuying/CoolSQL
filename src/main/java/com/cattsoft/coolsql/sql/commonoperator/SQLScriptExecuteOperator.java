/**
 * 
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.sql.SQLException;

import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.component.WaitDialogManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.ScriptExecuteWorker;
import com.cattsoft.coolsql.sql.execute.IMultiStatementExecute;

/**
 * @author ��Т��(kenny liu)
 * 
 * 2008-3-29 create
 */
public class SQLScriptExecuteOperator extends BaseOperator {

	private WaitDialog wd;
	/*
	 * arg type:List 0.bookmark 1.BaseMultiStatementExecute
	 * 
	 * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
	 */
	public void operate(Object arg) throws UnifyException, SQLException {
		if (arg == null)
			throw new IllegalArgumentException("no argument is given");
		// if(!(arg instanceof List))
		// throw new IllegalArgumentException("the arg given by invoker is not
		// suitable(must be List)," +
		// "error type:"+arg.getClass().getName());

		// List list=(List)arg;
		// if(list.size()!=2)
		// throw new IllegalArgumentException("argument size is not enough.");
		// Object firstArg=list.get(0);
		// if(firstArg instanceof Bookmark)
		// throw new IllegalArgumentException("the first element of list must be
		// Bookmark type,error type:"+
		// (firstArg==null?null:firstArg.getClass().getName()));

		// Object secondArg=list.get(1);
		if (!(arg instanceof IMultiStatementExecute))
			throw new IllegalArgumentException(
					"the first element of list must be IMultiStatementExecute type,error type:"
							+ (arg == null ? null : arg.getClass().getName()));

		// Bookmark bookmark=(Bookmark)firstArg;
		final IMultiStatementExecute statementExecuter = (IMultiStatementExecute) arg;
		wd = WaitDialogManage.getInstance().register(
				GUIUtil.findLikelyOwnerWindow());
		wd.addQuitAction(new Actionable() {

			public void action() {
				statementExecuter.cancel();
			}

		});
		ScriptExecuteWorker worker = new ScriptExecuteWorker(statementExecuter,
				wd);
		worker.execute();
		wd.setVisible(true);

	}
}
