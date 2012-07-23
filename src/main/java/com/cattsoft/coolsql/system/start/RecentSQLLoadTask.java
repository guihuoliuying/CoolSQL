/*
 * �������� 2006-12-26
 */
package com.cattsoft.coolsql.system.start;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.LoadData;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *װ�����ִ�е�sql��Ϣ
 */
public class RecentSQLLoadTask implements Task {

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.launch.loadrecentsql");
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {       
        try {
            /**
             * װ�����ִ�е�sql���
             */
            LoadData.getInstance().loadRecentSQL();
        } catch (Exception e) {
            LogProxy.errorReport(e);
        }
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
