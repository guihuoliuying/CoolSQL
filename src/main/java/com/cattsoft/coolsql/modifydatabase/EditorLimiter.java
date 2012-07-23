/*
 * Created on 2007-1-23
 */
package com.cattsoft.coolsql.modifydatabase;

/**
 * @author liu_xlin
 *�༭���������ʾ�ӿ�
 */
public interface EditorLimiter {

    /**
     * �༭�����������ݵ���󳤶ȣ��÷������������б༭��
     *@param size  --���������󳤶�
     */
    public abstract void setSize(int size);
    
    /**
     * ���༭��������Ϊ�������룬�÷�������ʹ��
     *
     */
    public abstract void setDigits(int digits);
}
