/*
 * �������� 2006-6-30
 *
 */
package com.cattsoft.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.cattsoft.coolsql.pub.component.CommonFrame;


/**
 * @author liu_xlin
 *
 * ��һ���İ�ť�����¼�
 */
public class NextButtonAction extends AbstractAction {
	CommonFrame frame=null;

	public NextButtonAction(CommonFrame frame)
	{
		super();
		this.frame=frame;
	}
	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		frame.nextButtonProcess(null);
	}

}
