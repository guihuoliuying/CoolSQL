/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-4-19
 */
package com.cattsoft.coolsql.pub.component;

/**
 * @author Kenny Liu
 *
 * 2009-4-19 create
 */
public interface IValidator {

	/**
	 * Validate the state of source object.
	 * @param source the object needed be validated.
	 * @return true if the state of source object is legal, otherwise return false. 
	 */
	public boolean validate(Object source);
}
