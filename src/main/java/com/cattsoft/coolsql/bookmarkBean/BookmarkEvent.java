/*
 * �������� 2006-7-1
 *
 */
package com.cattsoft.coolsql.bookmarkBean;

import java.util.EventObject;

/**
 * @author liu_xlin
 * ��ǩ�¼�����
 */
public class BookmarkEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	/**
	 * ����µ���ǩ
	 */
	public static final int BOOKMARK_ADD=0;
	/**
	 * ɾ����ǩ
	 */
	public static final int BOOKMARK_DELETE=1;
	/**
	 * ������ǩ
	 */
	public static final int BOOKMARK_UPDATE=2;
	
	private Bookmark bookmark=null;
	public BookmarkEvent(BookmarkManage source,int type,Bookmark bookmark) {
		super(source);
		this.bookmark=bookmark;
	}

	/**
	 * @return ���� bookmark��
	 */
	public Bookmark getBookmark() {
		return bookmark;
	}
}
