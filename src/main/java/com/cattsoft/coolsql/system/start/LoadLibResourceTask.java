/*
 * �������� 2006-12-27
 */
package com.cattsoft.coolsql.system.start;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.LoadData;
import com.cattsoft.coolsql.system.Task;

/**
 * @author liu_xlin
 *װ���������,���������
 */
public class LoadLibResourceTask implements Task {

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.launch.loaddriverinfo");
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        LoadData.getInstance().loadLibResource();
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {

        return 1;
    }

}
