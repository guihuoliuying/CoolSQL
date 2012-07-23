/*
 * PrimaryKeyProperty.java
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

import javax.swing.JTable;

import com.cattsoft.coolsql.gui.property.BasePropertyPane;
import com.cattsoft.coolsql.pub.display.CommonDataTable;
import com.cattsoft.coolsql.pub.display.TableCellObject;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.sql.model.PrimaryKey;
import com.cattsoft.coolsql.sql.model.Table;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-6-8 create
 */
public class PrimaryKeyProperty extends BasePropertyPane {
	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(SystemFunctionProperty.class);
	/**
	 * 
	 */
	public PrimaryKeyProperty() {
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
		if (ob == null)
			return;
		if (ob instanceof Table) {
			Table en = (Table) ob;
			panel.removeAll();
        	panel.setLayout(new BorderLayout());
        	
        	 String[] header=new String[3];
             for(int i=0;i<header.length;i++)
             {
                 header[i]=stringMgr.getString("property.database.primarykey.tablecolumn"+i);
             }
        	
        	try {
        		PrimaryKey pk=en.getPrimaryKey();
				Object[][] data=new Object[pk.getNumberOfColumns()][header.length];
				for(int i=0;i<pk.getNumberOfColumns();i++)
				{
						if(0==i)
						{
							data[i][0]=pk.getPkName();
						}else
						{
							data[i][0]="";
						}
						data[i][1]=String.valueOf(pk.getKeySequence(i));
						data[i][2]=new TableCellObject(pk.getColumn(i),BookMarkPubInfo
								.getIconList()[BookMarkPubInfo.NODE_COLUMN]);
				}
				CommonDataTable table=new CommonDataTable(data,header,new int[]{2})
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
