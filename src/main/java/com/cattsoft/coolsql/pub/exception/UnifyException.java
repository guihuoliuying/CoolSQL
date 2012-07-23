/*
 * �������� 2006-6-1
 *
 */
package com.cattsoft.coolsql.pub.exception;

/**
 * @author liu_xlin
 *�漰����ݿ⴦������ʱ�׳����쳣
 * 
 */
public class UnifyException extends Exception {
	private static final long serialVersionUID = 6335842122747858303L;
	public UnifyException()
	{
		super();
	}
    public UnifyException(String msg)
    {
    	super(msg);
    }
    public UnifyException(String msg,Throwable e)
    {
        super(msg,e);
    }
    public UnifyException(Throwable e)
    {
        super(e);
    }
}
