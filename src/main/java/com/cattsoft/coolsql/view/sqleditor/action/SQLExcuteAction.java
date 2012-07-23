/*
 * �������� 2006-10-16
 */
package com.cattsoft.coolsql.view.sqleditor.action;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.commonoperator.SQLScriptExecuteOperator;
import com.cattsoft.coolsql.sql.execute.IMultiStatementExecute;
import com.cattsoft.coolsql.sql.execute.MultiStatementExecute;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menu.action.BaseEditorSelectAction;
import com.cattsoft.coolsql.view.SqlEditorView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin
 * sqlִ�еĴ����࣬������sqlִ�д����߳���ʵ�ָù���
 */
public class SQLExcuteAction extends BaseEditorSelectAction {

	private static final long serialVersionUID = 1L;
	public SQLExcuteAction(){
		super(true);
		initMenuDefinitionById("SQLExcuteAction");
	}
    /* ���� Javadoc��
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	@Override
    public void executeAction(ActionEvent e) {
        SqlEditorView view=ViewManage.getInstance().getSqlEditor();
        Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
        if(bookmark==null)
        {
            LogProxy.infoMessage(PublicResource.getSQLString("sql.execute.nobookmark"));
            return;
        }
        if(!bookmark.isConnected())
        {
            LogProxy.errorMessage(PublicResource.getSQLString("database.notconnected")+bookmark.getAliasName());
            return;
        }
        List list= view.getQueries();
        
        if(list.size()<1)
        {
            LogProxy.message(PublicResource.getSQLString("sql.execute.nosql"),JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(bookmark==null)
        {
            LogProxy.message(PublicResource.getSQLString("sql.execute.nobookmark"),JOptionPane.WARNING_MESSAGE);
            return;
        }
        int scriptThreshold=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SCRIPT_SQLTHRESHOLD, 2);
        if(list.size()>=scriptThreshold)
        {
        	JCheckBox checkbox = new JCheckBox("Don't show this dialog again.");
    		checkbox.setMnemonic('D');
        	String message=PublicResource.getSQLString("sql.execute.scriptprompt", new Object[]{list.size(),scriptThreshold})+"\n";
        	int result=JOptionPane.showConfirmDialog(GUIUtil.findLikelyOwnerWindow(), message,
        			"confirm",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        	if(result==JOptionPane.CANCEL_OPTION)
        		return;
        	else if(result==JOptionPane.YES_OPTION)
        	{
        		Operatable operator;
        		try {
					operator=OperatorFactory.getOperator(SQLScriptExecuteOperator.class);
				} catch (Exception e1) {
					LogProxy.errorReport("create sqlOperator error!"+e1.getMessage(), e1);
					return;
				}
				IMultiStatementExecute se=new MultiStatementExecute(bookmark,list);
        		try {
					operator.operate(se);
				} catch (UnifyException e2) {
	                LogProxy.errorReport(e2);
	            } catch (SQLException e2) {
	                LogProxy.SQLErrorReport(e2);
	            }
	            return;
        	}
        }
        
        Operatable operator = null;
        try {
            operator = OperatorFactory
                    .getOperator("com.coolsql.sql.commonoperator.SQLResultProcessOperator");
        } catch (Exception e1) {
            LogProxy.internalError(e1);
            return;
        }
        for(int i=0;i<list.size();i++)
        {
            List<Object> argList = new ArrayList<Object>();
            argList.add(bookmark);
            argList.add(list.get(i));
            argList.add(new Integer(ResultSetDataProcess.EXECUTE));//����sql��ִ�д������ͣ��״�ִ��
            try {
                operator.operate(argList);
            } catch (UnifyException e2) {
                LogProxy.errorReport(e2);
            } catch (SQLException e2) {
                LogProxy.SQLErrorReport(e2);
            }
        }

    }

}
