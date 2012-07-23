/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-6-4
 */
package com.cattsoft.coolsql.api;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * @author Kenny Liu
 *
 * 2009-6-4 create
 */
public class ApplicationMenuBarAPI implements IApplicationMenuBarAPI {

	private JMenuBar menuBar;
	
	private JMenu helpMenu;
	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBar#addMenu(javax.swing.JMenuItem)
	 */
	public void addMenu(JMenu menu) {
		int menuCount = menuBar.getMenuCount();
		menuBar.add(menu, menuCount - 1);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBar#addMenuToEdit(javax.swing.JMenuItem, boolean)
	 */
	public void addMenuToEdit(JMenuItem menu, boolean isAddSeparator) {
		addMenu(1, menu, isAddSeparator);

	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBar#addMenuToFavorite(javax.swing.JMenuItem, boolean)
	 */
	public void addMenuToFavorite(JMenuItem menu, boolean isAddSeparator) {
		addMenu(3, menu, isAddSeparator);

	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBar#addMenuToFile(javax.swing.JMenuItem, boolean)
	 */
	public void addMenuToFile(JMenuItem menu, boolean isAddSeparator) {
		addMenu(0, menu, isAddSeparator);

	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBar#addMenuToHelp(javax.swing.JMenuItem, boolean)
	 */
	public void addMenuToHelp(JMenuItem menu, boolean isAddSeparator) {
		if (isAddSeparator) {
			helpMenu.addSeparator();
		}
		
		helpMenu.add(menu);

	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBar#addMenuToSQLAndData(javax.swing.JMenuItem, boolean)
	 */
	public void addMenuToSQLAndData(JMenuItem menu, boolean isAddSeparator) {
		addMenu(4, menu, isAddSeparator);

	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBar#addMenuToTool(javax.swing.JMenuItem, boolean)
	 */
	public void addMenuToTool(JMenuItem menu, boolean isAddSeparator) {
		addMenu(5, menu, isAddSeparator);

	}

	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBar#addMenuToView(javax.swing.JMenuItem, boolean)
	 */
	public void addMenuToView(JMenuItem menu, boolean isAddSeparator) {
		addMenu(2, menu, isAddSeparator);

	}
	
	/* (non-Javadoc)
	 * @see com.coolsql.api.IApplicationMenuBarAPI#getHelpMenu()
	 */
	public JMenu getHelpMenu() {
		return helpMenu;
		
	}

	/**
	 * @param menuBar the menuBar to set
	 */
	void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}
	
	void setHelpMenu(JMenu helpMenu) {
		this.helpMenu = helpMenu;
	}
	
	private void addMenu(int i, JMenuItem menu, boolean isAddSeparator) {
		JMenu parentMenu = menuBar.getMenu(i);
		if (isAddSeparator) {
			parentMenu.addSeparator();
		}
		
		parentMenu.add(menu);
	}

}
