/*
 * �������� 2006-12-19
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 *         ����ؼ��Ҽ�ˢ�²˵���ļ����?����ڽ����ͼ��ˢ��ͼ�갴ť���ô����߼���Ҫ�ж���ʾ��ݵı�ؼ��Ƿ���ʾ�ڿ�ݴ�����
 */
public class EditDataSetTableAction extends DataSetTableAction {

	private static final long serialVersionUID = 1L;

	private int processType = 0;

    private JComponent component;
    /**
     * @param processType
     */
    public EditDataSetTableAction(int processType) {
        this(processType, null);
    }

    /**
     * @param processType
     */
    public EditDataSetTableAction(int processType, JComponent com) {
        super();
        this.component=com;
        this.processType = processType;
    }
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        DataSetPanel pane = null;
        DataSetTable table = getCurrentTable(e);
        if (table == null) {   //����ǲ˵�������¼�
            if (component instanceof DataSetPanel) {
                pane = (DataSetPanel) component;
            } else
                return;
        } else {

            pane = DataSetPanel.getDataPaneByContent(table);
            if (pane == null)
                return;
        }
        List list = new ArrayList();
        list.add(pane);
        list.add(new Integer(processType));

        Operatable operator = null;
        try {
            operator = OperatorFactory
                    .getOperator(com.cattsoft.coolsql.sql.commonoperator.SQLProcessOperator.class);
        } catch (Exception e1) {
            LogProxy.internalError(e1);
        }

        try {
            operator.operate(list);
        } catch (UnifyException e2) {
            LogProxy.errorReport(e2);
        } catch (SQLException e2) {
            LogProxy.SQLErrorReport(e2);
        }
    }
}
