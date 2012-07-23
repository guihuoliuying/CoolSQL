/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.tree.DefaultTreeModel;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin ����ǩ��������Ա仯���м��
 */
public class BookmarkChangedListener implements PropertyChangeListener {

	public BookmarkChangedListener() {

	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if (name.equals("displayLabel")) //�ڵ���ʾ���ַ�ı䣬��Ӧ�ĸ���������ʾ
		{
			BookmarkView view = ViewManage.getInstance().getBookmarkView();
			DefaultTreeModel model = (DefaultTreeModel) view.getConnectTree()
					.getModel();
			DefaultTreeNode node = BookmarkTreeUtil.getInstance().getBookMarkNodeByAlias(
					((Bookmark) evt.getSource()).getAliasName());
			model.nodeChanged(node);
		} else if (name.equals("connected")) //��ݿ�����ӷ���仯
		{
			boolean newValue = ((Boolean) evt.getNewValue()).booleanValue();
			if (!newValue) //���ӶϿ�
			{
				Bookmark bm = (Bookmark) evt.getSource();
				BookmarkView view = ViewManage.getInstance().getBookmarkView();
				DefaultTreeNode node = BookmarkTreeUtil.getInstance().getBookMarkNodeByAlias(
						((Bookmark) evt.getSource()).getAliasName());

				node.setExpanded(false);
				DefaultTreeModel model = (DefaultTreeModel) view
						.getConnectTree().getModel();

				if (node.getChildCount() > 0) //�����ǩ�ڵ����ӽڵ㣬��ôֱ�ӷ���
				{
					/**
					 * ��ȡ���ִ�й��sql
					 */
					DefaultTreeNode sqlGroup = (DefaultTreeNode) node.getChildAt(0); //��ȡsql��Ͻڵ�
					String[] sql = new String[sqlGroup.getChildCount()];
					for (int i = 0; i < sql.length; i++) {
						Identifier ob = (Identifier) ((DefaultTreeNode) sqlGroup
								.getChildAt(i)).getUserObject();
						sql[i] = ob.getContent();
					}

					//    		    XMLParse saver=XMLParse.getParser();
					//    		    saver.saveBookmarkInfo(bm,sql); //����ڵ���Ϣ

					BookmarkTreeUtil.removeChildrenOfNode(node); //ɾ�����еĽڵ�
				}
				bm.setHasChildren(false);
				model.nodeStructureChanged(node);

			} else //��������ݿ�󣬽���ǩ�ڵ�����Ϊ���ӽڵ�
			{
				Bookmark bm = (Bookmark) evt.getSource();
				bm.setHasChildren(true);
			}
		}
	}

}
