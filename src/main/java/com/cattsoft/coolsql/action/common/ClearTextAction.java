/*
 * �������� 2006-9-26
 */
package com.cattsoft.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

/**
 * @author liu_xlin
 *���������ݴ���
 */
public class ClearTextAction extends AbstractAction {
    private JTextComponent txt;
    public ClearTextAction(JTextComponent txt)
    {
        this.txt=txt;
    }
    /* ���� Javadoc��
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(txt!=null)
            txt.setText("");
    }

}
