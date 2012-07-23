/**
 * 
 */
package com.cattsoft.coolsql.system.favorite;

import java.util.EventObject;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-3-26 create
 */
public class FavoriteEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public static int ACTION_TYPE_ADDED=0;
	public static int ACTION_TYPE_DELETED=1;
	public static int ACTION_TYPE_UPDATED=2;
	public FavoriteEvent(Object source)
	{
		super(source);
	}
}
