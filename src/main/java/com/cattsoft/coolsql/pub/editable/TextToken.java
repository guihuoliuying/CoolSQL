/**
 * 
 */
package com.cattsoft.coolsql.pub.editable;

import java.io.Serializable;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-6 create
 */
public class TextToken implements Serializable {

	private static final long serialVersionUID = 1L;
	private int start;
	private int length;
	private String text;
	public TextToken(int start,String text)
	{
		this.start=start;
		this.text=text;
		if(text!=null)
			length=text.length();
	}
	/**
	 * @return the start
	 */
	public int getStart() {
		return this.start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return this.length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
