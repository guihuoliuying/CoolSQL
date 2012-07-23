/*
 * PopMenuMouseListener.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-28 create
 */
public class PopMenuMouseListener extends MouseAdapter {

	protected boolean isPopupTriggerWhenPress;
	
	@Override
	public final void mousePressed(MouseEvent e)
	{
		isPopupTriggerWhenPress=e.isPopupTrigger();
	}
	/**
	 * Check if the mouse event can trigger the popup menu.
	 * @param e --this event must occurred when mouse released
	 */
	protected boolean isPopupTrigger(MouseEvent e)
	{
		return isPopupTriggerWhenPress||e.isPopupTrigger();
	}
}
