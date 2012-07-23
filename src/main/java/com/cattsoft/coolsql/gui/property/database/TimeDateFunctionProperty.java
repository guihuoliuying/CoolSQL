/*
 * TimeDateFunctionProperty.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.gui.property.database;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.gui.property.BasePropertyPane;
import com.cattsoft.coolsql.pub.display.CommonDataTable;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-6-8 create
 */
public class TimeDateFunctionProperty extends BasePropertyPane{
	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(TimeDateFunctionProperty.class);
	/**
	 * 
	 */
	public TimeDateFunctionProperty() {
		super();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
	 */
	public boolean isNeedApply() {
		return false;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
	 */
	public void setData(Object ob) {
		Bookmark bookmark=(Bookmark)ob;
		if(bookmark.isConnected())  //����״̬����ʼ�����������塣
        {
			panel.removeAll();
        	panel.setLayout(new BorderLayout());
        	
        	String[] header=new String[]{"Function Name"};
        	
        	try {
				String[] functions=bookmark.getDbInfoProvider().getDatabaseMetaData().getTimeDateFunctions();
				String[][] data=new String[functions.length][1];
				for(int i=0;i<functions.length;i++)
				{
					data[i][0]=functions[i];
				}
				CommonDataTable table=new CommonDataTable(data,header)
                {
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int row, int column)
                    {
                        return false;
                    }
                }
                ;
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                panel.add(new TableScrollPane(table),BorderLayout.CENTER);
                table.adjustPerfectWidth();
			} catch (Exception e) {
				LogProxy.errorReport(e);
			}
        	
        }else
        {
        	panel.removeAll();
        	panel.setLayout(new FlowLayout());
        	panel.add(new JLabel(stringMgr.getString("property.database.timedatefunctions.noconnection")));
        }
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#apply()
	 */
	public void apply() {
		
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#set()
	 */
	public boolean set() {
		return true;
	}

}
