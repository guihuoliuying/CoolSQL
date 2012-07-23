/**
 * 
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.action.common.InsertToEntityCommand;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * ��ǩ��ͼ��ͨ���Ҽ�˵���ѡ�е�ʵ�������ݵ���ӣ���Action�ദ�����Ĳ���
 * @author kenny liu
 * 
 * 2007-12-12 create
 */
public class AddEntityDataAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	public AddEntityDataAction() {
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) ViewManage
				.getInstance().getBookmarkView().getConnectTree()
				.getLastSelectedPathComponent();
		Identifier userOb = (Identifier) (node.getUserObject());
		if (userOb.getType() == BookMarkPubInfo.NODE_TABLE
				|| userOb.getType() == BookMarkPubInfo.NODE_VIEW) {
			ActionCommand command = new InsertToEntityCommand((Entity)userOb.getDataObject());
			try {
				command.exectue();
			} catch (Exception e1) {
				LogProxy.errorReport("initing frame used to add data errors:"+e1.getMessage(), e1);
			}
		}
	}

}
