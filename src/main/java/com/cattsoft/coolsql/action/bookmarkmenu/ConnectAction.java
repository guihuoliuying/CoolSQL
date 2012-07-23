/*
 * �������� 2006-6-7
 *
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.sql.DBThread;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;


/**
 * @author liu_xlin
 *������ݿ���¼�������
 */
public class ConnectAction extends PublicAction {
	private static final long serialVersionUID = 1L;
	public ConnectAction(View view)
    {
       super(view);	
    }
	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		if(node==null)
			return;
		Object userOb=node.getUserObject();
		if(userOb instanceof Bookmark)
		{
		    Bookmark bm=(Bookmark)userOb;
		    if(bm.isConnected()||bm.getConnectState()==BookMarkPubInfo.BOOKMARKSTATE_CONNECTING||
		    		bm.getConnectState()==BookMarkPubInfo.BOOKMARKSTATE_DISCONNECTING)
		    	return;
		    DBThread dbthread=new DBThread(bm);
		    dbthread.start();
		}

	}

}
