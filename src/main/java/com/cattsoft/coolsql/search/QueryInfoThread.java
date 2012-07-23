/*
 * �������� 2006-9-18
 */
package com.cattsoft.coolsql.search;

import com.cattsoft.coolsql.view.log.LogProxy;


/**
 * @author liu_xlin ��ѯ����С�ʵ����Ϣ�̣߳������ѯ���������󣬶��µȴ�ʱ��������
 */
public class QueryInfoThread extends Thread {
    private SearchResultFrame con = null; //��������

    private boolean isRun = true; //�̵߳Ľ����־
    
    private QueryDBInfo query=null; //���ڻ�ȡ��ݿ���Ϣ�Ĵ�����
    public QueryInfoThread(QueryDBInfo query) {
        super();
        this.query=query;
    }

    /**
     * ���ñ������Ĵ���
     * 
     * @param con
     */
    public void setOperateWindow(SearchResultFrame con) {
        this.con=con;
    }

    public void run() {
        while (isRun) {
            pause();
            if (con != null) {
               query.setResultFrame(con);
               try
               {
                  query.query();
               }catch(Exception e)
               {
                   LogProxy.internalError(e);
               }
            }
        }
    }

    /**
     * ʹ�̵߳ȴ�
     *  
     */
    public synchronized void pause() {
        try {
            super.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���Ѹ��߳�
     *  
     */
    public synchronized void launch() {
        super.notify();
    }

    /**
     * ������߳�
     *  
     */
    public synchronized void dispose() {
        isRun = false;
        resetData();
        launch();
    }

    /**
     * @return ���� isRun��
     */
    public boolean isRun() {
        return isRun;
    }
    /**
     * ����������
     *
     */
    public void resetData()
    {
        if(con!=null)
        {
            con.removeAll();
            con=null;
        }
    }
}
