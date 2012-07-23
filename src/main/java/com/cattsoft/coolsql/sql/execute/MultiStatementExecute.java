/**
 * 
 */
package com.cattsoft.coolsql.sql.execute;

import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.ExceptionUtil;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-3-30 create
 */
public class MultiStatementExecute extends BaseMultiStatementExecute{
	
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(MultiStatementExecute.class);
	
	private List<String> sqls;
	
	

	public MultiStatementExecute(Bookmark bookmark,List<String> sqls)
	{
		this(bookmark,sqls,null);
	}
	public MultiStatementExecute(Bookmark bookmark,List<String> sqls,IStatementExecute statementExecuter)
	{
		super(bookmark,statementExecuter);
		this.bookmark=bookmark;
		if(sqls!=null)
			this.sqls=sqls;
		else
			this.sqls=new ArrayList<String>();
	}
	public void cancel() {
		cancelExecution=true;
		if (this.statementExecuter != null)
			statementExecuter.cancel();

	}
	/* (non-Javadoc)
	 * @see com.coolsql.sql.execute.BaseMultiStatementExecute#execute()
	 */
	public void execute() {
		if(sqls==null||sqls.size()==0)
			return;
		
		fireTaskLengthChanged(sqls.size());
		boolean error=false;
		long start=System.currentTimeMillis();
		for(int i=0;i<sqls.size();i++)
		{
			String sql=sqls.get(i);
			error=false;
			try
			{
				error=executeSingleStatement(sql);
				if (this.cancelExecution)
				{
					String msg=stringMgr.getString("batchprocess.execute.cancel");
					fireMessageChanged(msg);
					printMessage(msg);
					break;
				}
			}catch(Exception e)
			{
				printMessage(ExceptionUtil.getDisplay(e));
				error = true;
				LogProxy.errorReport(stringMgr.getString("batchprocess.error") + " "  + sql, e);
				break;
			}finally
			{
				fireProgressChanged(executedCount ++, executedCount);
			}
			if (error && abortOnError) 
				break;
			
		}
		long end=System.currentTimeMillis();
		String msg=stringMgr.getString("batchprocess.execute.totaltime")+(end-start)+" ms";
		LogProxy.getProxy().info(msg);
		printMessage(msg);
		fireMessageChanged(msg);
		if (abortOnError && error)
		{
			this.isSuccess = false;
		}
		else
		{
			this.isSuccess = true;
		}
		isFinished=true;
		try
		{
			if(resultLogger!=null)
				resultLogger.finishLog();
		}finally
		{
			fireMessageChanged(stringMgr.getString("batchprocess.execute.finished"));
		}
	}
}
