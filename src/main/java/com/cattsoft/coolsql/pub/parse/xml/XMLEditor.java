/*
 * �������� 2006-11-8
 */
package com.cattsoft.coolsql.pub.parse.xml;

import java.beans.PropertyDescriptor;

/**
 * @author liu_xlin
 *
 */
public interface XMLEditor {
    /**
     * �������
     * @return
     */
    public abstract String getName();
    
    /**
     * ��Ӧ��java����
     * @return
     */
    public abstract Class getJavaClass();
    
    /**
     * ��ȡ���Ե�ֵ
     * @return
     */
    public abstract Object getValue();
    /**
     * �Ƿ�������
     * @return
     */
    public abstract boolean isArray();
    
    /**
     * ��ȡ������������
     * @return
     */
    public abstract PropertyDescriptor getPropertyDescriptor();
    
    /**
     * ��ǰ�Ĳ������ͣ�
     * true: ��bean��xml��ת��
     * false: ��xml��bean��ת��
     * @return
     */
    public abstract boolean editorType();
    
    /**
     * ��������Ҫ������Ԫ�ض�Ӧ�ı���ֵ�Ƿ�Ϊnull��
     * �÷�����xml->bean��bean->xml����ģʽ�ж���ʹ�õ���
     * @return
     */
    public boolean isNull();
}
