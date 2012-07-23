/**
 * 
 */
package com.cattsoft.coolsql.system.menubuild.menuchecker;

import javax.swing.JComponent;

import com.cattsoft.coolsql.sql.SQLResultSetResults;
import com.cattsoft.coolsql.system.menubuild.MenuItemEnableCheck;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;

/**
 * ����ݱ������� ��ϵͳ�˵��Ŀ�����У��
 * @author kenny liu
 *
 * 2007-11-11 create
 */
public class AddRowsMenuCheck implements MenuItemEnableCheck {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
	 */
	public boolean check() {
		JComponent com=ViewManage.getInstance().getResultView().getDisplayComponent();
		if(com instanceof DataSetPanel)
		{
			DataSetPanel pane=(DataSetPanel)com;
            //У����²˵�
            if( pane.getSqlResult() != null
                    && pane.getSqlResult().isResultSet()
                    && ((SQLResultSetResults) pane.getSqlResult()).getEntities().length == 1)
            {
                return true;
            }else
                return false;
		}else
			return false;
	}

}
