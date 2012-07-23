/*
 * �������� 2006-11-22
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.EntityImpl;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.ResultSetDataProcess;

/**
 * View the count of data in table entity.
 * @author liu_xlin
 */
@SuppressWarnings("serial")
public class ViewCountOfTableAction extends PublicAction {

    public ViewCountOfTableAction(View view)
    {
        super(view);
    }
    
    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
    	//1. Retrieve selected table.
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Identifier userOb = (Identifier) (node.getUserObject());
		if (userOb == null
				|| (userOb.getType() != BookMarkPubInfo.NODE_VIEW && userOb
						.getType() != BookMarkPubInfo.NODE_TABLE)) {
			return;
		}
		EntityImpl entity = (EntityImpl) userOb.getDataObject(); 
		Bookmark bookmark = userOb.getBookmark();

		//2. Execute the operator for SQL executing .
		Operatable operator = null;
		try {
			operator = OperatorFactory
					.getOperator(com.cattsoft.coolsql.sql.commonoperator.SQLResultProcessOperator.class);
		} catch (Exception e1) {
			LogProxy.internalError(e1);
			return;
		}

		String sql = "";
		if (bookmark.getAdapter() != null) {
			sql = bookmark.getAdapter()
					.getCountQuery(entity.getQualifiedName());
		} else 
		{
			sql = "SELECT count(*) FROM " + entity.getQualifiedName();
			return;
		}

		List<Object> argList = new ArrayList<Object>();
		argList.add(bookmark);
		argList.add(sql);
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
