/*
 * �������� 2006-12-1
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author liu_xlin ˫��������¼�����
 */
public class DoubleClickListener extends MouseAdapter{

    private ActionListener action = null;

    public DoubleClickListener(ActionListener action) {
        if(action==null)
            throw new IllegalArgumentException("action can't be value:null");
        this.action = action;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {   //���˫��
//            ActionEvent event = new ActionEvent(e.getSource(),
//                    ActionEvent.ACTION_PERFORMED, "double click");
            action.actionPerformed(null);
        }
    }

}
