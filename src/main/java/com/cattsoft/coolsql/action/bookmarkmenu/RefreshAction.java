/*
 * �������� 2006-6-7
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JTree;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.log.LogProxy;


/**
 * @author liu_xlin
 * ��ǩ���ڵ��ˢ���¼�����
 */
public class RefreshAction extends PublicAction {
	private static final long serialVersionUID = 1L;
	
	public RefreshAction(BookmarkView view)
    {
       super(view);	
    }
	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	    JComponent com=(JComponent)e.getSource();
	    if(!com.isEnabled())   //�����ã�ֱ�ӷ���
	        return ;
	    
	    JTree tree=((BookmarkView) getComponent()).getConnectTree();
		DefaultTreeNode node = (DefaultTreeNode) (tree.getLastSelectedPathComponent());
		Identifier userOb=(Identifier)(node.getUserObject());
		
		if(!isValidate(userOb))
		    return ;
		
		tree.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
            userOb.refresh(node,node.getNodeFilter());
        } catch (SQLException e1) {
            LogProxy.SQLErrorReport(e1);
        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
        }finally
        {
            tree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

	}
	/**
	 * У��ڵ�ˢ�µ���Ч��
	 * 1�����Ϊ��ǩ��ڵ�֮�ϵĽڵ㣨��ڵ㣩��������ˢ��
	 * 2�����ѡ�нڵ�������ǩ���ڲ���״̬��������ǩδ�������ӣ�������ˢ��
	 * @param node  --�ڵ���ݶ���
	 * @return  --true:��Ч  false:��Ч
	 */
	private boolean isValidate(Identifier node)
	{
	    if(node.getType()==0)
	        return false;
	        
	    Bookmark bookmark=node.getBookmark();
	    if(bookmark==null)
	        return false;
        if (bookmark.getConnectState() == 0) //�����ǩ�ڵ�״̬��
        {
            //����ˢ�²˵��Ƿ����
            if (bookmark.isConnected()) {
                return true;
            } else {
                return false;
            }
        }else
            return false;
	}
}
