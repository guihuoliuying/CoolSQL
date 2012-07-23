/*
 * �������� 2006-12-1
 */
package com.cattsoft.coolsql.system;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin ����չʾΪһ���Ի��� �ڽ������ʱ��������ʾ�ر���Դ�Ľ����Ϣ
 */
public class CloseProgressDialog extends BaseDialog implements SystemProcess {
    private JLabel label = null; //������ʾ��ǰ����ִ�еĶ���

    private JProgressBar progressBar = null; //����ִ�еĽ����

    /**
     * ���ڱ����Ƿ���������ı�־����ֹ���������󣬼����������
     */
    private boolean isStart = false;

    /**
     * ����������Ҫִ�е�����
     */
    private Vector tasks = null;

    public CloseProgressDialog() {
        this(null);

    }

    public CloseProgressDialog(List tasks) {
        super(GUIUtil.getMainFrame(), PublicResource
                .getString("system.closedialog.title"), false);
        if (tasks != null)
            this.tasks = new Vector(tasks);
        else
            this.tasks = new Vector();
        initialize();

    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new GridLayout(0, 1));

        //�ı���ʾ���
        JPanel txtPane = new JPanel();
        txtPane.setLayout(new FlowLayout(SwingConstants.CENTER));
        label = new JLabel();
        label.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        txtPane.add(label);
        pane.add(txtPane);

        //������������
        JPanel progressPane = new JPanel();
        progressPane.setLayout(new FlowLayout(SwingConstants.CENTER));
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(480, 26));
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(false);
        setProgressValue(0);
        setProgressLength(getTotalLength()); //������������

        progressPane.add(progressBar);
        pane.add(progressPane);

        setSize(500, 159);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int result=JOptionPane.showConfirmDialog(CloseProgressDialog.this, PublicResource
                        .getString("system.closedialog.forcecloseconfirm"),
                        "force close confirm", JOptionPane.YES_NO_OPTION);
                if(result==JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * ������ʾ��Ϣ
     * 
     * @param info
     *            --��Ҫ��ʾ���ı���Ϣ
     */
    public void setPrompt(String info) {
        label.setText(info);
    }

    /**
     * ������������Ľ��ֵ
     * 
     * @param value
     *            --���ֵ
     */
    public void setProgressValue(int value) {
        progressBar.setValue(value);
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
     * ��ȡ��ǰ�����Ƿ��Ѿ���������
     * 
     * @return --true:�Ѿ����� false:δ����
     */
    public boolean isLaunched() {
        return isStart;
    }

    /**
     * �������
     * 
     * @param task
     *            --��Ҫִ�е�����
     */
    public void addTask(Task task) {
        if (task != null && !isStart)
            tasks.add(task);
    }

    /**
     * �����µ����񼯺ϣ���ԭ�������񼯺��滻��
     * 
     * @param list
     *            --�µ������б�
     */
    public void setTasks(List list) {
        if (!isStart && list != null) {
            tasks.clear();
            tasks = new Vector(list);
        }
    }

    /**
     * ɾ��ָ��������
     * 
     * @param task
     *            --��Ҫɾ�������
     * @return --���ɾ��ɹ�������true�����򷵻�false
     */
    public boolean removeTask(Task task) {
        if (!isStart && task.getTaskLength() > 0)
            return tasks.remove(task);
        else
            return false;
    }

    /**
     * ��������
     *  
     */
    public void start() {

        if (!isStart) {
            try {
                initTask();
                for (int i = 0; i < tasks.size() && isStart; i++) {
                    Object ob = tasks.get(i);
                    if (ob instanceof Task) {
                        Task task = (Task) ob;
                        setPrompt(task.getDescribe());
                        task.execute();
                        progressBar.setValue(progressBar.getValue()
                                + task.getTaskLength());
                    }
                }
            } finally {
                this.dispose();
                System.exit(0);
            }
        }

    }

    /**
     * ֹͣ�����ִ��
     *  
     */
    public void stop() {
        isStart = false;
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

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.system.SystemProcess#getDescribe()
     */
    public String getDescribe() {
        return "close system";
    }
}
