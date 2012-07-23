/*
 * Created on 2007-2-8
 */
package com.cattsoft.coolsql.view.sqleditor.pop;

import java.util.EventListener;

/**
 * @author liu_xlin
 *��������ѡ����Ӧ�ĵ��øü������ķ������д���
 */
public interface SelectedListener extends EventListener{

    /**
     * ѡ���Ĵ��?��
     * @param value
     */
    public void selected(Object value);
}
