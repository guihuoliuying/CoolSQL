/**
 * Create date:2008-4-24
 */
package com.cattsoft.coolsql.system.lookandfeel;

import java.io.Serializable;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-24 create
 */
public class LookAndFeelMetaData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//The name represents current L&F data.
	private String name;
	
	//The look and feel definition class.
	private String lafClass;
	
	//file resource in which the look and feel class is included.
	private String resourcePath;
	public LookAndFeelMetaData()
	{
		this(null,null,null);
	}
	public LookAndFeelMetaData(String name,String lafClass,String resourcePath)
	{
		this.name=name;
		this.lafClass=lafClass;
		this.resourcePath=resourcePath;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @return the lafClass
	 */
	public String getLafClass() {
		return this.lafClass;
	}
	/**
	 * @return the resourcePath
	 */
	public String getResourcePath() {
		return this.resourcePath;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param lafClass the lafClass to set
	 */
	public void setLafClass(String lafClass) {
		this.lafClass = lafClass;
	}
	/**
	 * @param resourcePath the resourcePath to set
	 */
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
}
