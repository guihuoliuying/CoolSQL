/*
 * �������� 2006-12-25
 */
package com.cattsoft.coolsql.system.close;

import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.Task;

/**
 * @author liu_xlin �����������
 */
public class SaveDriverInfo implements Task {

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.closetask.savedriver.describe");
    }

    /**
     * ��������Ϣ
     */
    public void execute() {

        LoadJar.getInstance().writeClasspath();
        LoadJar.getInstance().saveExternalFiles();
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
