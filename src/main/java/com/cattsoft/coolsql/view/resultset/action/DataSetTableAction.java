/*
 * �������� 2006-12-19
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.view.ResultSetView;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 *�������еĽ�����չʾ��ؼ��ı༭����
 */
public abstract class DataSetTableAction extends CsAction {

    public DataSetTableAction()
    {
        super();
    }
    /**
     * ����¼�����,��ȡ��ǰ�������ı�ؼ�
     * @param e   --�¼�����
     * @return  --�������ı�ؼ�
     */
    protected DataSetTable getCurrentTable(ActionEvent e)
    {
        if(!(e.getSource() instanceof JMenuItem))
        	return null;
        
        JPopupMenu popMenu=GUIUtil.getTopMenu((JMenuItem)e.getSource());
        if(popMenu==null)
            return null;
        
        DataSetTable table=(DataSetTable)popMenu.getClientProperty(ResultSetView.DataTable);
        if(table==null)
        {
            LogProxy.errorMessage("can't get data table object!");
            return null;
        }
        
        return table;
    }
}
