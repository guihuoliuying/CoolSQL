/*
 * �������� 2006-12-25
 */
package com.cattsoft.coolsql.system;

/**
 * @author liu_xlin
 *����ӿ�
 */
public interface Task {

    /**
     * ��ȡ��ǰ���������
     * @return  --��������
     */
    public abstract String getDescribe();
    
    /**
     * ִ������������߼�����
     *
     */
    public abstract void execute();
    
    /**
     * ��ȡ������Ĺ�����
     * @return  --int��������
     */
    public abstract int getTaskLength();
}
