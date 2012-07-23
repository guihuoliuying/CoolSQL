/*
 * �������� 2006-6-7
 *
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.log.LogProxy;


/**
 * @author liu_xlin
 *
 */
public class PropertyAction extends PublicAction {
    public PropertyAction(BookmarkView view)
    {
       super(view);	
    }
	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Identifier userOb=(Identifier)(node.getUserObject());
		try {
            userOb.property();
        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
        } catch (SQLException e1) {
            LogProxy.SQLErrorReport(e1);
        }

	}

}
