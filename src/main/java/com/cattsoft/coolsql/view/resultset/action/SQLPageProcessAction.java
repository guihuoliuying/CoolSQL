/*
 * �������� 2006-10-17
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.component.MyTabbedPane;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.view.ResultSetView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 *��������������ݴ���
 */
public class SQLPageProcessAction extends CsAction {

	private static final long serialVersionUID = 1L;
	
	private static StringManager stringMgr=StringManagerFactory.getStringManager(SQLPageProcessAction.class);
	/**
     * ��������,����com.coolsql.view.resultset.ResultSetDataProcess�Ĵ������Ͷ���һ��
     */
    private int processType;
    private JComponent component;
    public SQLPageProcessAction(int processType)
    {
        this(processType,null);
    }
    public SQLPageProcessAction(int processType,JComponent com)
    {
        super();
        this.processType=processType;
        component=com;
    }
    /**�������
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @SuppressWarnings("unchecked")
    public void executeAction(ActionEvent e) {
        
        List list=new ArrayList();
        
        JComponent component=getProcessObject();
        if(component instanceof DataSetPanel)
        {
        	JComponent content=((DataSetPanel)component).getContent();
        	if(content instanceof DataSetTable)
        	{
        		DataSetTable dsTable=(DataSetTable)content;
        		if(dsTable.hasModified())
        		{
        			
        			boolean result=GUIUtil.getYesNo(GUIUtil.findLikelyOwnerWindow(),
        					stringMgr.getString("resultset.action.changedatamodel.prompt"));
        			if(!result)
        			{
        				return;
        			} else {
        				MyTabbedPane dataTab = ViewManage.getInstance()
						.getResultView().getResultTab();
        				int index = dataTab
						.indexOfComponent((DataSetPanel)component);
        				dataTab.setTabState(index, false);
        			}
        		}
        	}
        }
        
        list.add(component);
        list.add(new Integer(processType));
        
        Operatable operator=null;
        try {
            operator=OperatorFactory.getOperator(com.cattsoft.coolsql.sql.commonoperator.SQLProcessOperator.class);
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

    public int getProcessType() {
        return processType;
    }
    public void setProcessType(int processType) {
        this.processType = processType;
    }
    /**
     * ��ȡ��ǰ��������������
     * @return  --���أ�JComponent������
     */
    private JComponent getProcessObject()
    {
        if(component!=null)
            return component;
        else
        {
            ResultSetView view=ViewManage.getInstance().getResultView();
            MyTabbedPane pane=view.getResultTab();
            return (JComponent)pane.getSelectedComponent();
        }
    }
	/**
	 * @return the component
	 */
	public JComponent getComponent() {
		return this.component;
	}
}
