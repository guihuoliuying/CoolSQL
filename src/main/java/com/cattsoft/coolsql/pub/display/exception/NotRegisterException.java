/*
 * �������� 2006-12-13
 */
package com.cattsoft.coolsql.pub.display.exception;

/**
 * @author liu_xlin
 *��Եȴ�Ի�������߳�û����ȴ�Ի���ע�ᣬ����Ȼ��ȡ���̶߳�Ӧ�ĶԻ�����ô���׳����쳣
 */
public class NotRegisterException extends Exception {

    public NotRegisterException()
    {
        super();
    }
    public NotRegisterException(String msg)
    {
        super(msg);
    }
    public NotRegisterException(String msg,Throwable cause)
    {
        super(msg,cause);
    }
    public NotRegisterException(Throwable cause)
    {
        super(cause);
    }
}
