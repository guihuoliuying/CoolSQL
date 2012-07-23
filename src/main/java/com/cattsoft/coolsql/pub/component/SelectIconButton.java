/*
 * Created on 2007-3-6
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author liu_xlin
 *����ѡ�����
 */
public class SelectIconButton extends IconButton{

    private JPopupMenu popMenu;
    public SelectIconButton(Icon icon)
    {
        super(icon);
        popMenu=new JPopupMenu();
        addActionListener(new ClickAction());
    }
    /**
     * ���ѡ��˵���
     * @param label  --ѡ��˵���ı�ǩ
     * @param icon  --ѡ��˵����ͼ��
     * @param action  --ѡ��˵������󴥷��Ķ���
     * @return  --���ز˵������
     */
    public JMenuItem addSelectItem(String label,Icon icon,ActionListener action)
    {
        JMenuItem item=new JMenuItem(label,icon);
        item.addActionListener(action);
        return popMenu.add(item);
        
    }
    /**
     * 
     * @author liu_xlin
     *��ť����󣬴������¼�������ѡ����˵�
     */
    private class ClickAction implements ActionListener
    {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            int componentCount=popMenu.getComponentCount();
            if(componentCount<1)
            {
                return;
            }else if(componentCount==1)  //���ֻ��һ���˵��ֱ��ִ��Ψһ�˵���Ĵ���
            {
                JMenuItem item=(JMenuItem)popMenu.getComponent(0);
                item.doClick();
            }else 
            {
                int x = (int) (SelectIconButton.this.getWidth() - popMenu
                        .getPreferredSize().getWidth());
                popMenu.show(SelectIconButton.this, x, SelectIconButton.this
                        .getHeight());
            }
        }
        
    }
}
