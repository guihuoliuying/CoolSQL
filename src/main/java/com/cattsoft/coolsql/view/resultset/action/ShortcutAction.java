/*
 * �������� 2006-12-18
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.pub.component.MyTabbedPane;
import com.cattsoft.coolsql.view.ResultSetView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.ShortcutDialog;

/**
 * @author liu_xlin
 *�������ٲ쿴���ڣ�ͬʱɾ��tab����ĵ�ǰҳ
 */
public class ShortcutAction extends PublicAction {

    public ShortcutAction()
    {
        super(null);
    }
    /* ���� Javadoc��
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        ResultSetView view=ViewManage.getInstance().getResultView();
        MyTabbedPane pane=view.getResultTab();
        Component com=pane.getSelectedComponent();

        if(com instanceof DataSetPanel)
        {
            DataSetPanel dataPane=(DataSetPanel)com;
            dataPane.setRemoving(false);  //������������Ϊ�ǳ���ɾ��
            pane.remove(dataPane);  //�ڽ����ͼ��ɾ�����ҳ
            
            dataPane.setVisible(true);  //����ɾ�������������Ϊ����
            
            //�������ٲ쿴����
            ShortcutDialog shortcustDialog=new ShortcutDialog(dataPane);
            shortcustDialog.toCenter();
            shortcustDialog.setVisible(true);
        }
        
        
    }

}
