/*
 * Created on 2007-1-12
 */
package com.cattsoft.coolsql.modifydatabase.model;

import javax.swing.JTable;

import com.cattsoft.coolsql.sql.model.Column;

/**
 * @author liu_xlin
 *����Ķ��壬��Ӧ�����������еı����󡣸��б���ܱ༭��ֻ����Ϊ��ʾ���ݡ�
 */
public class ColumnNameTableCell extends BaseTableCell {

    /**
     * �ñ���Ӧ��ʵ���ж���
     */
    private Column columnValue=null;
    
    /**
     * ���������ؼ�����
     */
    private JTable table=null;
    
    /**
     * ����չʾ��ֵ
     */
    private String displayValue=null;

    public ColumnNameTableCell(JTable table,Column column)
    {
        this(table,column,column.isPrimaryKey()?true:false);
    }
    public ColumnNameTableCell(JTable table,Column column,boolean isAsTerm)
    {
        super(false,isAsTerm,false);
        this.table=table;
        this.columnValue=column;

        if(column!=null)
        {
            displayValue=column.isPrimaryKey()?"<u>"+column.getName()+"</u>":column.getName();
        }
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getValue()
     */
    public Object getValue() {
        return columnValue;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getTable()
     */
    public JTable getTable() {
        return table;
    }
    public String toString()
    {
        return columnValue.getName();
    }
    public String getStyleString()
    {
        if(isAsTerm())
            return "<html><body><b>"+displayValue+"</i><body></html>";
        else
            return "<html><body>"+displayValue+"<body></html>";
    }
}
