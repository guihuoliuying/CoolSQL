/*
 * �������� 2006-7-3
 *
 */
package com.cattsoft.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

/**
 * @author liu_xlin
 *�༭���ļ����¼�
 */
public class TextCutAction extends AbstractAction {

	private JTextComponent _com=null;
	public TextCutAction(JTextComponent com)
	{
		_com=com;
	}
	
	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		if(_com!=null)
			_com.cut();
	}

}
