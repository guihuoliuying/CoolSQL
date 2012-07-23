/*
 * Created on 2007-4-26
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;

import com.cattsoft.coolsql.pub.display.FindProcessConfig;

/**
 * @author liu_xlin
 *�����ض�����Ĳ��Ҵ��?����ʵ�ִ˽ӿڡ�
 */
public interface FindProcess {

    /**
     * �����ض���������ܻ��Բ�ͬ�ķ�ʽ������ݵĲ��ң��˷���ʵ������ݵ�ƥ����ҹ��ܡ�
     * @param config --���ҵĲ������ã�������com.coolsql.data.display.FindProcessConfig����
     * @param com --�����ҵ��������
     * @return --true:���ҳɹ��� false:����ʧ��
     * @throws Exception
     */
    public boolean find(FindProcessConfig config,Component com) throws Exception;
    
    /**
     * �Բ��ҽ���������
     * @return
     */
    public String resultInfo();
}
