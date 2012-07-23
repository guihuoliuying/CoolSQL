/*
 * �������� 2006-12-15
 */
package com.cattsoft.coolsql.view.resultset;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.table.JTableHeader;

import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin ��ͷ������Ҽ������
 */
public class HeaderMouseListener implements MouseListener {

	boolean isPopupTriggerWhenPress;
    /*
     * ���� Javadoc��
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {

    }

    /*
     * ���� Javadoc��
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {

    }

    /*
     * ���� Javadoc��
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {

    }

    /*
     * ���� Javadoc��
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
    	isPopupTriggerWhenPress=e.isPopupTrigger();
    }

    /*
     * ���� Javadoc��
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        if (!(e.getSource() instanceof JTableHeader))
            return;

        if (isPopupTriggerWhenPress||e.isPopupTrigger()) {
            TableHeaderMenuManage headerMenu = ViewManage.getInstance()
                    .getResultView().getHeaderMenu();
            //���µ�ǰ�����˵��������ı�ͷ���
            headerMenu.setComponent((JTableHeader) e.getSource());
            //���µ�ǰ�˵�������λ��
            headerMenu.setPoint(e.getPoint());

            headerMenu.setComponent((JComponent) e.getSource()); //���²˵��������������
            headerMenu.getPopMenu().show((JComponent) e.getSource(), e.getX(),
                    e.getY());
        }
    }

}
