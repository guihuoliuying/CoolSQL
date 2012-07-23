/*
 * �������� 2006-12-26
 */
package com.cattsoft.coolsql.system.start;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *�����ڽ����ʼ��
 */
public class MainFrameInitTask implements Task {

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        
        return PublicResource.getString("system.launch.initmainframe");
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
    	GUIUtil.getMainFrame();
    	for(View v:ViewManage.getInstance().getViews())
    	{
    		try {
				v.doAfterMainFrame();
			} catch (Exception e) {
				LogProxy.errorReport("processing view failed:"+e.getMessage(), e);
			}
    	}
    }

    /* ���� Javadoc��
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 2;
    }

}
