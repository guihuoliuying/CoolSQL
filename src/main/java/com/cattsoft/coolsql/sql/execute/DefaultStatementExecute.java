/**
 * 
 */
package com.cattsoft.coolsql.sql.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.ExceptionUtil;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-3-29 create
 */
public class DefaultStatementExecute implements IStatementExecute {

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(DefaultStatementExecute.class);
	
	private Bookmark bookmark;
	private Statement statement;
	
	private boolean isFinished=false;
	private boolean isStarted=false;
	
	private boolean useSavepoint;
	private Savepoint savepoint;
	private StatementRunnerResult result;
	
	public DefaultStatementExecute(Bookmark bookmark)
	{
		this.bookmark=bookmark;
		result=new StatementRunnerResult();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.sql.execute.IStatementExecute#cancel()
	 */
	public void cancel() {
		if(statement==null)
			return;
		if (isStarted) {
			try {
				statement.cancel();
			} catch (SQLException e) {
				LogProxy.SQLErrorReport("Cancelling statement failed!"
						+ e.getMessage(), e);
			} finally {
				result.setPromptingWasCancelled();
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
					}finally
					{
						statement=null;
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.execute.IStatementExecute#execute(String)
	 */
	public boolean execute(String sql,int maxFetchSize,int queryTimeOut) {
		isStarted=true;
		if(!bookmark.isConnected())
		{
			LogProxy.errorMessage("bookmark("+bookmark.getAliasName()+") is not connected");
			result.failure();
			return false;
		}
		if(result!=null)
			result.clear();
		try {
			statement=bookmark.getConnection().createStatement();
			ISQLDatabaseMetaData md = null;
			md = bookmark.getDbInfoProvider().getDatabaseMetaData();
			boolean isJdbcOdbc = md.getURL().startsWith("jdbc:odbc:");
			if (!isJdbcOdbc) {
				statement.setFetchSize(maxFetchSize);
				statement.setQueryTimeout(queryTimeOut);
			}
		} catch (SQLException e) {
//			LogProxy.SQLErrorReport("create statement failed:"+e.getMessage(), e);
			isFinished=true;
			addErrorInfo(result,sql, e);
			return false;
		}catch(UnifyException e)
		{
			isFinished=true;
			addErrorInfo(result,sql, e);
			return false;
		}
		
		try {
			setSavepoint();
			long sqlExecStart = System.currentTimeMillis();
			boolean flag=statement.execute(sql);
			long time = (System.currentTimeMillis() - sqlExecStart);
			result.setExecutionTime(time);
			result.success();
			if(flag)//query
			{
				result.setResultSet(statement.getResultSet());
			}else  //modify database information
			{
				result.addUpdateCountMsg(statement.getUpdateCount());
			}
			processResult(result);
			return true;
		} catch (SQLException e) {
//			LogProxy.SQLErrorReport("Statement executing failed:"+e.getMessage(), e);
			addErrorInfo(result,sql, e);
			return false;
		}catch(Throwable e)
		{
//			LogProxy.errorReport("Statement executing failed:"+e.getMessage(), e);
			addErrorInfo(result,sql, e);
			return false;
		}
		finally
		{
			isFinished=true;
			if (this.result.isSuccess())
			{
				this.releaseSavepoint();
			}
			else
			{
				this.rollbackSavepoint();
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.execute.IStatementExecute#getExecuteResultInfo()
	 */
	public StatementRunnerResult getExecuteResultInfo() {
		if(!isStarted())
			return null;
		return result;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.execute.IStatementExecute#isFinish()
	 */
	public boolean isFinish() {
		return isFinished;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.execute.IStatementExecute#isStarted()
	 */
	public boolean isStarted() {
		return isStarted;
	}
	private void setSavepoint()
	{
		if (!useSavepoint) return;
		try
		{
			this.savepoint = bookmark.getConnection().setSavepoint();
		}
		catch (Exception e)
		{
			LogProxy.sqlErrorLog("Error creating savepoint", e);
		}
	}
	private void releaseSavepoint()
	{
		if (this.savepoint == null) return;
		try
		{
			bookmark.getConnection().releaseSavepoint(savepoint);
		}
		catch (Exception e)
		{
			LogProxy.sqlErrorLog("Error releasing savepoint", e);
		}
		finally
		{
			this.savepoint = null;
		}
	}
	
	private void rollbackSavepoint()
	{
		if (this.savepoint == null) return;
		try
		{
			bookmark.getConnection().rollback(savepoint);
		}
		catch (Exception e)
		{
			LogProxy.sqlErrorLog("Error rolling back savepoint", e);
		}
		finally
		{
			this.savepoint = null;
		}
	}
	protected void addErrorInfo(StatementRunnerResult result,String sql, Throwable e)
	{
		result.clear();

		StringBuilder msg = new StringBuilder(150);
		msg.append(stringMgr.getString("statement.execute.error") + "\n");
		msg.append(StringUtil.getMaxSubstring(sql.trim(), 150));

		result.addMessage(msg);
		result.addMessageNewLine();
		result.addMessage(ExceptionUtil.getAllExceptions(e));

		result.failure();
	}
	/**
	 *	Append any warnings from the given Statement and Connection to the given
	 *	StringBuilder. If the connection is a connection to Oracle
	 *	then any messages written with dbms_output are appended as well
	 *  This behaviour is then similar to MS SQL Server where any messages
	 *  displayed using the PRINT function are returned in the Warnings as well.
	 */
	protected boolean processWarnings(StatementRunnerResult result)
	{
		Connection con;
		try {
			con = bookmark.getConnection();
		} catch (UnifyException e) {
			return false;
		}
		CharSequence warn = SqlUtil.getWarnings(con, this.statement);
		boolean hasWarning = false;
		if (warn != null && warn.length() > 0)
		{
			hasWarning = true;
			if (result.hasMessages()) result.addMessageNewLine();
			result.addMessage(stringMgr.getString("TxtWarnings"));
			result.addMessageNewLine();
			result.addMessage(warn);
			result.setWarning(true);
		}
		return hasWarning;
	}
	protected void processResult(StatementRunnerResult result)
	{
		processWarnings(result);
	}
}
