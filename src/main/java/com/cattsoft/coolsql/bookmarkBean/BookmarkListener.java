/*
 * �������� 2006-7-1
 *
 */
package com.cattsoft.coolsql.bookmarkBean;

import java.util.EventListener;

/**
 * @author liu_xlin
 *��ǩ�ı����������ǩ��ɾ����ǩ����ӣ���ǩ���ı䣩
 */
public interface BookmarkListener extends EventListener {
	/**
	 * �����ǩ���¼���Ӧ����
	 * @param e
	 */
	public void bookmarkAdded(BookmarkEvent e);
	/**
	 * ɾ����ǩ���¼���Ӧ����
	 * @param e
	 */
	public void bookmarkDeleted(BookmarkEvent e);
}
