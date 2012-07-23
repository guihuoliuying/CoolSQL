package com.cattsoft.coolsql.view.resultset;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;


/**
 * 
 * @author liu_xlin ����ݿ�ִ��sql,�Խ�����չʾ,������ҪΪ���Զ��߳������swing����̰߳�ȫ�ϵ�����
 */
public class RetriveDataThread extends Thread {
    private ResultSetDataProcess process;
    
    public RetriveDataThread(DataSetPanel dataPane, String sql,
            Bookmark bookmark, int processType)
    {
        process=new ResultSetDataProcess(dataPane,sql,bookmark,processType);
    }
    /**
     * ��д�̷߳���
     */
    public void run()
    {
        process.process();
    }
}
