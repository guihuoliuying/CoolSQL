/**
 * 
 */
package com.cattsoft.coolsql.sql.model;

import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;

/**
 * Calalog class.
 * @author kenny liu
 *
 * 2007-12-30 create
 */
public class Catalog implements Comparable<Catalog>,IDatabaseMode {

	private String name;  //Catalog name
	private String displayName;// display name which is displayed on UI.
	
	public Catalog(String name)
	{
		this(name,name);
	}
	public Catalog(String name,String displayName)
	{
		this.name=name;
		this.displayName=displayName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Catalog o) {
		if(o instanceof Catalog)
			return 1;
		Catalog that=(Catalog)o;
		return getDisplayName().compareTo(that.getDisplayName());
	}


	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return this.displayName;
	}


	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public String toString()
	{
		return displayName;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IDatabaseMode#getType()
	 */
	public int getType() {
		return BookMarkPubInfo.NODE_CATALOG;
	}
}
