/**
 * 
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;

/**
 * @author ��Т��(kenny liu)
 * 
 * 2008-3-23 create
 */
public class TableScrollPane extends JScrollPane {
	private static final long serialVersionUID = -6196643203032770679L;

	/**
	 * 
	 */
	public TableScrollPane() {
		super();
	}

	/**
	 * @param view
	 */
	public TableScrollPane(Component view) {
		super(view);
	}

	/**
	 * @param vsbPolicy
	 * @param hsbPolicy
	 */
	public TableScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
	}

	/**
	 * @param view
	 * @param vsbPolicy
	 * @param hsbPolicy
	 */
	public TableScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
	}
	@Override
	public void setViewportView(Component view) {
		if (getViewport() == null) {
			setViewport(createViewport());
		}
		getViewport().setView(view);
		if(view instanceof BaseTable)
		{
			((BaseTable)view).setTableRowNumberVisible(Setting.getInstance().
					getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_ISDISPLAYLINENUMBER, true));
			JViewport rowHeaderView=getRowHeader();
			if(rowHeaderView!=null)
			{
				Component com=rowHeaderView.getView();
				if(com instanceof BaseTable.PrivateList)
				{
					com.setBackground(getBackground());
				}
			}
		}
	}
}
