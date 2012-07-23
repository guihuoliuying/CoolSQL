/*
 * �������� 2006-10-19
 */
package com.cattsoft.coolsql.exportdata;

import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;

/**
 * @author liu_xlin
 *  
 */
public class ExportFactory {

    /**
     * �����ı��ؼ��ĵ�����
     * 
     * @param source
     * @return
     */
    public static ExportData createExportForTextComponent(JTextComponent source) {
        return new ExportFromTextComponent(source);
    }
    /**
     * ����table�ؼ����
     * @param table --JTable���Ϳؼ�
     * @return
     */
    public static ExportData createExportForTable(JTable table)
    {
        return new ExportDataFromTable(table);
    }
    /**
     * ����ͨ��sqlִ�еõ��Ľ�������ݵ�����ʵ����
     * @param bookmark ��ǩ
     * @param sql  sql���
     * @return
     */
    public static ExportData createExportForSql(Bookmark bookmark,String sql)
    {
        return new ExportDataFromSql(sql,bookmark);
    }
}
