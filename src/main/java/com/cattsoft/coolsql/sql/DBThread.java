/*
 * �������� 2006-9-7
 */
package com.cattsoft.coolsql.sql;

import java.sql.SQLException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��ݿ�����ִ�в���ʱʹ�ø��̣߳�������Ϊ������ݿ����ִ��sqlʱ�����½�����ˢ�¡�
 */
public class DBThread extends Thread {
    private Bookmark bookmark = null;

    /**
     * ִ�в������ͣ�0:��ݿ����Ӳ��� 1��ִ��sql
     */
    private int type = 0;


    public DBThread(Bookmark bookmark) {
        this.bookmark = bookmark;
        type = 0;
    }

    public DBThread(Bookmark bookmark, String sql) {
        this.bookmark = bookmark;
        type = 1;
        if (sql == null || sql.trim().equals("")) {
            throw new IllegalArgumentException(
                    " argument:sql value is null or space");
        }
    }

    public void run() {
        if (type == 0) { //��ݿ�����
            try {
                bookmark.setDisplayLabel(bookmark.getAliasName()
                        + "(connecting...)");
                bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_CONNECTING);//��������״̬
                bookmark.connect();   //������ݿ�
                bookmark.setDisplayLabel(bookmark.getAliasName()
                        + "(getting information...)");

                Operatable operator = OperatorFactory
                        .getOperator(com.cattsoft.coolsql.sql.commonoperator.RefreshTreeNodeOperator.class);
                operator.operate(bookmark);
            }catch(SQLException e)
            {
                /**
                 * ������쳣����ǰ�ָ�
                 */
                bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_NORMAL);//�ָ���״̬
                bookmark.setDisplayLabel(bookmark.getAliasName());
                LogProxy.SQLErrorReport(e);
            }
            catch (Exception e) {
                /**
                 * ������쳣����ǰ�ָ�
                 */
                bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_NORMAL);//�ָ���״̬
                bookmark.setDisplayLabel(bookmark.getAliasName());
                LogProxy.errorReport(e);
            } finally {
                if(bookmark.getConnectState()!=0)
                {
                    bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_NORMAL);//�ָ���״̬
                    bookmark.setDisplayLabel(bookmark.getAliasName());
                }
            }

        } else if (type == 1) //ִ��sql
        {

        }
    }
}
