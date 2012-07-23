/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-6-4
 */
package com.cattsoft.coolsql.api;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * @author Kenny Liu
 *
 * 2009-6-4 create
 */
public interface IApplicationMenuBarAPI {

	void addMenuToFile(JMenuItem menu, boolean isAddSeparator);
	
	void addMenuToEdit(JMenuItem menu, boolean isAddSeparator);
	
	void addMenuToView(JMenuItem menu, boolean isAddSeparator);
	
	void addMenuToFavorite(JMenuItem menu, boolean isAddSeparator);
	
	void addMenuToSQLAndData(JMenuItem menu, boolean isAddSeparator);
	
	void addMenuToTool(JMenuItem menu, boolean isAddSeparator);
	
	void addMenuToHelp(JMenuItem menu, boolean isAddSeparator);
	
	JMenu getHelpMenu();
	/**
	 * Add specified menu to menu bar before the help menu.
	 * If you want to define your own menu, this method will satisfy your requirement
	 */
	void addMenu(JMenu menu);
}
