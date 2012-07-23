/*
 * �������� 2006-12-7
 */
package com.cattsoft.coolsql.action.common;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQLDialog;

/**
 * @author liu_xlin
 *sqlִ�е���ʷ��¼
 */
public class SQLHistoryAction extends CsAction {

	private static final long serialVersionUID = 1L;
	public SQLHistoryAction()
    {
        super();
        initMenuDefinitionById("SQLHistoryAction");
    }
    /* ���� Javadoc��
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	@Override
    public void executeAction(ActionEvent e) {
        if(RecentSQLDialog.getDisplayState())  //���������ʾ��ֱ�ӷ���
        {
            return;
        }
        RecentSQLDialog dialog=new RecentSQLDialog(GUIUtil.getMainFrame());
        dialog.setVisible(true);
    }

}
