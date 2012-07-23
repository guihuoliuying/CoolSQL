/*
 * �������� 2007-1-8
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;

import com.jidesoft.swing.JidePopupMenu;

/**
 * @author liu_xlin
 *1���޸������Ҽ���˵�ʱ���ü��������ȡ����
 */
public class BasePopupMenu extends JidePopupMenu {

    public BasePopupMenu()
    {
        super();
    }
    public BasePopupMenu(String label)
    {
        super(label);
    }
    /**
     * ��д�Ҽ�˵�����ʱ�������������ȡ����
     */
    public void show(Component invoker,int x,int y)
    {
        if(invoker!=null)
            invoker.requestFocusInWindow();
        super.show(invoker,x,y);
    }
}
