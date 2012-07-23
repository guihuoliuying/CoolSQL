/*
 * EnableAutoCommitAction.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.view.sqleditor.action;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;

import com.cattsoft.coolsql.action.framework.CheckCsAction;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-25 create
 */
public class EnableAutoCommitAction extends CheckCsAction {

	private static final long serialVersionUID = 1L;

	public void executeAction(ActionEvent e)
	{
		super.executeAction(e);
		
		Object ob=e.getSource();
		boolean isSelected;
		if(ob instanceof JCheckBoxMenuItem)
		{
			isSelected=((JCheckBoxMenuItem)ob).isSelected();
		}else if(ob instanceof JCheckBox)
		{
			isSelected=((JCheckBox)ob).isSelected();
		}else
			return;
		
		try {
			BookmarkManage.getInstance().getDefaultBookmark().setAutoCommit(isSelected);
		} catch (Exception e1) {
			LogProxy.errorReport(e1);
		} 
	}
}
