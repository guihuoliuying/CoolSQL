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
 *��һ����ť�¼�����
 */
public class PreButtonAction extends AbstractAction {
	CommonFrame frame=null;
	public PreButtonAction(CommonFrame frame)
	{
		super();
		this.frame=frame;
	}
	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		frame.preButtonProcess(null);
	}

}
