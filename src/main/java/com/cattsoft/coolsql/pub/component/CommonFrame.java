/*
 * �������� 2006-6-2
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.cattsoft.coolsql.action.common.QuitAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.View;


/**
 * @author liu_xlin �Ի��򹫹���
 */
public abstract class CommonFrame extends BaseDialog {
    private JPanel description = null;

    private JPanel buttonPanel = null;

    protected RenderButton preButton = null;

    protected RenderButton nextButton = null;

    protected RenderButton quitButton = null;

    protected RenderButton okButton = null;

    protected View view = null;

    public CommonFrame(View view) {
        super();
        this.view = view;
        initPub(initDialog());
    }

    public CommonFrame(JFrame f, boolean b, View view) {
        super(f, b);
        this.view = view;
        initPub(initDialog());
    }

    public CommonFrame(JDialog d, boolean b, View view) {
        super(d, b);
        this.view = view;
        initPub(initDialog());
    }

    public CommonFrame(JFrame f, String title, boolean b, View view) {
        super(f, title, b);
        this.view = view;
        initPub(initDialog());
    }

    public CommonFrame(JDialog f, String title, boolean b, View view) {
        super(f, title, b);
        this.view = view;
        initPub(initDialog());
    }

    public void initPub(JComponent content) {
        JLabel l = new JLabel();
        l.setOpaque(false);
        l.setBackground(Color.white);

        /**
         * ���ͷ����ʾ��ʾ��Ϣ
         */
        description = new JPanel();
        description.setBackground(Color.white);
        description.setLayout(new BorderLayout());
        description.add("Center", l);
        description.setPreferredSize(new Dimension(400, 50));

        /**
         * ������ť���
         */
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(400, 40));
        addButtons();

