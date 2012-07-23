/*
 * �������� 2006-8-30
 *
 */
package com.cattsoft.coolsql.exportdata.excel;

/**
 * @author liu_xlin
 *excel�ļ�����ʱ������쳣��
 */
public class ExcelProcessException extends Exception {
	private Exception e=null;
    public ExcelProcessException(String name)
    {
    	super(name);
    	e=null;
    }
    public ExcelProcessException(Exception e)
    {
    	super(e.getMessage());
    	this.e=e;
    }
}
