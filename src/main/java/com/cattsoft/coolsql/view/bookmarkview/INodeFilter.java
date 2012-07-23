/**
 * 
 */
package com.cattsoft.coolsql.view.bookmarkview;


/**
 * This interface defines filter logic when tree node is expanded.
 * @author xiaolin
 *
 * 2008-6-11 create
 */
public interface INodeFilter {

	/**
	 * Validate whether the specified parameter should be filtered.
	 * Return false if the userObject should be filtered.
	 */
	public boolean filter(Object userObject);
}
