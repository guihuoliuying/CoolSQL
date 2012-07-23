/**
 * 
 */
package com.cattsoft.coolsql.sql.execute;


/**
 * Statement executing interface .
 * @author ��Т��(kenny liu)
 *
 * 2008-3-29 create
 */
public interface IStatementExecute {

	/**
	 * Statment executes sql, and save information related to the execution  .
	 * @param sql --sql statment that need be executed
	 * @param maxFetchSize -- the number of rows to fetch, If the value specified is zero, then the hint is ignored.
	 * @param queryTimeout --the new query timeout limit in seconds; zero means 
     *        there is no limit
     * @return return true if executing successfully, otherwise return false.
	 */
	public boolean execute(String sql,int maxFetchSize,int queryTimeOut);
	
	/**
	 * Get all information related to execution, including query data, time executing costed, error and so on.
	 * @return return null if statement executing is not started
	 */
	public StatementRunnerResult getExecuteResultInfo();
	
	/**
	 * indicates whether executing has been finished.
	 * @return true if finished,otherwise false;
	 */
	public boolean isFinish();
	
	/**
	 * indicates whether executing started.
	 * @return true if started, otherwise false.
	 */
	public boolean isStarted();
	
	/**
	 * if statement is executing, you can cancel it.
	 *
	 */
	public void cancel();
}
