/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-5-12
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.io.Serializable;

import com.cattsoft.coolsql.sql.model.IDatabaseMode;

/**
 * @author Kenny Liu
 *
 * 2009-5-12 create
 */
public class EntityGroup implements IDatabaseMode, Serializable {

	private static final long serialVersionUID = 1L;

	private IDatabaseMode parentObj;
	
	private String name;
	private int type;
	
	public EntityGroup(IDatabaseMode parentObj, String name) {
		this.parentObj = parentObj;
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IDatabaseMode#getName()
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parentObj
	 */
	public IDatabaseMode getParentObject() {
		return this.parentObj;
	}
	/**
	 * @param parentObj the parentObj to set
	 */
	public void setParentObject(IDatabaseMode parentObj) {
		this.parentObj = parentObj;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return this.type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}
