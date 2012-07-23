/*
 * Created on 2007-2-27
 */
package com.cattsoft.coolsql.view.sqleditor.pop;


/**
 * @author liu_xlin
 *��ʾ��Ϣ�����У��ֶ��б�ؼ���Ԫ����
 */
public class FieldListCell extends BaseListCell {

    private String fieldName;//�ֶ����
    public FieldListCell(String catalog,String schema,String entity,String fieldName,int type)
    {
        super(catalog,schema,entity,null);
        this.fieldName=fieldName;
        this.type=type;
    }
    public String getDisplayLabel()
    {
        return this.toString()+"-->"+super.toString();
    }
    public String toString()
    {
        return fieldName;
    }
    /**
     * @return Returns the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }
    /**
     * @param fieldName The fieldName to set.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
