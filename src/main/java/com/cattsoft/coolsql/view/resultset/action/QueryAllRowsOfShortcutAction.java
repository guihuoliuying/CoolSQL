/*
 * �������� 2006-12-21
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.KeyStroke;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.sql.SQLResults;
import com.cattsoft.coolsql.sql.SQLStandardResultSetResults;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin
 *�ڿ��ٲ쿴�����в쿴�����еĴ����߼�
 */
public class QueryAllRowsOfShortcutAction extends SQLPageProcessAction {
	
	private static final long serialVersionUID = 1L;
	
	public QueryAllRowsOfShortcutAction(DataSetPanel dataPane) {
        super(ResultSetDataProcess.REFRESH,dataPane);
        initMenuDefinitionById("QueryAllRowsAction");
        CsAction baseAction=Setting.getInstance().getShortcutManager().getActionByClass(QueryAllRowsAction.class);
        baseAction.addPropertyChangeListener(new PropertyChangeListener()
        {
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals(Action.ACCELERATOR_KEY))
				{
					setAccelerator((KeyStroke)evt.getNewValue());
				}
			}
        	
        }
        );
    }
    public void executeAction(ActionEvent e)
    {
        if(!(getComponent() instanceof DataSetPanel))
            return;
        
        DataSetPanel dataPane=(DataSetPanel)getComponent();
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
