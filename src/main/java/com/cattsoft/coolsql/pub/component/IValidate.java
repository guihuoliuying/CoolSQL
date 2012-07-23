/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-4-17
 */
package com.cattsoft.coolsql.pub.component;

/**
 * @author Kenny Liu
 *
 * 2009-4-17 create
 */
public interface IValidate {

	/**
	 * Return true if the value of object is legal, otherwise return false.
	 */
	public boolean isLegal();
	/**
	 * To set the legality of object.
	 */
	public void setLegal(boolean isLegal);
	/**
	 * Specify a validator.
	 */
	public void setValidator(IValidator validator);
}
