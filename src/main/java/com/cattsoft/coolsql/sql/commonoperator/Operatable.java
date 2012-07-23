/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.sql.SQLException;

import com.cattsoft.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin ����ݿ�����Ĺ����ӿ�
 */
public interface Operatable {

    /**
     * �������ܷ��������ڹ����ĵ���
     * 
     * @param arg
     */
    public abstract void operate(Object arg) throws UnifyException,SQLException;

    /**
     * �������ܷ��������ڹ����ĵ���,ͬ�ϣ�ֻ������ʹ����������
     * 
     * @param arg
     */
    public abstract void operate(Object arg0, Object arg1)throws UnifyException,SQLException;
}
