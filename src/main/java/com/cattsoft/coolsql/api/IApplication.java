/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-6-4
 */
package com.cattsoft.coolsql.api;

import javax.swing.JFrame;

/**
 * @author Kenny Liu
 *
 * 2009-6-4 create
 */
public interface IApplication {

	JFrame getMainFrame();
	
	String getVersoin();
	
	IApplicationMenuBarAPI getApplicationMenuBarAPI();
	
	IApplicationViewsAPI getApplicationViewsAPI();
}
