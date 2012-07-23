/*
 * Created on 2007-1-12
 */
package com.cattsoft.coolsql.modifydatabase.model;

import javax.swing.JTable;

/**
 * @author liu_xlin
 *ʵ������ֶζ�Ӧ��ԭֵ�����а���Ķ���
 */
public class OldValueTableCell extends BaseTableCell {

    /**
     * �ֶ�ԭֵ����,���ı�չʾ
     */
    private String value=null;
    
    /**
     * ������ؼ�
     */
    private JTable table=null;
    
    public OldValueTableCell(JTable table,String value)
    {
        this(table,value,false);
    }
    public OldValueTableCell(JTable table,String value,boolean isEditable)
    {
        super(isEditable,false,false);
        this.table=table;
        this.value=value;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getValue()
     */
    public Object getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getTable()
     */
    public JTable getTable() {
        return table;
    }
    public String toString()
    {
        return value;
    }
}
