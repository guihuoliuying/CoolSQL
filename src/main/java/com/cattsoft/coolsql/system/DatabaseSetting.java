/**
 * 
 */
package com.cattsoft.coolsql.system;

import java.io.Serializable;

import com.cattsoft.coolsql.pub.display.DelimiterDefinition;

/**
 * the property of database response to bookmark
 * 
 * @author ��Т��(kenny liu)
 * 
 * 2008-1-29 create
 */
public class DatabaseSetting implements Serializable {

	private static final long serialVersionUID = 1L;

	DatabaseSetting() {

	}
	/**
	 * Retrieve the string used to separate multiple SQL statements. Possible
	 * examples are ";" or "GO";
	 * 
	 * @return String used to separate SQL statements.
	 */
	public String getSQLStatementSeparator() {
		return Setting.getInstance().getProperty(
				PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER,
				DelimiterDefinition.STANDARD_DELIMITER.getDelimiter());
	}
	/**
	 * for oracle//TODO:two methods below should be modified later.
	 * @return
	 */
	public boolean supportSingleLineCommands()
	{
		return false;
	}
	public boolean supportShortInclude()
	{
		return false;
	}
}
