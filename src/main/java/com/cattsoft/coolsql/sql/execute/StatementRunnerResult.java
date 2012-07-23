/**
 * 
 */
package com.cattsoft.coolsql.sql.execute;

import java.sql.ResultSet;
import java.text.DecimalFormat;

import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.DataUtil;
import com.cattsoft.coolsql.pub.util.MessageBuffer;

/**
 * record the result of statement executing, including waring, updated rows count, issuccess, wasCancelled and so on.
 * @author ��Т��(kenny liu)
 *
 * 2008-3-29 create
 */
public class StatementRunnerResult {
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(StatementRunnerResult.class);
	private long totalUpdateCount;
	private MessageBuffer message;
	private String sourceCommand;
	
	private ResultSet resultSet;
	
	private boolean success = true; //indicate whether statement has been executed successfully.
	private boolean hasWarning = false;//indicate whether it has some warning information when statement was executed.
	private boolean wasCancelled = false;//indicate whether current statement has been cancelled.
	
	private long executionTime = -1;
	private DecimalFormat timingFormatter;
	
	public StatementRunnerResult()
	{
		this.timingFormatter = DataUtil.createTimingFormatter();
		message=new MessageBuffer();
	}
	public StatementRunnerResult(String sqlCommand)
	{
		this();
		this.sourceCommand = sqlCommand;
	}
	public boolean promptingWasCancelled() { return wasCancelled; }
	public void setPromptingWasCancelled() { this.wasCancelled = true; }
	
	public void setExecutionTime(long t) { this.executionTime = t; }
	public long getExecutionTime() { return this.executionTime; }
	
	public void success() { this.success = true; }
	public void failure() { this.success = false; }
	public void setWarning(boolean flag) { this.hasWarning = flag; }
	public boolean hasWarning() { return this.hasWarning; }

	public boolean isSuccess() { return this.success; }
	public String getSourceCommand() { return this.sourceCommand; }
	public void addUpdateCountMsg(int count)
	{
		this.totalUpdateCount += count;
		addMessage(count + " " + stringMgr.getString("message.execute.updatecount"));
	}
	public String getTimingMessage()
	{
		if (executionTime == -1) return null;
		StringBuilder msg = new StringBuilder(100);
		msg.append(stringMgr.getString("message.execute.time"));
		msg.append(' ');
		double time = ((double)executionTime) / 1000.0;
		msg.append(timingFormatter.format(time));
		return msg.toString();
	}
	public void addMessage(MessageBuffer buffer)
	{
		if (buffer == null) return;
		this.message.append(buffer);
	}

	public void addMessageNewLine()
	{
		this.message.appendNewLine();
	}
	
	public void addMessage(CharSequence msgBuffer)
	{
		if (msgBuffer == null) return;
		if (message.getLength() > 0) message.appendNewLine();
		message.append(msgBuffer);
	}

	public boolean hasmessage()
	{
		if (this.message == null) return false;
		return (message.getLength() > 0);
	}
	/**
	 * Return the message that have been collected for this result.
	 * This will clear the internal buffer used to store the message.
	 */
	public CharSequence getMessageBuffer()
	{
		if (this.message == null) return null;
		return message.getBuffer();
	}
	
	public long getTotalUpdateCount()
	{
		return totalUpdateCount;
	}
	public void setResultSet(ResultSet set)
	{
		this.resultSet=set;
	}
	public ResultSet getResultSet()
	{
		return resultSet;
	}
	public boolean hasMessages()
	{
		if (this.message == null) return false;
		return (message.getLength() > 0);
	}
	/**
	 * close resultset object.
	 */
	public void clearResultSets()
	{
		if (this.resultSet != null) {

			if (resultSet != null) {
				try {
					resultSet.clearWarnings();
				} catch (Exception th) {
				}
				try {
					resultSet.close();
				} catch (Exception th) {
				}
			}

		}
	}

	public void clearMessageBuffer()
	{
		this.message.clear();
	}
	
	public void clear()
	{
		clearResultSets();
		clearMessageBuffer();
		this.totalUpdateCount = 0;
		this.sourceCommand = null;
		this.hasWarning = false;
		this.executionTime = -1;
	}
}
