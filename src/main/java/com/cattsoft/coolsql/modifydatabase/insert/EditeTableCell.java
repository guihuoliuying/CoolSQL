/*
 * Created on 2007-3-2
 */
package com.cattsoft.coolsql.modifydatabase.insert;

/**
 * @author liu_xlin
 *�༭��ؼ��ı��Ԫ�����Ͷ���
 */
public interface EditeTableCell {

    /**
     * �������Ƿ�Ϊ��
     * @return
     */
    public boolean isNull();
    
    /**
     * ���Ԫ���ڽ����ϵĵ���ʾ����
     * @return
     */
    public String getDisplayLabel();
    
    /**
     * ��ȡ���Ԫ�ض���ֵ
     * @return
     */
    public Object getValue();
    
    /**
     * ���ñ��Ԫ�صĶ���ֵ
     */
    public void setValue(Object value);
    /**
     * �����Ԫ��ֵ��Ϊ�գ�����null��
     *
     */
    public void setEmpty();
}
