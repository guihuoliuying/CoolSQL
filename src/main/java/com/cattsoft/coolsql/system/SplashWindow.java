/*
 * �������� 2006-12-28
 */
package com.cattsoft.coolsql.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *��½ǰ�Ļ�ӭҳ��
 */
public class SplashWindow extends JWindow {

	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar=null;
    
    /**
     * ���ڱ����Ƿ���������ı�־����ֹ���������󣬼����������
     */
    private boolean isStart = false;
    /**
     * ����ͼ��
     */
    private Icon themeIcon=null;
    /**
     * ����������Ҫִ�е�����
     */
    private Vector tasks = null;
    public SplashWindow()
    {
        this(null);
    }
    public SplashWindow(Frame frame)
    {
        this(frame,null,null);
    }
    public SplashWindow(Frame frame,List tasks,Icon themeIcon) {
        super(frame);
        if (tasks != null)
            this.tasks = new Vector(tasks);
        else
            this.tasks = new Vector();
        this.themeIcon=themeIcon;
        initContent();

        pack();
        GUIUtil.centerFrameToFrame(null,this);
    }
    private void initContent()
    {
        JPanel content=(JPanel)getContentPane();
        content.setLayout(new BorderLayout());
        
        /**
         * ������ͼƬ����ڴ��ڵĶ���
         */
        if(themeIcon==null)
            themeIcon=PublicResource.getIcon("system.splash.icon");
        JLabel themeLabel=new JLabel(themeIcon);
        content.add(themeLabel,BorderLayout.NORTH);
        
        /**
         * ��Ӱ汾��Ϣ
         */
        VersionLabel version=new VersionLabel();
        content.add(version,BorderLayout.CENTER);
        
        /**
         * ��ӽ����
         */
        progressBar=new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        content.add(progressBar,BorderLayout.SOUTH);
        
        content.setBackground(new Color(216,233,236));
    }
    /**
     * ���ý�����Ľ����ʾ��Ϣ
     * @param txt
     */
    public void setProgressPrompt(String txt)
    {
        progressBar.setString(txt);
    }
    /**
     * ���ý�����Ľ��ֵ
     * @param value
     */
    public void setProgressValue(int value)
    {
        progressBar.setValue(value);   
    }
    public Vector getAllTask() {
        return tasks;
    }
    public void setTasks(Vector tasks) {
        if(isStart)
            return;
        this.tasks = tasks;
    }
    /**
     * ���������������񳤶�
     * 
     * @param taskLength
     */
    public void setProgressLength(int taskLength) {
        progressBar.setMaximum(taskLength);
    }
    /**
     * ��������
     *  
     */
    public void start() {

        if (!isStart) {
            initTask();
            for (int i = 0; i < tasks.size() && isStart; i++) {
                Object ob = tasks.get(i);
                if (ob instanceof Task) {
                    Task task = (Task) ob;
                    setProgressPrompt(task.getDescribe());
                    task.execute();
                    progressBar.setValue(progressBar.getValue()
                            + task.getTaskLength());
                }
            }
        }

    }
    /**
     * ע��ô���
     */
    public void dispose()
    {
        removeAll();
        if(tasks!=null)
            tasks.clear();
        tasks=null;
        themeIcon=null;
        super.dispose();
    }
    /**
     * ��������ǰ������������ʾ�ĳ�ʼ������
     *  
     */
    private void initTask() {
        isStart = true;
        setProgressValue(0);
        setProgressLength(getTotalLength()); //������������
    }

    /**
     * ��ȡ����������ܳ���
     * 
     * @return --�ܵ�������
     */
    private int getTotalLength() {
        int length = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Object ob = tasks.get(i);
            if (ob instanceof Task) {
                Task task = (Task) ob;
                int len = task.getTaskLength();
                if (len > 0)
                    length += len;
            }
        }
        return length;
    }
}
