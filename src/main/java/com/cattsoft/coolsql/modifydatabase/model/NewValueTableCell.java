/*
 * Created on 2007-1-12
 */
package com.cattsoft.coolsql.modifydatabase.model;

import javax.swing.JTable;

/**
 * @author liu_xlin
 *��Ҫ���µ������Ӧ�ı��Ԫ�ض���
 */
public class NewValueTableCell extends BaseTableCell {

    /**
     * ���Ԫ��������ؼ�����
     */
    private JTable table =null;
    
    /**
     * ��ֵ
     */
    private String newValue=null;
    
    /**
     * �Ƿ���Ҫ�޸�
     */
    private boolean isNeedModify;
    
    private String editorType=null;
    public NewValueTableCell(JTable table,String newValue,String editorType)
    {
        this(table,newValue,editorType,false);
    }
    
    public NewValueTableCell(JTable table,String newValue,String editorType,boolean isNeedModify)
    {
        this.table=table;
        this.newValue=newValue;
        this.editorType=editorType;
        this.isNeedModify=isNeedModify;
        
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getValue()
     */
    public Object getValue() {
        return newValue;
    }

    public void setValue(Object ob)
    {
        newValue=ob.toString();
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getTable()
     */
    public JTable getTable() {
        return table;
    }
    public boolean isNeedModify() {
        return isNeedModify;
    }
    /**
     * ���øñ��������Ӧ�����ֶ���Ҫ�����»����
     * @param isNeedModify
     */
    public void setNeedModify(boolean isNeedModify)
    {
        this.isNeedModify=isNeedModify;
    }
    public String getEditorType() {
        return editorType;
    }
    /**
     * ��д�����Ƿ�ɱ༭�����������ֵԪ�ؿɱ༭������Ϊ��Ҫ��ĵ���
     */
    public void setEditable(boolean isEditable)
    {
        super.setEditable(isEditable);
        setNeedModify(isEditable);
    }
    /**
     * �÷�������ʵ��
     */
    public String toString()
    {
        return newValue;
    }
}
