/*
 * �������� 2006-10-20
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.sql.SQLException;
import java.util.List;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.RetriveDataThread;

/**
 * @author liu_xlin
 * ���Ѿ�ִ�й��sql�����Ϣ������صı༭����
 */
public class SQLProcessOperator extends BaseOperator {

    /**�����ݵĴ��������
     * @param arg List
     * 0 : DataSetPanel  ������
     * 1 : int  ��������
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
            return;
        if(!(arg instanceof List))
            throw new UnifyException("the type of argument is error,erorr type:"+arg.getClass());
        
        List list=(List)arg;
        DataSetPanel dataPane=(DataSetPanel)list.get(0);
        int processType=((Integer)list.get(1)).intValue();

        RetriveDataThread thread=new RetriveDataThread(dataPane,dataPane.getSql(),dataPane.getBookmark(),processType);
        dataPane.setCurrentThread(thread);
        thread.start();
    }
}
