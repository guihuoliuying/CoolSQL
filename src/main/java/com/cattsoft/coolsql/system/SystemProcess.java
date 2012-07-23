/*
 * �������� 2006-12-26
 */
package com.cattsoft.coolsql.system;

/**
 * @author liu_xlin
 *ϵͳ����ӿ�
 */
public interface SystemProcess {

    /**
     * ����ϵͳ����
     *
     */
    public abstract void start();
    
    /**
     * ��ȡ�ô��������
     * @return
     */
    public abstract String getDescribe();
}
