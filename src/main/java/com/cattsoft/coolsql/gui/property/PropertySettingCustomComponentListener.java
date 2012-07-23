/**
 * Create date:2008-5-4
 */
package com.cattsoft.coolsql.gui.property;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.cattsoft.coolsql.pub.component.ColorIconButton;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-4 create
 */
public class PropertySettingCustomComponentListener
		extends
			CustomComponentListener {

	private PropertyPane propertyPane;
	
	private PropertyChangeListener colorIconButtonListener;
	public PropertySettingCustomComponentListener(PropertyPane propertyPane)
	{
		this.propertyPane=propertyPane;
		colorIconButtonListener=new ColorIconButtonListener();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.CustomComponentListener#accept(java.awt.Component)
	 */
	@Override
	public boolean accept(Component c) {
		if(c instanceof ColorIconButton)
			return true;
		else
			return false;
	}
	@Override
	public boolean isContainer(Component c) {
		return false;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.CustomComponentListener#startListeningTo(java.awt.Component)
	 */
	@Override
	public void startListeningTo(Component c) {
		if(c instanceof ColorIconButton)
		{
			if(propertyPane!=null)
				c.addPropertyChangeListener(colorIconButtonListener);
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.CustomComponentListener#stopListeningTo(java.awt.Component)
	 */
	@Override
	public void stopListeningTo(Component c) {
		if(c instanceof ColorIconButton)
		{
			c.removePropertyChangeListener(colorIconButtonListener);
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.CustomComponentListener#valueFor(java.awt.Component)
	 */
	@Override
	public Object valueFor(Component c) {
		if(c instanceof ColorIconButton)
		{
			return ((ColorIconButton)c).getIconColor();
		}
		return null;
	}
	private class ColorIconButtonListener implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getPropertyName().equals(ColorIconButton.PROPERTY_ICON_COLOR))
			{
//				Object ob=evt.getSource();
//				if(!(ob instanceof ColorIconButton))
//					return;
//				JComponent jc=(JComponent)ob;
//					
//				String mapKey =(String)jc.getClientProperty(PropertyInterface.PROPERTY_NAME);
//		        // debug: System.err.println("MaybeUpdateMap " + mapKey + " from " + comp);
//		        if (mapKey != null) {
//		            Object value = valueFor(jc);
//		            propertyPane.getPropertyMap().put(mapKey, value);
//		        }
				propertyPane.fireItemChanged(evt);
			}
		}
	}
}
