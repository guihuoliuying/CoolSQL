package com.cattsoft.coolsql.sql;
/**
 * 
 * @author liu_xlin
 *�����쳣������Ӧ��ǩ����ʱ���ֵ�����ʱ���׳����쳣��
 */

public class ConnectionException extends Exception
{
    /**
     * ����ʧ�ܵĴ����쳣����
     */
    private Throwable cause;
    public ConnectionException()
    {
        cause = null;
    }

    public ConnectionException(String message)
    {
        super(message);
        cause = null;
    }

    public ConnectionException(String message, Throwable cause)
    {
        super(message);
        this.cause = null;
        this.cause = cause;
    }

    public ConnectionException(Throwable cause)
    {
        super(cause.getMessage());
        this.cause = null;
        this.cause = cause;
    }

    public Throwable getCause()
    {
        return cause;
    }

    public String toString()
    {
        String base = super.toString();
        if(cause != null)
            base = base + System.getProperty("line.separator") + "Root cause:" + System.getProperty("line.separator") + cause.toString();
        return base;
    }

    
}
