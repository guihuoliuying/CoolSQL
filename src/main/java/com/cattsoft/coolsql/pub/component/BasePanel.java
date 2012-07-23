/*
 * �������� 2006-6-6
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentListener;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 * 
 * �Զ��幤����� ������Ϊ����ʾ�ÿ���ʹ��16*16��ͼƬ
 */
public class BasePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static Color themeColor = new Color(155, 204, 181);

    private static Color selectionColor = new Color(160, 207, 184);

    /**
     * ���ı�����
     */
    private JPanel top =null;
    private JPanel center = null;

    protected JPanel topLeft = null;

    protected JPanel topRight = null;

    private int heigth = 10;

    public BasePanel() {
        this(null, null, null);
    }

    public BasePanel(String title) {
        this(title, null, null);
    }

    public BasePanel(Icon icon) {
        this(null, icon, null);
    }

    public BasePanel(String title, Icon icon) {
        this(title, icon, null);
    }

    public BasePanel(String title, Icon icon, JComponent com) {
        BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        top = new FancyBgLabel();
        Border b = BorderFactory.createLineBorder(getBorderColor(), 2);
        top.setBorder(b);
        top.setPreferredSize(new Dimension(100, 26));
//        top.setBackground(getThemeColor());
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

        topLeft = new JPanel();
        topLeft.setOpaque(false);
        topLeft.setLayout(new BorderLayout());
//        topLeft.setBackground(getThemeColor());
        JLabel l = null;
        if (title != null) {
            if (icon != null) {
                l = new JLabel(title, new TitleIcon(icon), SwingConstants.LEFT);
            } else
                l = new JLabel(title);
        } else {
            if (icon != null) {
                l = new JLabel(icon, SwingConstants.LEFT);
            } else
                l = new JLabel("");
        }
        topLeft.add(l, BorderLayout.CENTER);
        topRight = new JPanel();
        topRight.setOpaque(false);
        topRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
//        topRight.setBackground(getThemeColor());
        top.add(topLeft);
        top.add(topRight);

        this.add("North", top);
        center = new JPanel();
        center.setLayout(new BorderLayout());
        if (com != null)
            center.add(com, BorderLayout.CENTER);
        this.add("Center", center);
        extraInit();

    }

    public BasePanel(JComponent c) {
        this(null, null, c);
    }

    protected void extraInit() {

    }

    /**
     * ��ȡtop���
     * 
     * @return
     */
    public JPanel getTopPane() {
        return (JPanel) (topLeft.getParent());
    }

    protected Color getBorderColor() {
        return themeColor.brighter().brighter();
    }

    /**
     * ��������
     * 
     * @param s
     */
    public void setTopText(String s) {
        if (topLeft != null && topLeft.getComponents().length > 0) {
            JLabel l = (JLabel) (topLeft.getComponent(0));
            l.setText(s);
        } else {
            JLabel l = new JLabel(s);
            topLeft.add(l, BorderLayout.CENTER);
        }
    }

    /**
     * ����ͼ��
     * 
     * @param icon
     */
    public void setTopTextIcon(Icon icon) {
        if (topLeft != null && topLeft.getComponents().length > 0) {
            JLabel l = (JLabel) (topLeft.getComponent(0));
            l.setIcon(new TitleIcon(icon));
        } else {
            JLabel l = new JLabel(icon);
            topLeft.add(l, BorderLayout.CENTER);
        }
    }

    /**
     * ����������ɫ
     * 
     * @param icon
     */
    public void setTopTextColor(Color c) {
        if (topLeft != null && topLeft.getComponents().length > 0) {
            JLabel l = (JLabel) (topLeft.getComponent(0));
            l.setForeground(c);
        } else {
            JLabel l = new JLabel("");
            l.setForeground(c);
            topLeft.add(l, BorderLayout.CENTER);
        }
    }

    /**
     * ��������
     * 
     * @param f
     */
    public void setTopTextFont(Font f) {
        if (topLeft != null && topLeft.getComponents().length > 0) {
            JLabel l = (JLabel) (topLeft.getComponent(0));
            l.setFont(f);
        } else {
            JLabel l = new JLabel("");
            l.setFont(f);
            topLeft.add(l, BorderLayout.CENTER);
        }
    }

    /**
     * ���ͼ�갴ť
     * 
     * @param icon
     * @return IconButton
     */
    public IconButton addIconButton(Icon icon, Action action, String toolTip) {
        IconButton b = new IconButton(icon);
        b.addActionListener(action);
        String tmp = StringUtil.trim(toolTip);
        if (!tmp.equals(""))
            b.setToolTipText(tmp);

        addIconButton(b);
        
        return b;
    }
    protected void addIconButton(AbstractButton b)
    {
        int count = topRight.getComponentCount();
        topRight.add(b, count-1);
        topRight.validate();
        topRight.repaint();
    }
    /**
     * ������ѡ��ť
     * 
     * @param b
     */
    public void addComponentOnBar(JComponent com) {
        int count = topRight.getComponentCount();
        topRight.add(com, count-1);
        topRight.validate();
        topRight.repaint();
    }

    /**
     * ɾ��ť
     * 
     * @param b
     */
    public void removeIconButton(IconButton b) {
        if (topRight != null && topRight.getComponents().length > 0) {
            Component[] com = topRight.getComponents();
            for (int i = 0; i < com.length; i++) {
                if (com[i] == b) {
                    topRight.remove(b);
                    topRight.validate();
                    repaint();
                    break;
                }
            }
        }
    }

    /**
     * ������������
     * 
     * @param com
     */
    public void setContent(JComponent com) {
        if (center != null) {
            center.removeAll();
        } else
            center = new JPanel();
        center.add(com, BorderLayout.CENTER);
        this.validate();
    }

    /**
     * ����������ɫ
     *
     */
    public void updateThemeColor()
    {
        top.setBackground(getThemeColor());
    }
    /**
     * Ԥ�õ���ɫ
     * 
     * @return
     */
    protected Color getFocusColor() {
        return new Color(73, 164, 47);
    }

    /**
     * Ԥ����ɫ
     * 
     * @return
     */
    protected Color getLostFocusColor() {
        return new Color(186, 232, 172);
    }

    public static Color getThemeColor() {
        return themeColor;
    }

    public static Color getSelectionColor() {
        return selectionColor;
    }

    /**
     * ����������ɫ������ͼ�ı�����ɫ��֮����
     * @param c  --ָ������ɫ����
     */
    public static void setThemeColor(Color c) {
        themeColor = c;
        if(c!=null)
        {
            ViewManage.getInstance().updateViewTheme();
        }
    }

    public static void setSelectionColor(Color c) {
        selectionColor = c;
    }

    /**
     * @return ���� heigth��
     */
    public int getHeigth() {
        return heigth;
    }

    /**
     * @param heigth
     *            Ҫ���õ� heigth��
     */
    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    /**
     * @return ���� center��
     */
    public JPanel getCenter() {
        return center;
    }

    protected void adjustComponent(Color c) {
        topLeft.setBackground(c);
        topRight.setBackground(c);
        Component com[] = topLeft.getComponents();
        for (int i = 0; i < com.length; i++) {
            com[i].setBackground(c);
        }
        com = topRight.getComponents();
        for (int i = 0; i < com.length; i++) {
            com[i].setBackground(c);
        }
    }

    /**
     * @return ���� topLeft��
     */
    public JPanel getTopLeft() {
        return topLeft;
    }

    /**
     * @return ���� topRight��
     */
    public JPanel getTopRight() {
        return topRight;
    }

    /**
     * Ϊ�����������������¼�
     * @param listener
     */
    public void addTopPanelListener(ComponentListener listener)
    {
        addComponentListener(listener);
    }
    /**
     * 
     * @author liu_xlin ����ͼ����
     */
    private class TitleIcon extends ImageIcon {

		private static final long serialVersionUID = 1L;

		public TitleIcon(String s) {
            super(s);
        }

        public TitleIcon(Icon icon) {
            super(((ImageIcon) icon).getImage());
        }

        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            if (this.getImageObserver() == null) {
                g.drawImage(this.getImage(), x, y, getIconWidth(),
                        getIconHeight(), c);
            } else {
                g.drawImage(this.getImage(), x, y, getIconWidth(),
                        getIconHeight(), this.getImageObserver());

            }
        }

        public int getIconWidth() {
            return 16;
        }

        public int getIconHeight() {
            return 16;
        }
    }

}
