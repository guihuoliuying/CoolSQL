/**
 * 
 */
package com.cattsoft.coolsql.gui.property;

import java.awt.Color;

import javax.swing.JPanel;

import com.cattsoft.coolsql.system.ISetting;
import com.cattsoft.coolsql.system.Setting;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-1-17 create
 */
@SuppressWarnings("serial")
public abstract class BasePropertyPane extends PropertyPane {

	protected JPanel panel;
	
	public BasePropertyPane()
	{
		super();
		setChanged(false);
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyPane#initContent()
	 */
	@Override
	public JPanel initContent() {
		if(panel==null)
			panel=new JPanel();
		return panel;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#cancel()
	 */
	public void cancel() {

	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
	 */
	public boolean isNeedApply() {
		return true;
	}
	@Override
	protected boolean isNeedListenToChild() {
		if(!this.isNeedApply())
		{
			return false;
		}
		return true;
	}
	/**
	 * Get new value from property map, and assign new value to setting object.
	 * This method is fit for condition where clientproperty name of component is equal to property name in the setting object.
	 */
	protected void assignValue(String propertyName)
	{
		Object newObject=getPropertyMap().get(propertyName);
		if(newObject==null)
			return;
		ISetting setting = getSetting();
		if (setting == null) {
			setting = Setting.getInstance();
		}
		if(newObject instanceof Boolean)
			setting.setBooleanProperty(propertyName,(Boolean)newObject);
		else if(newObject instanceof Integer)
			setting.setIntProperty(propertyName,(Integer)newObject);
		else if(newObject instanceof Color)
			setting.setColorProperty(propertyName,(Color)newObject);
		else
			Setting.getInstance().setProperty(propertyName,newObject.toString());
	}
}
