/*
 * �������� 2006-9-9
 */
package com.cattsoft.coolsql.sql;

/**
 * @author liu_xlin
 *exception that be thrown when bookmark hasn't valid connection of database
 */
public class NotConnectedException extends ConnectionException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotConnectedException()
    {
    }
}
