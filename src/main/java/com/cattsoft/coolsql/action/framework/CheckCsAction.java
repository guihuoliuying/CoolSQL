/**
 * Create date:2008-5-17
 */
package com.cattsoft.coolsql.action.framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

/**
 * @author ��Т��(kenny liu)
 * 
 * 2008-5-17 create
 */
public class CheckCsAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;
	public static final String SELECT_STATUS = "isselected";
	
	/**
	 * 
	 */
	public CheckCsAction() {
	}

	/**
	 * @param l
	 */
	public CheckCsAction(ActionListener l) {
		super(l);
	}

	/**
	 * @param l
	 * @param name
	 */
	public CheckCsAction(ActionListener l, String name) {
		super(l, name);
	}
	@Override
	protected void init() {
		super.init();
		setType(CsAction.MENUITEMTYPE_CHECKBOX);
	}
	public void setSelected(boolean isSelected) {
		this.putValue(SELECT_STATUS, isSelected);
	}
	public boolean isSelected()
	{
		Object value=getValue(SELECT_STATUS);
		if(value==null)
			return false;
		return (Boolean)value;
	}
	@Override
	public void actionPerformed(final ActionEvent e) {
		Object ob=e.getSource();
		if(ob instanceof JCheckBoxMenuItem)
		{
			JCheckBoxMenuItem checkItem=(JCheckBoxMenuItem)ob;
			setSelected(checkItem.isSelected());
		}else if(ob instanceof JCheckBox)
		{
			JCheckBox box=(JCheckBox)ob;
			setSelected(box.isSelected());
		}
		
		super.actionPerformed(e);
	}
	@Override
	protected JMenuItem getSuitableItem(String type) {
		final JCheckBoxMenuItem item = (JCheckBoxMenuItem) super
				.getSuitableItem(type);
		addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(SELECT_STATUS)) {
					Object newValue = evt.getNewValue();
					if (newValue == null)
						item.setSelected(false);
					item.setSelected((Boolean) newValue);
				}
			}
		});
		return item;
	}
}
