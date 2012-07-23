/*
 * Created on 2007-1-24
 */
package com.cattsoft.coolsql.modifydatabase.model;

/**
 * @author liu_xlin
 *�����м�¼����ʱ,��ѡ���е�Ԫ�ض�����
 */
public class CheckBean {

    /**
     * ��ǰ��ѡ���ѡ��ֵ
     */
    private Boolean selectValue=null;
    /**
     * ��Ӧ��ѡ���Ƿ����
     */
    private boolean isEnable;
    public CheckBean(Boolean selectValue,boolean isEnable)
    {
        this.selectValue=selectValue;
        this.isEnable=isEnable;
    }
    /**
     * @return Returns the isEnable.
     */
    public boolean isEnable() {
        return isEnable;
    }
    /**
     * @param isEnable The isEnable to set.
     */
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
    /**
     * @return Returns the selectValue.
     */
    public Boolean getSelectValue() {
        return selectValue;
    }
    /**
     * @param selectValue The selectValue to set.
     */
    public void setSelectValue(Boolean selectValue) {
        this.selectValue = selectValue;
    }
    public String toString()
    {
        return String.valueOf(selectValue==null?false:selectValue.booleanValue());
    }
}
