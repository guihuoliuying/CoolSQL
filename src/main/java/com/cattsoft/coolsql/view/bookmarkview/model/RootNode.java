/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author liu_xlin ��ǩ��ڵ���
 */
public class RootNode extends Identifier {

	private static final long serialVersionUID = 1L;

	public RootNode() {
		super();
	}
	public RootNode(String content, Bookmark bookmark) {
		super(BookMarkPubInfo.NODE_HEADER, content, bookmark);
	}
	public RootNode(String content, Bookmark bookmark, boolean isHasChild) {
		super(BookMarkPubInfo.NODE_HEADER, content, bookmark, isHasChild);
	}
	/*
	 * ���� Javadoc��
	 * 
	 * @see
	 * com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql
	 * .view.bookmarkview.model.ITreeNode)
	 */
	public void expand(DefaultTreeNode parent, INodeFilter filter) {

	}

}
