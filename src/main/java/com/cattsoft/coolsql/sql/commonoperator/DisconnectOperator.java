/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.sql.SQLException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *  
 */
public class DisconnectOperator implements Operatable {

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.sql.common.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if (arg == null)
            throw new UnifyException("no operate object!");
        if (!(arg instanceof Bookmark)) {
            throw new UnifyException("operate object error! class:"
                    + arg.getClass());
        }
        Bookmark bookmark = (Bookmark) arg;
        try {
            bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_DISCONNECTING);//ʹ�������ڶϿ�״̬
            bookmark.disconnect();
        } finally {
            bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_NORMAL); //ʹ״̬�ָ���
        }
        LogProxy log = LogProxy.getProxy();
        log.info("database:"+bookmark.getAliasName()+" disconnected!");
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.sql.common.Operatable#operate(java.lang.Object,
     *      java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException, SQLException {

    }

}
