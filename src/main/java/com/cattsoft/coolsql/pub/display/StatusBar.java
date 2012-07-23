/*
 * Created on 2007-1-31
 */
package com.cattsoft.coolsql.pub.display;

import javax.swing.BorderFactory;

import com.cattsoft.coolsql.pub.component.TextEditor;

/**
 * @author liu_xlin
 *״̬�����壬�ṩ�����Ϣ����ʾ
 */
public class StatusBar extends TextEditor {
    public StatusBar()
    {
        super();
        initGUI();
    }
    public StatusBar(int size)
    {
        super(size);
        initGUI();
    }
    /**
     * ��ʼ������
     *
     */
    protected void initGUI()
    {
        setEditable(false);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createBevelBorder(1), BorderFactory.createEmptyBorder(0, 4, 0,
						4)));
    }
}
