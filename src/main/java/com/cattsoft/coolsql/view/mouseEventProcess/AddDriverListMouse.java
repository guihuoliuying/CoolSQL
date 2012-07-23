/*
 * �������� 2006-6-3
 *
 */
package com.cattsoft.coolsql.view.mouseEventProcess;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * @author liu_xlin
 *�������ʾ�б�ؼ�������¼�����
 */
public class AddDriverListMouse extends MouseAdapter {
	JList list=null;
	public AddDriverListMouse(JList l)
	{
		list=l;
	}
   public void mouseReleased(MouseEvent e)
   {  	 
   	  
   	  DefaultListModel model=(DefaultListModel)list.getModel();
   	  if(model.getSize()<1)
   	  {
   	  	return;
   	  }
   	  JPanel p=(JPanel)list.getSelectedValue();
   	  if(p==null||p.getComponentCount()<1)
   		  return ;
   	  JCheckBox box=(JCheckBox)p.getComponent(0);
   	  boolean isSelected=box.isSelected();
   	  box.setSelected(!isSelected);
   	  list.repaint();

   }
}
