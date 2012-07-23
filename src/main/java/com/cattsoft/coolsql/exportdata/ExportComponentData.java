/*
 * �������� 2006-10-19
 */
package com.cattsoft.coolsql.exportdata;

import javax.swing.JComponent;

import com.cattsoft.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin ���������ݻ���
 */
public class ExportComponentData extends ExportData {

    /**
     * @param source
     */
    public ExportComponentData(JComponent source) {
        super(source);
    }

    /**
     * ����ӿ��еķ�������������Ϊ������ʹ��
     */
    public void exportToTxt() throws UnifyException {
    }

    public void exportToExcel() throws UnifyException {
    }

    public void exportToHtml() throws UnifyException {
    };
}
