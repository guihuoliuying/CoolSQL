/**
 * 
 */
package com.cattsoft.coolsql.view.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-3-30 create
 */
public class FileLogger implements ILogger {

	private PrintStream ps;
	public FileLogger(File file)
	{
		try {
			ps=new PrintStream(file);
		} catch (FileNotFoundException e) {
		}
	}
	/* (non-Javadoc)
	 * @see com.coolsql.view.log.ILogger#appendLog(Object)
	 */
	public void appendLog(Object ob) {
		if(ps!=null)
			ps.println(ob);
	}
	/* (non-Javadoc)
	 * @see com.coolsql.view.log.ILogger#finishLog()
	 */
	public void finishLog() {
		if(ps!=null)
		{
			ps.close();
			ps=null;
		}
		
	}

}
