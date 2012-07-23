/*
 * �������� 2006-10-25
 */
package com.cattsoft.coolsql.system;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 * ���߳���Ҫ�����ӿ�ϵͳ��Դ�Ļ��գ����ػ��̵߳���ʽ����
 */
public class SystemGarbageCollectThread extends Thread {
    /**
     * �ñ������������ػ��̵߳Ľ��������
     */
    private boolean isRun=true;
    
    /**
     * ѭ��������
     */
    private long timeGap=0;
    public SystemGarbageCollectThread(long timeGap)
    {
        super("garbageCollect");
        this.timeGap=timeGap;
        this.setDaemon(true);
        this.setPriority(Thread.NORM_PRIORITY);
    }
    public void run()
    {
        while(isRun)
        {
            try {
                circle();
            } catch (InterruptedException e) {
                LogProxy.internalError(e);
            } catch (UnifyException e) {
                LogProxy.errorReport(e);
            }
        }
    }
    /**
     * ѭ���ȴ�ִ��
     * @throws InterruptedException
     * @throws UnifyException
     */
    private void circle() throws InterruptedException, UnifyException 
    {
//        System.out.println("(1)total:"+(float)Runtime.getRuntime().totalMemory()/1024);
//        System.out.println("(1)free:"+(float)Runtime.getRuntime().freeMemory()/1024);
        collect();
//        System.out.println("(2)total:"+(float)Runtime.getRuntime().totalMemory()/1024);
//        System.out.println("(2)free:"+(float)Runtime.getRuntime().freeMemory()/1024);
        if(getTimeGap()<1000)
            throw new UnifyException("circle period is too short!");
        Thread.sleep(getTimeGap());
        
    }
    private void collect()
    {
        System.gc();//����һ�������������
    }
    /**
     * ��ǰ�̼߳���ֹͣ��־
     *
     */
    public void stopRun()
    {
        if(isRun)
            isRun=false;
    }
    /**
     * @return ���� timeGap��
     */
    public long getTimeGap() {
        return timeGap;
    }
    /**
     * @param timeGap Ҫ���õ� timeGap��
     */
    public void setTimeGap(long timeGap) {
        this.timeGap = timeGap;
    }
}