        /**
         * ���ݣ�¼����Ϣ����
         */
        JPanel tmp = new JPanel();
        tmp.setLayout(new BorderLayout());
        tmp.add("North", new JSeparator(SwingConstants.HORIZONTAL));
        content.setPreferredSize(getContentSize());
        tmp.add("Center", content);
        tmp.add("South", new JSeparator(SwingConstants.HORIZONTAL));

        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add("North", description);
        pane.add("Center", tmp);
        pane.add("South", buttonPanel);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        if(quitButton!=null)
           this.addWindowListener(new QuitAction(this));
        setDefaultSize();
        this.getRootPane().setDefaultButton(okButton);
        centerToScreen();
    }

    /**
     * �������ͷ����ʾ��Ϣ
     * 
     * @param s
     */
    public void setDescribeText(String s) {
        JLabel l = (JLabel) description.getComponent(0);
        l.setText(s);
    }

    /**
     * �������ͷ��ͼ��
     * 
     * @param icon
     */
    public void setDescribeImage(Icon icon) {
        JLabel l = (JLabel) description.getComponent(0);
        l.setIcon(icon);
    }

    /**
     * ���3��������ť previous next quit
     *  
     */
    private void addButtons() {
        if (displayPreButton()) {
            preButton = new RenderButton(PublicResource
                    .getString("wizardbutton.previous"));
            preButton.setEnabled(false);
            buttonPanel.add(preButton);
        }
        if (displayNextButton()) {
            nextButton = new RenderButton(PublicResource
                    .getString("wizardbutton.next"));
            nextButton.setEnabled(false);
            buttonPanel.add(nextButton);
        }

        okButton = new RenderButton(PublicResource.getString("wizardbutton.finish"));
        okButton.setEnabled(false);
        buttonPanel.add(okButton);

        if (displayQuitButton()) {
            quitButton = new RenderButton(PublicResource
                    .getString("wizardbutton.quit"));

            buttonPanel.add(quitButton);
            quitButton.addActionListener(new QuitAction(this));
        }
    }

    /**
     * �Ƿ���ʾ��һ����ť
     * 
     * @return
     */
    protected boolean displayPreButton() {
        return true;
    }

    /**
     * �Ƿ���ʾ��һ����ť
     * 
     * @return
     */
    protected boolean displayNextButton() {
        return true;
    }

    /**
     * �Ƿ���ʾȡ��ť
     * 
     * @return
     */
    protected boolean displayQuitButton() {
        return true;
    }

    /**
     * ʹ��һ����ť��Ч
     * 
     * @param action
     */
    protected void enablePreButton(Action action, boolean b) {
        if(preButton==null)
            return;
        preButton.setEnabled(b);
        if (action != null)
            preButton.addActionListener(action);
    }

    /**
     * ʹ��һ����ť��Ч
     * 
     * @param action
     */
    protected void enableNextButton(Action action, boolean b) {
        if(nextButton==null)
            return;
        nextButton.setEnabled(b);
        if (action != null)
            nextButton.addActionListener(action);
    }

    /**
     * ʹ��һ����ť��Ч
     * 
     * @param action
     */
    public void enableOkButton(Action action, boolean b) {
        if(okButton==null)
            return;
        okButton.setEnabled(b);
        if (action != null)
            okButton.addActionListener(action);
    }

    /**
     * �Ի����Ĭ�ϴ��ڴ�С
     *  
     */
    public void setDefaultSize() {
        this.setSize(500, 430);
    }

    /**
     * ��д�÷����������Զ������ݴ��ڵĴ�С
     * 
     * @return
     */
    protected Dimension getContentSize() {
        return new Dimension(400, 200);
    }

    /**
     * ������Ļ�м�
     *  
     */
    public void centerToScreen() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) ((d.getWidth() - this.getWidth()) / 2), (int) ((d
                .getHeight() - this.getHeight()) / 2), this.getWidth(), this
                .getHeight());
    }

    /**
     * ����ָ��frame�м�
     * 
     * @param f
     */
    public void centerToFrame(Window f) {
        Rectangle rect = f.getBounds();
        this
                .setBounds((int) (rect.getX() + (rect.getWidth() - this
                        .getWidth()) / 2), (int) (rect.getY() + (rect
                        .getHeight() - this.getHeight()) / 2), this.getWidth(),
                        this.getHeight());
    }

    /**
     * ����ָ��frame�м�
     * 
     * @param f
     */
    public void centerToFrame(JDialog f) {
        Rectangle rect = f.getBounds();
        this
                .setBounds((int) (rect.getX() + (rect.getWidth() - this
                        .getWidth()) / 2), (int) (rect.getY() + (rect
                        .getHeight() - this.getHeight()) / 2), this.getWidth(),
                        this.getHeight());
    }

    /**
     * 
     * ��Ⱦ�ؼ�����,��ѡ��ʹ��
     *  
     */
    private void renderFont() {
        Font f = new Font(PublicResource.getString("default.font.family"),
                Integer
                        .parseInt(PublicResource
                                .getString("default.font.style")),
                Integer.parseInt(PublicResource.getString("default.font.size")));
        UIManager.put("TextField.font", f);
        UIManager.put("Label.font", f);
        UIManager.put("ComboBox.font", f);
        UIManager.put("MenuBar.font", f);
        UIManager.put("Menu.font", f);
        UIManager.put("ToolTip.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("Button.font", f);
    }

    /**
     * ȱʡ���������ã��ݲ���
     * 
     * @return
     */
    public Font getDefaultFont() {
        return new Font(PublicResource.getString("default.font.family"),
                Integer
                        .parseInt(PublicResource
                                .getString("default.font.style")),
                Integer.parseInt(PublicResource.getString("default.font.size")));
    }

    /**
     * @return ���� view��
     */
    public View getView() {
        return view;
    }

    /**
     * ������ɰ�ť�Ķ����ӿڣ�������ݵ��߼�����
     *  
     */
    public void shutDialogProcess(Object ob) {
    }

    /**
     * ������һ����ť�����Ĵ���ӿ�
     * 
     * @param ob
     */
    public void nextButtonProcess(Object ob) {
    }

    /**
     * ������һ����ť�����Ĵ���ӿ�
     * 
     * @param ob
     */
    public void preButtonProcess(Object ob) {
    }

    /**
     * ������ͼ�Ľӿڶ���
     * 
     * @author liu_xlin
     */
    public void processView() {
    }

    /**
     * ��ʼ���Ի�����������ӿ�
     * 
     * @return
     */
    public abstract JComponent initDialog();

    /**
     * ��ȡ��ͼ���ϲ�JFrame����
     * 
     * @param view
     * @return
     */
    public static JFrame getParentFrame(Container view) {
        if (view == null)
            return GUIUtil.getMainFrame();
        Container con;
        for (con = view.getParent(); con != null && !(con instanceof JFrame); con = con
                .getParent())
            ;
        return (JFrame) con;
    }
    /**
     * ��ȡָ�������Ķ��㴰�ڶ���
     * @param com  --ִ������
     * @return   --�����������ڵĴ��ڶ���
     */
    public static Window getTopOwner(Container com)
    {
        Container con;
        for (con = com.getParent(); con != null && !(con instanceof Window); con = con
                .getParent())
            ;
        return (Window)con;
    }
    /**
     * ���type���ͻ�ȡcrrent���ϲ�����
     * @param current
     * @param type  0: JFrame  else:JDialog��ȡ����
     * @return
     */
    public static Container getParentWindow(Container current,int type)
    {
        if (current == null)
            return null;
        Container con;
        for (con = current.getParent(); con != null; con = con
                .getParent())
        {
            if(type==0) //JFrame
            {
                if(con instanceof JFrame)
                    break;
            }else 
            {
                if(con instanceof JDialog)
                    break  ;
            }
        }
            
        return con;
    }
}
