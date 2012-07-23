/**
 * 
 */
package com.cattsoft.coolsql.action.framework;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

/**
 * MenuItem class returned by action frame.
 * @author ��Т��(kenny liu)
 *
 * 2008-4-14 create
 */
public class CsMenuItem extends JMenuItem {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public CsMenuItem() {
	}

	/**
	 * @param icon
	 */
	public CsMenuItem(Icon icon) {
		super(icon);
	}

	/**
	 * @param text
	 */
	public CsMenuItem(String text) {
		super(text);
	}

	/**
	 * @param a
	 */
	public CsMenuItem(Action a) {
		super(a);
	}

	/**
	 * @param text
	 * @param icon
	 */
	public CsMenuItem(String text, Icon icon) {
		super(text, icon);
	}

	/**
	 * @param text
	 * @param mnemonic
	 */
	public CsMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
	}
	public void setText(String aText)
	{
		if(aText==null)
			return;
		int pos = aText.indexOf('&');
		if (pos > -1)
		{
			char mnemonic = aText.charAt(pos + 1);
			aText = aText.substring(0, pos) + aText.substring(pos + 1);
			this.setMnemonic(mnemonic);
		}
		super.setText(aText);
	}	
}
