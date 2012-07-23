/*
 * �������� 2006-12-15
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;

/**
 * @author liu_xlin
 *�ɹر�tab���ɾ��ҳ����Ĵ���ӿ�
 */
public interface TabClose {
    
    /**
     * ���ɹر�tab����ڵ��ɾ��ͼ�겢��ɾ��ָ��ҳ��ִ�и÷���
     *@param  --��ɾ���ҳ���
     */
    public abstract void doOnClose(Component pageComponent);
}
