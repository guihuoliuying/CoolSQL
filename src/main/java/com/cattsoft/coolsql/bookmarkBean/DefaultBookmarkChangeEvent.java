/*
 * DefaultBookmarkChangeEvent.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.bookmarkBean;

import java.util.EventObject;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-28 create
 */
public class DefaultBookmarkChangeEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private Bookmark oldValue;
	
	private Bookmark newValue;
	/**
	 * @param source
	 */
	public DefaultBookmarkChangeEvent(Object source,Bookmark oldValue,Bookmark newValue) {
		super(source);
		this.oldValue=oldValue;
		this.newValue=newValue;
	}
	/**
	 * @return the oldValue
	 */
	public Bookmark getOldValue() {
		return this.oldValue;
	}
	/**
	 * @return the newValue
	 */
	public Bookmark getNewValue() {
		return this.newValue;
	}
	
}
