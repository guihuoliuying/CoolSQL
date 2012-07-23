/*
 * Created on 2007-1-17
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin ���½����ͼ��ĳһ��ѯ����е�һ����¼��
 */
public class ModifyDataAction extends DataSetTableAction {
	private static final long serialVersionUID = 1L;
	/**
     * �������͵Ķ���
     */
    public final static int INSERT=0;  //����
    public final static int UPDATE=1;  //����
    
    private int operatorType;
    public ModifyDataAction(int operatorType) {
        super();
        this.operatorType=operatorType;
    }

    /*
     * 1����ȡ�������еĽ����� 2����������ʵ���Ƿ���Ч���Ƿ�Ϊnull���Ƿ�ֻ��һ��ʵ�壩
     * 3��������ѡ����У������?ѡ����
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        DataSetPanel pane = null;
        DataSetTable table = getCurrentTable(e);
//        DataSetTable table=ViewManage.getInstance().getResultView().getDisplayComponent().
        if (operatorType==UPDATE&&table.getSelectedRowCount() < 1) {
            JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), PublicResource
                    .getSQLString("rowupdate.table.noselect"), "warning", 1);
            return;
        }else if(operatorType==UPDATE&&table.getSelectedRowCount()>1)
        {
            JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), PublicResource
                    .getSQLString("rowupdate.table.selectmany"), "warning", 1);
            return;
        }
        pane = DataSetPanel.getDataPaneByContent(table);

        ActionCommand command=new ModifyDataCommand(pane,operatorType);
        try {
			command.exectue();
		} catch (Exception e1) {
			LogProxy.errorReport(e1);
		}
    }
   public DataSetTable getCurrentTable(ActionEvent e)
   {
	   JComponent c=ViewManage.getInstance().getResultView().getDisplayComponent();
	   if(c instanceof DataSetPanel)
	   {
		   return (DataSetTable)((DataSetPanel)c).getContent();
		   
	   }else
		   return null;
   }
}
