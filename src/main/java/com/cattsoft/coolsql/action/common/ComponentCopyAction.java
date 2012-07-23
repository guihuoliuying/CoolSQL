/*
 * �������� 2006-9-15
 */
package com.cattsoft.coolsql.action.common;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import com.cattsoft.coolsql.pub.display.Copyable;

/**
 * @author liu_xlin
 * ������Ƶ��¼�������
 */
public abstract class ComponentCopyAction extends AbstractAction implements Copyable{

    private JComponent com;
    public ComponentCopyAction(JComponent com)
    {
        this.com=com;
    }
    /**
     * @return ���� com��
     */
    public JComponent getComponent() {
        return com;
    }
    /**
     * @param com Ҫ���õ� com��
     */
    public void setComponent(JComponent com) {
        this.com = com;
    }
}
