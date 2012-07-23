/*
 * Created on 2007-4-26
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.cattsoft.coolsql.pub.display.FindProcessConfig;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.StatusBar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin �����Ͷ�Ӧһ���Ի���������Ӧ���������Ϣ���Ҵ��?
 */
public class FindFrame extends BaseDialog {

    private static List historylist = Collections
            .synchronizedList(new ArrayList());

    /**
     * ��ǰ�Ĳ��Ҵ��ڣ��ò��Ҵ���ͬʱֻ��ʵ��һ�����������Ѿ�ʵ��Ĳ��Ҵ��ڣ����Ὣ���ڵĴ��ڹرգ����´��µĴ��ڡ�
     */
    private static FindFrame currentFrame = null;

    /**
     * �ò��Ҵ����ж�Ӧ�Ĳ���ѡ�񣬲���ѡ��ֱ��ӳ�䵽�ö�����
     */
    private FindProcessConfig findConfig;

    /**
     * ���ҹ�̴������
     */
    private FindProcess findProcess;

    /**
     * GUI�������
     */
    private RecordEditCombBox keyword;

    private JCheckBox caseArg; //��Сдѡ��

    private JCheckBox matchMode; //ƥ��ģʽ���Ƿ�ȫ��ƥ��

    private JCheckBox isCircle; //�Ƿ��������

    private JRadioButton forward; //��ǰ����

    private JRadioButton back; //�������

    private StatusBar status;//״̬�������ڶԲ��ҽ�������
    /**
     * �����ҵ��������
     */
    private Component target = null;

    /**
     * ��ȡ���Ҵ��ڶ���ʵ��
     * @param window --������
     * @param title  --����
     * @param target --�����ҵ����
     * @param findProcess  --���Ҵ�����
     * @return
     */
    public static FindFrame getFindFrameInstance(Window window, String title,
            Component target, FindProcess findProcess) {
        if (currentFrame != null) {

            currentFrame.closeFrame();
        }
        currentFrame=null;
        if (window instanceof Frame)
            currentFrame = new FindFrame((Frame) window, title, target,
                    findProcess);
        else if (window instanceof Dialog)
            currentFrame = new FindFrame((Dialog) window, title, target,
                    findProcess);
        else
            throw new IllegalArgumentException(
                    "parent Window must be frame or dialog,error type:"
                            + window.getClass().getName());
        return currentFrame;
    }

    private FindFrame(Frame frame, String title, Component target,
            FindProcess findProcess) {
        super(frame, title);
        this.target = target;
        initGUI(findProcess);
    }

    private FindFrame(Dialog dialog, String title, Component target,
            FindProcess findProcess) {
        super(dialog, title);
        this.target = target;
        initGUI(findProcess);
    }

    /**
     * ��ʼ��ͼ�β���
     *  
     */
    protected void initGUI(FindProcess findProcess) {
        JPanel main = (JPanel) getContentPane();
        main.setLayout(new BorderLayout());
        
        JPanel inputPane=new JPanel();
        inputPane.setLayout(new BorderLayout());
        
        inputPane.add(createInputPane(), BorderLayout.NORTH); //����������

        inputPane.add(createArgumentPane(), BorderLayout.CENTER);//����ѡ�����

        inputPane.add(createButtonPane(), BorderLayout.SOUTH);

        main.add(inputPane,BorderLayout.CENTER);
        
        status=new StatusBar();
        status.setToolTipText(PublicResource.getUtilString("findinfo.frame.status.tip"));
        main.add(status,BorderLayout.SOUTH);  //���״̬��
        
        findConfig = new FindProcessConfig();
        this.findProcess = findProcess;

        pack();
        setResizable(false);
        initSelect();
    }

    /**
     * ��ʼ�������пؼ�ѡ�����
     *  
     */
    protected void initSelect() {
        forward.setSelected(true);

        /**
         * װ����һ�α���Ĺؼ����б�
         */
        for (int i = 0; i < historylist.size(); i++) {
            keyword.addItem(historylist.get(i));
        }
    }

    /**
     * �����ؼ����������
     * 
     * @return
     */
    private JPanel createInputPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = GUIUtil.getDefaultBagConstraints();

