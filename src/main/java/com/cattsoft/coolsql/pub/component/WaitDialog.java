/*
 * �������� 2006-10-27
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.jidesoft.swing.MultilineLabel;

/**
 * @author liu_xlin ��������ڴ���ʱ�������ʱ�����ôʹ�øöԻ��򣬽��д����ȵ���ʾ
 */
public class WaitDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private JTextArea label = null;

    /**
     * �в����
     */
    private JPanel centerPane = null;

    private RenderButton quit = null;

//    /**
//     * �����˵�ǰ�ȴ�Ի����Ƿ���ʾ
//     */
//    private boolean isDisplayed = false;
//
//    private boolean isLock=false;
    /**
     * �õȴ�Ի����ȡ��ť�������������Ҫִ�еĴ��?�����ڸ�������
     */
    private Vector<Actionable> quitActions = new Vector<Actionable>();

    protected WaitDialog(Frame owner)
    {
        super(owner, PublicResource
                .getString("waitdialog.title"), true);
        initPanel();
    }
    protected WaitDialog(Dialog owner) {
        super(owner, PublicResource
                .getString("waitdialog.title"), true);
        initPanel();
    }
    /**
     * ��ʼ���������
     *
     */
    private void initPanel()
    {
        
        JPanel content = (JPanel) this.getContentPane();  
        content.setLayout(new BorderLayout());

        JPanel displayPanel=new JPanel();
        displayPanel.setLayout(new GridLayout(2,1));
        displayPanel.setBackground(getBackgroundColor());
       
        label = new MultilineLabel();
        
        centerPane = new JPanel();
        centerPane.setLayout(new BorderLayout());
        centerPane.setBackground(getBackgroundColor());
        
//        displayPanel.add(centerPane);
//        displayPanel.add(label);
        content.add(centerPane, BorderLayout.NORTH);
        content.add(label, BorderLayout.CENTER);

        JPanel pane = new JPanel();
        pane.setBackground(getBackgroundColor());
        pane.setLayout(new FlowLayout(FlowLayout.CENTER));
        quit = new RenderButton(PublicResource.getString("waitdialog.button.quit"));
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.setEnabled(false);

                runQuit();

                dispose();
            }

        });
        pane.add(quit);
        content.add(pane, BorderLayout.SOUTH);

        content.setBackground(getBackgroundColor());
        this.setUndecorated(true);//ȥ���װ��
        Border border = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(
                24, 163, 83));
        content.setBorder(border);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 150);
        GUIUtil.centerFrameToFrame(this.getOwner(), this);
        
        displayProgress();
    }
    private Color getBackgroundColor()
    {
        return new Color(218, 221, 222); 
    }
    /**
     * �ȴ�Ի�������ʾ��������
     *  
     */
    private void displayProgress() {
        JProgressBar progress = addProgressBar();

        progress.setStringPainted(true);
        progress.setIndeterminate(true);
        progress.setMaximum(0);
        progress.setValue(0);
    }

//    /**
//     * ���ؽ����
//     *  
//     */
//    public void hiddenProgress() {
//        Component com = null;
//        if (centerPane.getComponentCount() > 0)
//            com = centerPane.getComponent(0);
//        if (com!=null&&com instanceof JProgressBar) {
//            centerPane.removeAll();
//            JPanel pane = (JPanel) this.getContentPane();
//            pane.validate();
//            pane.repaint();
//        }
//    }

    /**
     * ���������������ֵ
     * 
     * @param max
     */
    public void setTaskLength(int max) {
        JProgressBar progress = null;
        Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com!=null&&com instanceof JProgressBar) {
            progress = (JProgressBar) com;
        } else {
            return;
        }
        progress.setMaximum(max);
        progress.setIndeterminate(false);
    }
    /**
     * Get the task length.
     */
    public int getTaskLength()
    {
    	JProgressBar progress = null;
        Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com!=null&&com instanceof JProgressBar) {
            progress = (JProgressBar) com;
        } else {
            return -1;
        }
        
        return progress.getMaximum();
    }
    /**
     * ��ӽ����
     * 
     * @return
     */
    private JProgressBar addProgressBar() {
        JProgressBar progress = null;
        Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com == null || !(com instanceof JProgressBar)) {
            centerPane.removeAll();
            progress = new JProgressBar();
            centerPane.add(progress,BorderLayout.CENTER);

            JPanel pane = (JPanel) this.getContentPane();
            pane.validate();
            pane.repaint();
        }
        return progress;
    }

    /**
     * ���ý�ȣ����ȴ�Ի�����û�м��ؽ��������ô��������
     * 
     * @param value
     */
    public void setProgressValue(int value) {
        JProgressBar progress = null;

        Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com instanceof JProgressBar) {
            progress = (JProgressBar) com;
        } else {
            return;
        }
        if (progress.isIndeterminate())
            return;
        progress.setValue(value);
        progress.setString(value * 100 / progress.getMaximum() + "%");
    }
    /**
     * Return the progress value. Return -1 if progress bar is indeterminate or there is no progress bar in the dialog.
     */
    public int getProgressValue()
    {
    	JProgressBar progress = null;
    	Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com instanceof JProgressBar) {
            progress = (JProgressBar) com;
        } else {
            return -1;
        }
        if(progress.isIndeterminate())
        	return -1;
        
        return progress.getValue();
        
    }
    /**
     * ��д����
     */
    @Override
    public void dispose() {
        removeAll();
        quitActions.clear();
        super.dispose();
    }

    /**
     * ������ʾ��Ϣ
     * 
     * @param info
     */
    public void setPrompt(String info) {
        label.setText(info);
    }

    /**
     * ���ȡ���¼�����
     * 
     * @param action
     */
    public void addQuitAction(Actionable action) {
        if (action != null)
            quitActions.add(action);
    }

    /**
     * ���ȡ��ť����¼�����
     *  
     */
    private void runQuit() {
        for (int i = 0; i < quitActions.size(); i++) {
            Actionable action = (Actionable) quitActions.get(i);
            action.action();
        }
    }
}
