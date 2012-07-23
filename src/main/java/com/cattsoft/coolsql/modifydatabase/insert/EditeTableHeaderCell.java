/*
 * Created on 2007-1-31
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import com.cattsoft.coolsql.sql.model.Column;

/**
 * @author liu_xlin
 * �༭��ؼ��ı�ͷԪ��ֵ���͵Ķ���
 */
public class EditeTableHeaderCell implements TableHeaderCell {
    private boolean isSelected;  //��Ӧ�����Ƿ���Ч,ѡ��(true):��Ч  

    private Column column;   //�ж���

    public EditeTableHeaderCell(boolean isSelected, Column column) {
        this.isSelected = isSelected;
        this.column = column;
    }

    public boolean getState() {

        return isSelected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.modifydatabase.insert.TableHeaderCell#getHeaderValue()
     */
    public Object getHeaderValue() {
        return column;
    }

    /**
     * @param column
     *            The column to set.
     */
    public void setColumn(Column column) {
        this.column = column;
    }

    /**
     * @param isSelected
     *            The isSelected to set.
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public String toString() {
        return column.getName();
    }
    
}
