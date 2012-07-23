/*
 * �������� 2006-12-21
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToolBar;


/**
 * @author liu_xlin
 *�Զ��幤������
 */
public class DefinableToolBar extends JToolBar {

    public DefinableToolBar()
    {
        super();
        initBar();
    }
    public DefinableToolBar(int orientation)
    {
        super(orientation);
        initBar();
    }
    public DefinableToolBar(String name)
    {
        super(name);
        initBar();
    }
    public DefinableToolBar(String name,int orientation)
    {
        super(name,orientation);
        initBar();
    }
    protected void initBar()
    {
        setBackground(DisplayPanel.getThemeColor());
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorderPainted(true);
        setFloatable(true);
        setRollover(true);
        setMargin(new Insets(0,0,0,0));
    }
    /**
     * �򹤾�����Ӱ�ť���ð�ť���������߼�������action����
     * @param action  --��������Ĵ����߼�
     * @param btn  --����ӵİ�ť
     * @param tip  --��ť��ʾ��Ϣ
     * @return --���ر���ӵ�ͼ�갴ť
     */
    public IconButton addIconButton(Icon icon,Action action,String tip)
    {
        IconButton btn=new IconButton(icon);       
        btn.addActionListener(action);
        btn.setToolTipText(tip);
        super.add(btn);
        return btn;
    }
}
