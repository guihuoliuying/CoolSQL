/**
 * 
 */
package com.cattsoft.coolsql.system.menu.loadlistener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menubuild.IMenuLoadListener;

/**
 * �Ƿ���ʾ�кŲ˵������ʱ�ļ�������
 * @author kenny liu
 *
 * 2007-12-7 create
 */
public class ShowLineMenuLoadListener implements IMenuLoadListener {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action(javax.swing.JMenuItem)
	 */
	public void action(JMenuItem item) {
		if(item instanceof JCheckBoxMenuItem)
		{
			JCheckBoxMenuItem i=(JCheckBoxMenuItem)item;
			i.setSelected(Setting.getInstance().getShowLineNumbers());
		}

	}

}