        pane.add(new JLabel(PublicResource
                .getUtilString("findinfo.frame.keyword.label")), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        keyword = new RecordEditCombBox();
        pane.add(keyword, gbc);

        return pane;
    }

    /**
     * ��������ѡ�����
     * 
     * @return
     */
    private JPanel createArgumentPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 2));

        //ѡ�����
        JPanel argPane = new JPanel();
        argPane.setLayout(new GridLayout(3, 1));
        caseArg = new JCheckBox(PublicResource
                .getUtilString("findinfo.frame.case.label"));
        argPane.add(caseArg);
        matchMode = new JCheckBox(PublicResource
                .getUtilString("findinfo.frame.matchmode.label"));
        argPane.add(matchMode);
        isCircle = new JCheckBox(PublicResource
                .getUtilString("findinfo.frame.iscircle.label"));
        argPane.add(isCircle);

        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        border = BorderFactory.createTitledBorder(border, PublicResource
                .getUtilString("findinfo.frame.argselect.label"));
        argPane.setBorder(border);

        //�������
        JPanel directionPane = new JPanel();
        directionPane.setLayout(new GridLayout(3, 1));
        forward = new JRadioButton(PublicResource
                .getUtilString("findinfo.frame.forward.label"));
        back = new JRadioButton(PublicResource
                .getUtilString("findinfo.frame.back.label"));
        ButtonGroup group = new ButtonGroup();
        group.add(forward);
        group.add(back);
        directionPane.add(forward);
        directionPane.add(back);
        directionPane.add(new JPanel());

        border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        border = BorderFactory.createTitledBorder(border, PublicResource
                .getUtilString("findinfo.frame.direction.label"));
        directionPane.setBorder(border);

        pane.add(directionPane);
        pane.add(argPane);

        return pane;
    }

    /**
     * ������ť���
     * 
     * @return
     */
    private JPanel createButtonPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout());
        RenderButton findBtn = new RenderButton(PublicResource
                .getUtilString("findinfo.frame.findbtn.label"));
        findBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                status.setText("");
                updateConfig();
                if (findConfig.getKeyWord().equals("")) {
                    LogProxy.message(FindFrame.this, PublicResource
                            .getUtilString("findinfo.frame.nokeyword"),
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    if (!findProcess.find(findConfig, target)) {
                        status.setText(PublicResource.getUtilString("findinfo.noresult"));
                        LogProxy.message(FindFrame.this, PublicResource
                                .getUtilString("findinfo.frame.nofind"),
                                JOptionPane.WARNING_MESSAGE);
                    } else
                    {
                        status.setText(findProcess.resultInfo());
                        keyword.record();
                    }
                } catch (Exception e1) {
                    status.setText(PublicResource.getUtilString("findinfo.noresult"));
                    LogProxy.errorReport(FindFrame.this, e1);
                }
            }

        });
        RenderButton quitBtn = new RenderButton(PublicResource
                .getUtilString("findinfo.frame.quitbtn.label"));
        quitBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeFrame();
            }

        });
        pane.add(findBtn);
        pane.add(quitBtn);
        return pane;
    }

    /**
     * �رմ���
     *  
     */
    public void closeFrame() {
        //������ҹؼ����б�
        historylist.clear();
        for(int i=0;i<keyword.getItemCount();i++)
        {
            historylist.add(keyword.getItemAt(i));         
        }
        
        removeAll();
        dispose();
    }

    /**
     * ��������ѡ����������Խ�����ѡ���Ҫ����в���
     *  
     */
    public void updateConfig() {
    	Object selectedObject=keyword.getSelectedItem();
        findConfig.setKeyWord(selectedObject==null?"":selectedObject.toString());
        //�������
        if (forward.isSelected()) {
            findConfig.setForward(FindProcessConfig.FORWARD);
        } else if (back.isSelected())
            findConfig.setForward(FindProcessConfig.BACKFORWARD);

        //case
        findConfig
                .setCaseMatch(caseArg.isSelected() ? FindProcessConfig.SENSITIVECASE
                        : FindProcessConfig.IGNORECASE);

        //matchmode
        findConfig
                .setMatchMode(matchMode.isSelected() ? FindProcessConfig.MATCH_FULL
                        : FindProcessConfig.MATCH_PART);

        //isCircle
        findConfig.setCircle(isCircle.isSelected());
    }
}
