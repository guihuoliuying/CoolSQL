/*
 * �������� 2006-11-9
 */
package com.cattsoft.coolsql.system;

import com.cattsoft.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin
 *
 */
public interface CloseSystem {

    /**
     * �رճ����ִ�д���
     *
     */
    public abstract void close() throws UnifyException;
    
    /**
     * ������Ϣ
     */
    public abstract void save() throws UnifyException;
}
