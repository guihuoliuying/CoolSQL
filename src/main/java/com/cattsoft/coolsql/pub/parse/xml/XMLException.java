/*
 * �������� 2006-11-7
 */
package com.cattsoft.coolsql.pub.parse.xml;

/**
 * @author liu_xlin
 *xml�����쳣
 */
public class XMLException extends Exception
{

    public XMLException(String msg)
    {
        super(msg);
    }

    public XMLException(Throwable cause)
    {
        super(cause);
    }
    public XMLException(String msg,Throwable cause)
    {
        super(msg,cause);
    }
}
