/*
 * �������� 2006-6-2
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * @author liu_xlin
 *JList�ؼ���Ԫ����Ⱦ
 */
public class ListCellRender  implements ListCellRenderer {
     
	public ListCellRender()
	{
		super();
	}
	/* ���� Javadoc��
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		JPanel p=(JPanel)value;
		p.setBackground(list.getBackground());
		
		JCheckBox box=(JCheckBox)p.getComponent(0);
        JLabel l=(JLabel)p.getComponent(1);		
        
		if (box.isSelected()) {
			
			l.setBackground(list.getSelectionBackground());
			l.setForeground(list.getSelectionForeground());
		} else {
			
			l.setBackground(list.getBackground());
			l.setForeground(list.getForeground());
		}
		return p;
	}

}
