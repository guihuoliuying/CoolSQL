/**
 * 
 */
package com.cattsoft.coolsql.sql.execute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.display.DelimiterDefinition;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.ExceptionUtil;
import com.cattsoft.coolsql.system.DatabaseSetting;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.log.ILogger;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.sqleditor.ScriptParser;

/**
 * A class to run several statements from a script file.
 * 
 * @author ��Т��(kenny liu)
 * 
 * 2008-3-29 create
 */
public class ScriptStatementExecute extends BaseMultiStatementExecute {
	private static final StringManager stringMgr = StringManagerFactory
			.getStringManager(ScriptStatementExecute.class);

	private DelimiterDefinition limiter;
	private List<File> filenames;
	private String encoding = null; // encoding of script file

	/**
	 * alternative item, user can enable/disable them by modify the setting .
	 */
	private boolean checkEscapedQuotes = false;

	public ScriptStatementExecute(Bookmark bookmark) {
		this(bookmark, (List<File>) null);
	}
	public ScriptStatementExecute(Bookmark bookmark, List<File> filenames) {
		this(bookmark, filenames, null);
	}
	public ScriptStatementExecute(Bookmark bookmark, File[] filenames) {
		this(bookmark, filenames, null);
	}
	public ScriptStatementExecute(Bookmark bookmark, File[] files,
			IStatementExecute statementExecuter) {
		super(bookmark, statementExecuter);
		filenames = new ArrayList<File>();
		for (File file : files) {
			filenames.add(file);
		}
	}
	public ScriptStatementExecute(Bookmark bookmark, List<File> filenames,
			IStatementExecute statementExecuter) {
		super(bookmark, statementExecuter);
		this.filenames = filenames;
		if (filenames == null)
			filenames = new ArrayList<File>();
	}
	public void cancel() {
		cancelExecution = true;
		if (this.statementExecuter != null)
			statementExecuter.cancel();
	}
	public void execute() {
		boolean isError = false;
		long start = System.currentTimeMillis();
		String msg = "";
		for (int i = 0; i < filenames.size(); i++) {
			File f = filenames.get(i);

			msg = stringMgr.getString("batchprocess.start") + " "
					+ f.getAbsolutePath();
			/** print msg to ui prompt, log view and outside logger */
			fireMessageChanged(msg);
			printMessage(msg);
			LogProxy.getProxy().info(msg);

			try {
				isError = this.execute(f, encoding);
			} catch (Throwable e) {
				isError = true;
				LogProxy.errorReport(stringMgr
						.getString("batchprocess.filescript.error")
						+ " " + f.getAbsolutePath(), e);

				if (e instanceof FileNotFoundException) {
					msg = stringMgr
							.getString("batchprocess.filescript.filenotfound")
							+ f.getAbsolutePath();
				} else {
					msg = e.getMessage();
				}
				printMessage(stringMgr
						.getString("batchprocess.filescript.txtError")
						+ ": " + msg);
			}
			if (isError && abortOnError) {
				break;
			}
		}
		long end = System.currentTimeMillis();
		msg = stringMgr.getString("batchprocess.execute.totaltime")
				+ (end - start) + " ms, "+stringMgr.getString("batchprocess.execute.updatedrows",updatedRowCount);
		LogProxy.getProxy().info(msg);
		printMessage(msg);
		fireMessageChanged(msg);
		if (abortOnError && isError) {
			this.isSuccess = false;
		} else {
			this.isSuccess = true;
		}
		isFinished = true;
		try {
			if (resultLogger != null)
				resultLogger.finishLog();
		} finally {
			fireMessageChanged(stringMgr
					.getString("batchprocess.execute.finished"));
		}
	}
	protected boolean execute(File file, String encoding) throws IOException {
		DatabaseSetting dbSetting = Setting.getInstance().getDatabaseSetting(
				bookmark.getAliasName());
		ScriptParser parser = new ScriptParser();
		limiter = new DelimiterDefinition(dbSetting.getSQLStatementSeparator(),
				false);

		parser.setDelimiter(limiter);

		parser.setSupportOracleInclude(dbSetting.supportSingleLineCommands());
		parser.setCheckForSingleLineCommands(dbSetting.supportShortInclude());

		parser.setFile(file, encoding);
		parser.setCheckEscapedQuotes(this.checkEscapedQuotes);

		long start = System.currentTimeMillis();
		long end = 0;
		String sql;
		boolean error = false;

		parser.startIterator();

		while (parser.hasNext()) {
			sql = parser.getNextCommand();
			if (sql == null)
				continue;

			try {
				error = executeSingleStatement(sql);
				if (this.cancelExecution) {
					String msg = stringMgr
							.getString("batchprocess.execute.cancel");
					fireMessageChanged(msg);
					printMessage(msg);
					break;
				}

			} catch (Exception e) {
				printMessage(ExceptionUtil.getDisplay(e));
				error = true;
				LogProxy.errorReport(stringMgr.getString("batchprocess.error")
						+ " " + sql, e);
				break;
			} finally {
				executedCount++;
			}
			if (error && abortOnError)
				break;
		}

		end = System.currentTimeMillis();

		StringBuilder msg = new StringBuilder();
		msg.append(file.getCanonicalPath());
		msg.append(": ");
		msg.append(executedCount);
		msg.append(' ');
		msg.append(stringMgr.getString("batchprocess.file.execute.sqlcount"));
		this.printMessage(msg.toString());

		parser.done();

		// if (this.showTiming)
		// {
		long execTime = (end - start);
		String m = stringMgr.getString("batchprocess.file.execute.costtime")
				+ " " + (((double) execTime) / 1000.0) + "s";
		this.printMessage(m);
		// }

		return error;
	}
	/**
	 * @return the checkEscapedQuotes
	 */
	public boolean isCheckEscapedQuotes() {
		return this.checkEscapedQuotes;
	}
	/**
	 * @param checkEscapedQuotes
	 *            the checkEscapedQuotes to set
	 */
	public void setCheckEscapedQuotes(boolean checkEscapedQuotes) {
		this.checkEscapedQuotes = checkEscapedQuotes;
	}
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return this.encoding;
	}
	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	/**
	 * @return the limiter
	 */
	public DelimiterDefinition getLimiter() {
		return this.limiter;
	}
	/**
	 * @param limiter
	 *            the limiter to set
	 */
	public void setLimiter(DelimiterDefinition limiter) {
		this.limiter = limiter;
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
	 * @param statementExecuter
	 *            the statementExecuter to set
	 */
	public void setStatementExecuter(IStatementExecute statementExecuter) {
		this.statementExecuter = statementExecuter;
	}
	/**
	 * @return the cancelExecution
	 */
	public boolean isCancelExecution() {
		return this.cancelExecution;
	}
	/**
	 * @return the executedCount
	 */
	public int getExecutedCount() {
		return this.executedCount;
	}
	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return this.isSuccess;
	}
}
