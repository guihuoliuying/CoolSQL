/*
 * �������� 2006-10-18
 */
package com.cattsoft.coolsql.exportdata;

import com.cattsoft.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin ��ݵ����ӿ�
 */
public interface Exportable {
    /**
     * ����Ϊ�ı�
     */
    public abstract void exportToTxt() throws UnifyException;

    /**
     * ����Ϊexcel�ļ�
     *  
     */
    public abstract void exportToExcel() throws UnifyException;

    /**
     * ����Ϊhtml
     *  
     */
    public abstract void exportToHtml() throws UnifyException;
}
