/**
 * 
 */
package com.cattsoft.coolsql.plugin;

import javax.swing.Icon;

/**
 * The interface used to manage resources needed by plugin.
 * @author kenny liu
 *
 * 2008-1-23 create
 */
public interface Resource {

	/**
	 * Return a string resource corresponding to specified key.
	 */
	public String getString(String key);
	
	/**
	 * Return a icon resource corresponding to specified key.
	 * get icon object by name
	 */
	public Icon getIcon(String name);
	
	/**
	 * Return plugin object to which current resource object attaches.
	 */
	public IPlugin getPlugin();
}
