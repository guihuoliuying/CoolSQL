/*
 * �������� 2006-12-26
 */
package com.cattsoft.coolsql.system.start;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.LoadData;
import com.cattsoft.coolsql.system.Task;

/**
 * @author liu_xlin
 *װ����ǩ��Ϣ
 */
public class LoadBookmarkInfoTask implements Task {

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.launch.loadbookmarkinfo");
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        LoadData.getInstance().loadBookmarks();
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
