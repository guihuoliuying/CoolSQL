/*
 * �������� 2006-12-1
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.sql.SQLException;

import com.cattsoft.coolsql.gui.property.database.SQLProperty;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQL;

/**
 * @author liu_xlin
 *�鿴���ִ��sql������
 */
public class SQLPropertyOperator implements Operatable {

    /* ���� Javadoc��
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
            throw new UnifyException("no argument");
        if(!(arg instanceof RecentSQL))
            throw new UnifyException("class type of argument is incorrect:"+arg.getClass());

        RecentSQL sqlData=(RecentSQL)arg;
        SQLProperty propertyDialog=new SQLProperty(sqlData);
        propertyDialog.setVisible(true);
    }

    /* ���� Javadoc��
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object, java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException,
            SQLException {
    }

}
