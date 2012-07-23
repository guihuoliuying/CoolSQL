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
 * @author liu_xlin
 *��ѯѡ��ʵ����ȫ�����
 */
public class QueryAllDataOfTableAction extends PublicAction {

    public QueryAllDataOfTableAction(View view)
    {
        super(view);
    }
    /* 
     * ��ȡѡ�еĽڵ�,�Խڵ��Ӧ��ʵ�������ݲ�ѯ
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Identifier userOb=(Identifier)(node.getUserObject());
		if(userOb==null||(userOb.getType()!=BookMarkPubInfo.NODE_VIEW&&userOb.getType()!=BookMarkPubInfo.NODE_TABLE))
		{
		    return ;
		}
		EntityImpl entity=(EntityImpl)userOb.getDataObject(); //ʵ�����
		Bookmark bookmark=userOb.getBookmark();  //��ǩ����
		
        Operatable operator = null;
        try {
            operator = OperatorFactory
                    .getOperator(com.cattsoft.coolsql.sql.commonoperator.SQLResultProcessOperator.class);
        } catch (Exception e1) {
            LogProxy.internalError(e1);
            return;
        }
        
        String sql="";
        if(bookmark.getAdapter()!=null)
        {
            sql=bookmark.getAdapter().getTableQuery(entity.getQualifiedName());
        }else  //���û����ݿ�������������ô��ʾ��ֱ�ӷ���
        {
            LogProxy.errorMessage("no database adapter object,bookmark:"+bookmark.getAliasName());
            return ;
        }
        
        List argList = new ArrayList();
        argList.add(bookmark);
        argList.add(sql);
        argList.add(new Integer(ResultSetDataProcess.EXECUTE));//����sql��ִ�д������ͣ��״�ִ��
        try {
            operator.operate(argList);
        } catch (UnifyException e2) {
            LogProxy.errorReport(e2);
        } catch (SQLException e2) {
            LogProxy.SQLErrorReport(e2);
        }

    }

}
