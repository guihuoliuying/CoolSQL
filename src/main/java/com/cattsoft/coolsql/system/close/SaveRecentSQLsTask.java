/*
 * �������� 2006-12-25
 */
package com.cattsoft.coolsql.system.close;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.system.DoOnClosingSystem;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *�������ִ�е�sql��Ϣ
 */
public class SaveRecentSQLsTask implements Task {

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        
        return PublicResource.getString("system.closetask.saverecentsqls.describe");
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        try {
            /**
             * ����ִ�е�sql��Ϣ
             */
            DoOnClosingSystem.getInstance().saveRecentSQL();
            DoOnClosingSystem.getInstance().deleteNoUseFile();
            
        } catch (XMLException e1) {
            LogProxy.errorReport(e1);
        } catch (Exception e) {
            LogProxy.outputErrorLog(e);
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
