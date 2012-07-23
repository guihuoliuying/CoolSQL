/*
 * Created on 2007-2-5
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author kenny liu
 */
public class ProcessThread extends Thread {

    /**
     * ����ӿڶ���
     */
    private Actionable action;
    public ProcessThread(Actionable action)
    {
        super(StringUtil.getHashNameOfObject(action));
        if(action==null)
            throw new IllegalArgumentException("action must be a non-null value");
        this.action=action;
    }
    public void run()
    {
        try
        {
            if(action!=null)
                action.action();
        }catch(Throwable e)
        {
            LogProxy.errorMessage("error occur in process thread,cause of error: "+e.getMessage());
            LogProxy.outputErrorLog(e);
        }
    }
}
