/**
 * 
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author kenny liu
 * 
 * 2007-12-12 create
 */
public class CearTableAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Operatable operator = null;
		try {
			operator = OperatorFactory
					.getOperator("com.coolsql.sql.commonoperator.SQLResultProcessOperator");
		} catch (Exception e1) {
			LogProxy.internalError(e1);
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) ViewManage
				.getInstance().getBookmarkView().getConnectTree()
				.getLastSelectedPathComponent();
		Identifier userOb = (Identifier) (node.getUserObject());
		if (userOb.getType() == BookMarkPubInfo.NODE_TABLE
				|| userOb.getType() == BookMarkPubInfo.NODE_VIEW) {
			/**
			 * �����û�
			 */
			int result = JOptionPane
					.showConfirmDialog(
							GUIUtil.getMainFrame(),
							PublicResource
									.getString("bookmarkView.popup.clearentity.confirm"),
							"confirm delete all", JOptionPane.YES_NO_OPTION);
			if (result != JOptionPane.YES_OPTION)
				return;

			/**
			 * ִ��ɾ�����
			 */
			List argList = new ArrayList();
			Entity entity = (Entity) userOb.getDataObject();
			String deleteSql = userOb.getBookmark().getAdapter().getDelete(
					entity.getQualifiedName(), "");
			argList.add(userOb.getBookmark());
			argList.add(deleteSql);
			argList.add(new Integer(ResultSetDataProcess.EXECUTE));
			try {
				operator.operate(argList);
			} catch (UnifyException e2) {
				LogProxy.errorReport(e2);
			} catch (SQLException e2) {
				LogProxy.SQLErrorReport(e2);
			}
		}
	}

}
