/*
 * �������� 2006-6-7
 *
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.search.SearchInfoDialog;

/**
 * @author liu_xlin ������ݿ��е������Դ
 */
public class SearchAction extends CsAction {
	
	private static final long serialVersionUID = 1L;
	
    public SearchAction()
    {
    	super();
    	initMenuDefinitionById("SearchAction");
    }
    /*
     * ���� Javadoc��
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void executeAction(ActionEvent e) {
        if (!hasDatabase()) {
            JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), PublicResource
                    .getSQLString("searchinfo.nodatabase"), "warning", 2);
            return;
        }
        if (!SearchInfoDialog.checkInstanced())
            SearchInfoDialog.getInstance(GUIUtil.getMainFrame());
    }
    /**
     * �Ƿ���������Դ������
     * @return
     */
    public boolean hasDatabase() {
        if (BookmarkManage.getInstance().getBookmarkCount() > 0) {
            return true;
        } else
            return false;
    }
}
