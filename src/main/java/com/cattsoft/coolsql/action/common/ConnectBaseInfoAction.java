/*
 * �������� 2006-6-30
 *
 */
package com.cattsoft.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.component.CommonFrame;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.BookMarkwizard.ConnectPropertyDialog;


/**
 * @author liu_xlin
 *
 */
public class ConnectBaseInfoAction extends AbstractAction {
	CommonFrame frame=null;
	View view=null;
	Bookmark bookmark=null;
	public ConnectBaseInfoAction(CommonFrame frame,View view,Bookmark bookmark)
	{
		super();
		this.frame=frame;
		this.view=view;
		this.bookmark=bookmark;
	}
	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		new ConnectPropertyDialog(frame,view,bookmark);
		frame.dispose();
	}

}
