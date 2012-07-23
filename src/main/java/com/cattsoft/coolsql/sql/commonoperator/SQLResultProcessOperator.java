/*
 * �������� 2006-10-13
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.sql.SQLException;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.ResultSetView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.RetriveDataThread;

/**
 * @author liu_xlin
 *ִ��sql��䣬ͬʱ�������Ϣչʾ
 */
public class SQLResultProcessOperator extends BaseOperator  {

    /**ִ��sql��䣬ͬʱ�������Ϣչʾ
     * @param arg  ����Ϊlist���ͣ����ȣ�3
     *           0��BookMark
     *           1: sql(String)
     *           2��Integer ��������
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null||!(arg instanceof List))
        {
            throw new IllegalArgumentException("typeof argument must be java.util.list,error type:"+arg);
        }
        
        List list=(List)arg;
        Bookmark bookmark=(Bookmark)list.get(0);
        String sql=(String)list.get(1);
        int processType=((Integer)list.get(2)).intValue();
        
        ResultSetView view=ViewManage.getInstance().getResultView();
        DataSetPanel com=view.addTab(bookmark);  //���
        RetriveDataThread thread=new RetriveDataThread(com,sql,bookmark,processType);
        com.setCurrentThread(thread);
        thread.start();
        view.setSelectedTab(com);
    }
}
