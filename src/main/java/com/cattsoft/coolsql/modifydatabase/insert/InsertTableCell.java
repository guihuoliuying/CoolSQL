/*
 * Created on 2007-3-2
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import com.cattsoft.coolsql.pub.util.StringUtil;

/**
 * @author liu_xlin
 *������ݱ�ؼ��ı�����Ͷ���
 */
public class InsertTableCell implements EditeTableCell {

    private boolean isNull;
    
    private String displayLabel;
    
    private Object value;
    
    public InsertTableCell(Object value,boolean isNull)
    {
        this.isNull=isNull;
        this.displayLabel=value.toString();
        this.value=value;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.insert.EditTableCell#isNull()
     */
    public boolean isNull() {
        return isNull;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.insert.EditTableCell#getDisplayLabel()
     */
    public String getDisplayLabel() {
        return StringUtil.trim(displayLabel);
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.insert.EditTableCell#getValue()
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param displayLabel The displayLabel to set.
     */
    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }
    /**
     * @param isNull The isNull to set.
     */
    public void setNull(boolean isNull) {
        this.isNull = isNull;
        if(isNull)
        {
            value="";
        }
    }
    /**
     * һ��������ֵ�󣬿��ӵ�ǰ��Ԫ���Ϊ��null���
     * @param value The value to set.
     */
    public void setValue(Object value) {
        this.value = value;
        displayLabel=value.toString();
        if(isNull)
            isNull=false;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.insert.EditeTableCell#setEmpty()
     */
    public void setEmpty() {
        setValue("");        
    }
    public String toString()
    {
        return getDisplayLabel();
    }
}
