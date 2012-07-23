/**
 * 
 */
package com.cattsoft.coolsql.system.menu.loadlistener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menubuild.IMenuLoadListener;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author ��Т��
 *
 * 2008-1-6 create
 */
public class ResultSetViewDisplayCheckListener implements IMenuLoadListener {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action(javax.swing.JMenuItem)
	 */
	public void action(JMenuItem item) {
		if (item instanceof JCheckBoxMenuItem) {
			boolean isDisplay = Setting.getInstance().getBoolProperty(
					PropertyConstant.PROPERTY_VIEW_RESULTSET_ISDISPLAY, true);
			if(!isDisplay)
				ViewManage.getInstance().getResultView().hidePanel(false);
			final JCheckBoxMenuItem m=(JCheckBoxMenuItem)item;
			m.setSelected(isDisplay);
			
			ViewManage.getInstance().getResultView().addPropertyChangeListener(new PropertyChangeListener()
			{

				public void propertyChange(PropertyChangeEvent evt) {
					if(View.PROPERTY_HIDDEN.equals(evt.getPropertyName()))
					{
						Object ob=evt.getNewValue();
						if(ob instanceof Boolean)
							m.setSelected(((Boolean)evt.getNewValue()).booleanValue());
					}
				}
				
			}
			);
		}

	}

}
