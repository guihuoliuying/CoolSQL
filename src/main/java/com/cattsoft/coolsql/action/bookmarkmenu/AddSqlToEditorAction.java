/*
 * �������� 2006-11-30
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *
 */
public class AddSqlToEditorAction extends PublicAction {

    public AddSqlToEditorAction(View view)
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
		Identifier userOb=(Identifier)(node.getUserObject());
		if(userOb==null)
		    return;
		if(userOb.getType()==BookMarkPubInfo.NODE_RECENTSQL)
		{
		    Operatable operator=null;
		    try {
                operator=OperatorFactory.getOperator(com.cattsoft.coolsql.sql.commonoperator.AddSqlToEditorOperator.class);
            }  catch (Exception e1) {
                LogProxy.internalError(e1);
                return;
            }
		    try {
                operator.operate(userOb.getContent());
            } catch (UnifyException e2) {
                LogProxy.errorReport(e2);
            } catch (SQLException e2) {
                LogProxy.SQLErrorReport(e2);
            }
		}

    }

}
