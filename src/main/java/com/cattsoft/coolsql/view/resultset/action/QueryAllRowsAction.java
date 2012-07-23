/*
 * �������� 2006-10-25
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.pub.component.MyTabbedPane;
import com.cattsoft.coolsql.sql.SQLResults;
import com.cattsoft.coolsql.sql.SQLStandardResultSetResults;
import com.cattsoft.coolsql.view.ResultSetView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin
 *��ѯ��������ݵ��¼�����
 */
public class QueryAllRowsAction extends SQLPageProcessAction {

	private static final long serialVersionUID = 1L;
	/**
     * @param table
     */
    public QueryAllRowsAction() {
        super(ResultSetDataProcess.REFRESH);
        initMenuDefinitionById("QueryAllRowsAction");
    }
    public void executeAction(ActionEvent e)
    {
        ResultSetView view=ViewManage.getInstance().getResultView();
        MyTabbedPane pane=view.getResultTab();
        DataSetPanel dataPane=(DataSetPanel)pane.getSelectedComponent();
        if(!dataPane.isReady())  //��������������ݲ�����Ϊ��ؼ�,��ô��������
        {
            return;
        }
        SQLResults result=dataPane.getSqlResult();
        if(result.isResultSet())
        {
            ((SQLStandardResultSetResults)result).setFullMode(true);
        }
        
        super.executeAction(e);
    }

}
