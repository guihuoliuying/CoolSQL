package com.cattsoft.coolsql.sql;

import com.cattsoft.coolsql.pub.util.StringUtil;

public class ScriptCommandDefinition
{
	private final String command;
	private final int startPosInScript;
	private final int endPosInScript;
	private int indexInScript;
	
	public ScriptCommandDefinition(String c, int start, int end)
	{
		this(c, start, end, -1);
	}
	
	public ScriptCommandDefinition(String c, int start, int end, int index)
	{
		this.command = StringUtil.rtrim(c);
		this.startPosInScript = start;
		this.endPosInScript = end;
		this.indexInScript = index;
	}
	
	public String getSQL() { return this.command; }
	public int getStartPositionInScript() { return this.startPosInScript; }
	public int getEndPositionInScript() { return this.endPosInScript; }
	public int getIndexInScript() { return this.indexInScript; }
	public void setIndexInScript(int index) { this.indexInScript = index; }
	
	public String toString() { return this.command; }
}
