/**
 * 
 */
package com.cattsoft.coolsql.sql.execute;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.EventListenerList;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.log.ILogger;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 * 
 * 2008-3-30 create
 */
public abstract class BaseMultiStatementExecute
		implements
			IMultiStatementExecute {

	private static final StringManager stringMgr = StringManagerFactory
			.getStringManager(BaseMultiStatementExecute.class);
	public static final String PROPERTY_MESSAGE = "message";
	public static final String PROPERTY_PROGRESS = "progress";
	public static final String PROPERTY_STATE_STARTED = "started";
	/**
	 * alternative item, user can enable/disable them by modify the setting .
	 */
	protected boolean isSuccess = true;

	protected boolean isFinished=false;
	protected boolean cancelExecution = false;

	protected boolean verboseLogging = true; // indicate whether printing
												// detailed log .
	protected boolean abortOnError = true;

	protected int executedCount;// current count of statements executed
	protected long updatedRowCount=0;

	protected IStatementExecute statementExecuter;
	protected Bookmark bookmark;

	protected ILogger resultLogger;// outside logger used to print information
									// related to execution to else log
									// container, etc: file.

	/**
	 * used to manange propertychangelistener list
	 */
	private EventListenerList evList;
	protected BaseMultiStatementExecute(Bookmark bookmark) {
		this(bookmark, null);
	}
	protected BaseMultiStatementExecute(Bookmark bookmark,
			IStatementExecute statementExecuter) {
		this.bookmark = bookmark;
		this.statementExecuter = statementExecuter;
		if(statementExecuter==null)
		{
			this.statementExecuter=new DefaultStatementExecute(bookmark);
		}
		evList = new EventListenerList();
	}
	public void setExecuteLogger(ILogger logger)
	{
		resultLogger=logger;
	}
	/**
	 * @return the abortOnError
	 */
	public boolean isAbortOnError() {
		return this.abortOnError;
	}

	/**
	 * @param abortOnError
	 *            the abortOnError to set
	 */
	public void setAbortOnError(boolean abortOnError) {
		this.abortOnError = abortOnError;
	}

	/**
	 * @return the statementExecuter
	 */
	public IStatementExecute getStatementExecuter() {
		return this.statementExecuter;
	}

	/**
	 * if current statementExecuter has started, this method will do nothing.
	 * 
	 * @param statementExecuter
	 *            the statementExecuter to set
	 */
	public void setStatementExecuter(IStatementExecute statementExecuter) {
		if (this.statementExecuter != null
				&& this.statementExecuter.isStarted()
				&& !this.statementExecuter.isFinish())
			return;
		this.statementExecuter = statementExecuter;
	}

	/**
	 * @return the verboseLogging
	 */
	public boolean isVerboseLogging() {
		return this.verboseLogging;
	}

	/**
	 * @param verboseLogging
	 *            the verboseLogging to set
	 */
	public void setVerboseLogging(boolean verboseLogging) {
		this.verboseLogging = verboseLogging;
	}
	public void changeBookmark(Bookmark bookmark)
	{
		this.bookmark=bookmark;
	}
	/**
	 * @return the bookmark
	 */
	public Bookmark getBookmark() {
		return this.bookmark;
	}

	/**
	 * @return the cancelExecution
	 */
	public boolean isCancelExecution() {
		return this.cancelExecution;
	}

	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return this.isSuccess;
	}
	/**
	 * @return the resultLogger
	 */
	public ILogger getResultLogger() {
		return this.resultLogger;
	}
	/**
	 * @param resultLogger
	 *            the resultLogger to set
	 */
	public void setResultLogger(ILogger resultLogger) {
		this.resultLogger = resultLogger;
	}
	public final void addPropertyListener(PropertyChangeListener l) {
		evList.add(PropertyChangeListener.class, l);
	}

	public final void removePropertyListener(PropertyChangeListener l) {
		evList.remove(PropertyChangeListener.class, l);
	}
	protected boolean executeSingleStatement(String sql) {
		if (sql == null)
			return true;

		String msg = stringMgr.getString("batchprocess.execute.statement")
				+ " " + sql;
		fireMessageChanged(msg);
		if (verboseLogging) {
			this.printMessage(msg);
			LogProxy.getProxy().info(msg);
		}
		long verbstart = System.currentTimeMillis();
		boolean error =statementExecuter.execute(sql, 0, 0);
		long verbend = System.currentTimeMillis();

		StatementRunnerResult result = this.statementExecuter
				.getExecuteResultInfo();
		updatedRowCount += result.getTotalUpdateCount();
		if (result != null) {
			error = !result.isSuccess();

			// We have to store the result of hasMessages()
			// as the getMessageBuffer() will clear the buffer
			// and a subsequent call to hasMessages() will return false;
			String feedback = result.getMessageBuffer().toString();

			if (error) {
				msg = stringMgr.getString("batchprocess.error") + ":"
						+ feedback;
				LogProxy.errorMessage(msg);
				this.printMessage(msg);
			} else if (result.hasWarning()) {
				msg = stringMgr.getString("batchprocess.warning") + ":"
						+ feedback;
				LogProxy.getProxy().warning(msg);
				this.printMessage(msg);
			}
		}
		this.printMessage(stringMgr.getString("batchprocess.execute.sql.time")
				+ " " + (((double) (verbend - verbstart)) / 1000.0) + "s");
		return error;

	}
	/**
	 * notify all changelistener to change their message displayed on the ui.
	 */
	protected void fireMessageChanged(String message) {
		PropertyChangeEvent e = new PropertyChangeEvent(this, PROPERTY_MESSAGE,
				"", message);
		firePropertyEvent(e);
	}
	protected void fireProgressChanged(int oldprogress, int progress) {
		PropertyChangeEvent e = new PropertyChangeEvent(this,
				PROPERTY_PROGRESS, oldprogress, progress);
		firePropertyEvent(e);
	}
	protected void fireTaskLengthChanged(int tasklength) {
		PropertyChangeEvent e = new PropertyChangeEvent(this,
				PROPERTY_STATE_STARTED, 0, tasklength);
		firePropertyEvent(e);
	}
	protected void firePropertyEvent(PropertyChangeEvent e) {
		Object[] list = evList.getListenerList();
		for (int i = list.length - 2; i >= 0; i--) {
			if (list[i] == PropertyChangeListener.class) {
				((PropertyChangeListener) list[i + 1]).propertyChange(e);
			}
		}
	}
	protected void printMessage(String msg) {
		if (resultLogger != null) {
			resultLogger.appendLog(msg);
		}
	}
	/**
	 * @return the executedCount
	 */
	public int getExecutedCount() {
		return this.executedCount;
	}
	/**
	 * @return the updatedRowCount
	 */
	public long getUpdatedRowCount() {
		return this.updatedRowCount;
	}
	/**
	 * @return the isFinished
	 */
	public boolean isFinished() {
		return this.isFinished;
	}
}
