/*
 * EnableAutoCommitMenuListener.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.system.menu.loadlistener;

import javax.swing.JMenuItem;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.system.menubuild.IMenuLoadListener;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-25 create
 */
public class EnableAutoCommitMenuListener implements IMenuLoadListener{

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action(javax.swing.JMenuItem)
	 */
	public void action(JMenuItem item) {
		
		Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
		if(bookmark==null)
		{
			item.setEnabled(false);
		}else
		{
			item.setEnabled(true);
			item.setSelected(bookmark.isAutoCommit());
		}
	}

	
}
