/*
 * �������� 2006-12-13
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.cattsoft.coolsql.pub.display.exception.NotRegisterException;

/**
 * @author liu_xlin
 *�ȴ�Ի���Ĺ�����
 */
public class WaitDialogManage {

    /**
     * ���ö���ʵ����ڴ���
     */
    private static WaitDialogManage manage=null;
    
    /**
     * ��������ĶԻ�����Ϣ
     * key:current thread     value:waiterDialog
     */
    private Map map=null;
    private WaitDialogManage()
    {
        map=Collections.synchronizedMap(new HashMap());
    }
    public synchronized static WaitDialogManage getInstance()
    {
        if(manage==null)
        {
            manage=new WaitDialogManage();
        }
        return manage;
    }
    /**
     * ��ȡ��ǰ�̶߳�Ӧ�ĵȴ�Ի���
     * @throws NotRegisterException  --����߳�û��ע�ᣬ���׳����쳣
     * @return --�ȴ�Ի���ʵ��
     */
    public WaitDialog getDialogOfCurrent() throws NotRegisterException
    {
        Thread th=Thread.currentThread();
        WaitDialog dialog=(WaitDialog)map.get(th);
        if(dialog==null)
            throw new NotRegisterException("current thread haven't registered to waiter dialog manager");
        
        return dialog;
    }
    /**
     * ע�ᵱǰ�̶߳�Ӧ�ĵȴ�Ի���,ֻ��ע�����ܷ���Ļ�ȡ�ȴ�Ի����ʵ��
     * @param th  --��ע����̶߳���
     * @param owner  --�ȴ�Ի���ĸ�����
     * @return   --WaitDialog ���͵ĵȴ�Ի���
     */
    public WaitDialog register(Thread th,Window owner)
    {
        WaitDialog dialog=getWaiterDialog(owner);
        map.put(th,dialog);
        return dialog;
    }
    public WaitDialog register(Window owner)
    {
       return register(Thread.currentThread(),owner);
    }
    /**
     * ��ȡ�ȴ�Ի���ʵ��
     * @param owner  --�ȴ�Ի���ĸ�����
     * @return  --�ȴ�Ի����
     */
    private WaitDialog getWaiterDialog(Window owner)
    {
        if(owner instanceof Frame)
            return new WaitDialog((Frame)owner);
        else if(owner instanceof Dialog)
            return new WaitDialog((Dialog)owner);
        else
            throw new IllegalArgumentException("owner of wait dialog must be frame or dialog��error owner:"+owner.getClass());
    }
    /**
     * ע���̵߳ķ���
     * @param th  --ָ�����̶߳���
     * @return   --��ɾ���ֵ
     */
    public WaitDialog disposeRegister(Thread th)
    {
        return (WaitDialog)map.remove(th);
    }
}
