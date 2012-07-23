/*
 * �������� 2006-7-2
 *
 */
package com.cattsoft.coolsql.view.bookmarkview;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.cattsoft.coolsql.view.BookmarkView;


/**
 * @author liu_xlin
 *��ǩ��ͼ�е���ǩ����ѡ���������Ӧ�Ĵ���
 */
public class BookMarkTreeSelect implements TreeSelectionListener {

	private BookmarkView view=null;
	public BookMarkTreeSelect(BookmarkView view)
	{
		this.view=view;
	}
	/* ���� Javadoc��
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) 
	{
		TreePath temp = e.getPath();

		DefaultMutableTreeNode n = (DefaultMutableTreeNode) temp.getLastPathComponent();
		DefaultTreeModel model = (DefaultTreeModel) (view.getConnectTree().getModel());

		if (!model.isLeaf(n)) {

		} else {
			temp = temp.getParentPath();
			n = (DefaultMutableTreeNode) temp.getLastPathComponent();
	
		}

	}

}
