/**
 * 
 */
package com.cattsoft.coolsql.system.menu.loadlistener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menubuild.IMenuLoadListener;

/**
 * @author kenny liu
 *
 * 2007-12-14 create
 */
public class CompositeViewDisplayCheckListener implements IMenuLoadListener {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action(javax.swing.JMenuItem)
	 */
	public void action(final JMenuItem item) {
		if (item instanceof JCheckBoxMenuItem) {
			boolean isDisplay = Setting.getInstance().getBoolProperty(
					"view.composite.isdisplay", true);
			JCheckBoxMenuItem m=(JCheckBoxMenuItem)item;
			m.setSelected(isDisplay);
			
//			ViewManage.getInstance().get.addPropertyChangeListener(new PropertyChangeListener()
//			{
//
//				public void propertyChange(PropertyChangeEvent evt) {
//					if(View.PROPERTY_HIDDEN.equals(evt.getPropertyName()))
//					{
//						Object ob=evt.getNewValue();
//						if(ob instanceof Boolean)
//						item.setVisible(((Boolean)evt.getNewValue()).booleanValue());
//					}
//				}
//				
//			}
//			);
		}

	}

}
