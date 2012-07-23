/*
 * �������� 2006-6-7
 *
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin
 *  ���ڵ�ĸ���Action
 */
public class CopyAction extends PublicAction {
	public CopyAction(BookmarkView view) {
		super(view);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Identifier userOb=(Identifier)(node.getUserObject());
		userOb.copy();
	}

	private ActionListener getDefaultCopyAction() {
		BookmarkView bmv = (BookmarkView) this.getComponent();
		InputMap inputMap = bmv.getConnectTree().getInputMap().getParent();
		KeyStroke key = KeyStroke.getKeyStroke("control C");
		Object ob=inputMap.get(key);
		ActionMap actionMap=bmv.getConnectTree().getActionMap().getParent();
		Action action=actionMap.get(ob);
		return action;
	}
}
