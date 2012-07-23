/*
 * �������� 2006-9-20
 */
package com.cattsoft.coolsql.view.bookmarkview.actionOfIconButton;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *����ǩ��ͼ�����ڵ�ȫ����£
 */
public class CollapseNodesAction extends PublicAction {
	private static final long serialVersionUID = 8315189701048073667L;
	public CollapseNodesAction(View view)
    {
        super(view);
    }
    /* ���� Javadoc��
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
         JComponent view=this.getComponent();
         if(!(view instanceof BookmarkView))
         {
             LogProxy.messageReport("the type of view is not correct,error type:"+view.getClass(),0);
             return ;
         }
         BookmarkTreeUtil.getInstance().collapseAllNodes();
    }

}
