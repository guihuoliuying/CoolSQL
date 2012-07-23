/*
 * RollbackAction.java
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

import javax.swing.JOptionPane;

import com.cattsoft.coolsql.action.framework.AutoCsAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-25 create
 */
public class RollbackAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;
	
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(RollbackAction.class);

	public void executeAction(ActionEvent e)
	{
		super.executeAction(e);
		
		Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
		
		try {
			bookmark.getConnection().rollback();
			JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(), 
					stringMgr.getString("sqleditor.action.rollback.success"),
					"Information",JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e1) {
			LogProxy.errorReport(e1);
		}
	}
}
