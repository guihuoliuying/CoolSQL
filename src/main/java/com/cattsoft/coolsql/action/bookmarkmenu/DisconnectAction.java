/*
 * �������� 2006-6-7
 *
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.component.CommonFrame;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.View;


/**
 * @author liu_xlin
 *�Ͽ�����ݿ������
 */
public class DisconnectAction extends PublicAction {
    public DisconnectAction(View view)
    {
       super(view);	
    }
	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	    /**
	     * ��ȡ��ѡ��ǩ�ڵ�
	     */
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Object userOb=node.getUserObject();
		
		/**
		 * �����ѡ�ڵ�Ϊ��ǩ�ڵ㣬�Ͽ�����
		 */
		if(userOb instanceof Bookmark)
		{
		    Operatable operator;
            try {
                operator = OperatorFactory.getOperator(com.cattsoft.coolsql.sql.commonoperator.DisconnectOperator.class);
                operator.operate(userOb);
                BookmarkManage.getInstance().nextConnectedBookmarkAsDefault();
            } catch(Exception e1)
            {
                JOptionPane.showMessageDialog(CommonFrame.getParentFrame(getComponent()),e1.getMessage(),"error",0); 
            }
            
		}
	}

}
