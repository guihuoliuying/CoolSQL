/*
 * Created on 2007-3-1
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.view.BookmarkView;

/**
 * @author liu_xlin ����ѡ�е���ǩ�ڵ���ΪĬ����ǩ
 */
public class SetAsDefaultBookmarkAction extends PublicAction {

	private static final long serialVersionUID = 1L;
	
	public SetAsDefaultBookmarkAction(BookmarkView view)
    {
        super(view);
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
                .getConnectTree().getLastSelectedPathComponent());
        Object ob = node.getUserObject();
        if (ob instanceof Bookmark) {
            Bookmark bm = (Bookmark) ob;
            BookmarkManage.getInstance().setDefaultBookmark(bm);
        }
    }

}
