/**
 * 
 */
package com.cattsoft.coolsql.action.framework;

import java.awt.Dimension;

import javax.swing.Icon;

import com.cattsoft.coolsql.pub.component.IconButton;

/**
 * ToolBar button 
 * @author ��Т��(kenny liu)
 *
 * 2008-4-14 create
 */
public class ToolBarButton extends IconButton {

	private static final long serialVersionUID = 1L;
	
	public ToolBarButton()
	{
		this(null);
	}
	public ToolBarButton(Icon icon) {
		super(icon);
	}
	
	public Dimension getIconButtonSize() {
		Icon icon = getIcon();
		if (icon == null) {
			return new Dimension(21, 21);
		} else {
			return new Dimension(icon.getIconWidth() + 3, icon.getIconHeight() + 3);
		}
    } 
}
