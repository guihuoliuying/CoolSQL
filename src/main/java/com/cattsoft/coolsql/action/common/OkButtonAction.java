/*
 * �������� 2006-6-2
 *
 *
 */
package com.cattsoft.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.cattsoft.coolsql.pub.component.CommonFrame;


/**
 * @author liu_xlin
 * ѡ���������������¼�����
 */
public class OkButtonAction extends AbstractAction {
	CommonFrame frame=null;
	public OkButtonAction(CommonFrame frame)
	{
		super();
		this.frame=frame;
	}
	public void actionPerformed(ActionEvent e) {
		frame.shutDialogProcess(null);
	}

}
