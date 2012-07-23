/*
 * DriverManagePanel.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.view.BookMarkwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.cattsoft.coolsql.bookmarkBean.DriverInfo;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.BaseTable;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.PopMenuMouseListener;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.system.menubuild.IconResource;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * Driver manage panel, displaying all drivers.
 * User can add and delete selected drivers.
 * @author ��Т��(kenny liu)
 *
 * 2008-6-11 create
 */
public class DriverManagePanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	private JTable driverList;
	
	/**
	 * Menu part.
	 */
	private JPopupMenu popMenu;
	
	private JMenuItem add;
	private JMenuItem delete;
	
	public DriverManagePanel()
	{
		setLayout(new BorderLayout());
    	Vector<String> prompt1=new Vector<String>();
    	prompt1.add(PublicResource.getString("system.bookmarkwizard.selectdriverframe.table.column.driverclass"));
    	prompt1.add(PublicResource.getString("system.bookmarkwizard.selectdriverframe.table.column.drivertype"));
    	prompt1.add(PublicResource.getString("system.bookmarkwizard.selectdriverframe.table.column.driverfilepath"));
    	Vector tmp=new Vector();
    	DefaultTableModel dtm = new DefaultTableModel(tmp, prompt1);
    	driverList = new BaseTable(dtm) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTableHeader header=driverList.getTableHeader();
		header.setReorderingAllowed(false);
//		header.setUI(new IBasicTableHeaderUI());
		header.setBackground(View.getThemeColor());		
		header.setReorderingAllowed(false);
		driverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		driverList.setModel(dtm);
		driverList.setAutoscrolls(true);
		driverList.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
    	JScrollPane scroll=new JScrollPane(driverList);
    	add("Center",scroll);
    	
    	//addDriver button
    	JPanel p=new JPanel();
    	p.setLayout(new FlowLayout(FlowLayout.LEFT));
    	RenderButton addDriver=new RenderButton(PublicResource.getString("buttonLabel.adddriver"));
    	p.add(addDriver);
    	add("South",p);
    	
    	addDriver.addActionListener(this);
    	
    	createPopMenu();
    	driverList.addMouseListener(new PopMenuMouseListener()
    	{
    		@Override
    		public void mouseReleased(MouseEvent e)
    		{
    			if(isPopupTrigger(e))
    			{
    				checkMenuAvailability();
    				popMenu.show(driverList, e.getX(), e.getY());
    			}
    		}
    	}
    	);
    	
    	loadInfo();
	}
	protected void checkMenuAvailability()
	{
		if(driverList.getSelectedColumnCount()==0)
		{
			delete.setEnabled(false);
		}else
		{
			 DriverInfo defaultDriver=SqlUtil.getDefaultDriver();
			 if(defaultDriver==null)
			 {
				 delete.setEnabled(true);
				 return;
			 }
			 String className=(String)driverList.getValueAt(driverList.getSelectedRow(),0);
			 if(className.equals(defaultDriver.getClassName()))
			 {
				 delete.setEnabled(false);
			 }else
			 {
				 delete.setEnabled(true);
			 }
		}
		
	}
	protected JPopupMenu createPopMenu()
	{
		if(popMenu!=null)
		{
			return popMenu;
		}
		popMenu=new JPopupMenu();
		add=new JMenuItem("Add New Driver",IconResource.getBlankIcon());
		add.addActionListener(this);
		popMenu.add(add);
		
		delete=new JMenuItem("Delete",IconResource.getBlankIcon());
		delete.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				if(driverList.getSelectedRowCount()==0)
				{
					JOptionPane.showMessageDialog(DriverManagePanel.this, 
							"Please select a driver!","Warning",
							JOptionPane.WARNING_MESSAGE);
				}
				int[] rows=driverList.getSelectedRows();
				for(int i=0;i<rows.length;i++)
				{
					String className=(String)driverList.getValueAt(rows[i], 0);
					LoadJar.getInstance().removeDriver(className);
					DefaultTableModel model=(DefaultTableModel)driverList.getModel();
					model.removeRow(rows[i]);
				}
			}
			
		}
		);
		popMenu.add(delete);
		
		return popMenu;
	}
    public void actionPerformed(ActionEvent e)
    {
    	Window window=GUIUtil.findLikelyOwnerWindow();
    	AddDriverFrame tmp=null;
    	if(window instanceof JDialog)
    		tmp=new AddDriverFrame((JDialog)window,ViewManage.getInstance().getBookmarkView());
    	else
    		tmp=new AddDriverFrame((JFrame)window,ViewManage.getInstance().getBookmarkView());
    	
    	tmp.setVisible(true);
    }
    /**
     * �����б������һ������Ϣ
     * @param info
     */
    private void addInfoForLoad(DriverInfo info)
    {
    	if(info==null)
    		return ;
    	DefaultTableModel dtm=(DefaultTableModel)driverList.getModel();
    	Vector<String> v=new Vector<String>();
    	v.add(info.getClassName());
    	v.add(info.getType());
    	v.add(info.getFilePath());
    	dtm.addRow(v);
    }
    /**
     * �ӻ�����װ���������Ϣ
     *
     */
    private void loadInfo()
    {
        DriverInfo defaultDriver=SqlUtil.getDefaultDriver();
        if(defaultDriver!=null)
            addInfoForLoad(defaultDriver);
            
    	LoadJar load=LoadJar.getInstance();
    	DriverInfo[] info=load.getDriverInfos();
    	for(int i=0;i<info.length;i++)
    	{
    		addInfoForLoad(info[i]);
    	}
    }
	/**
	 * @return the driverList
	 */
	public JTable getDriverList() {
		return this.driverList;
	}
	/**
	 * @param driverList the driverList to set
	 */
	public void setDriverList(JTable driverList) {
		this.driverList = driverList;
	}
}
