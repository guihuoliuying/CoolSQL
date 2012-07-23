package com.cattsoft.coolsql.pub.util;

import com.cattsoft.coolsql.pub.display.FileMappedSequence;
import com.cattsoft.coolsql.sql.interfaces.CharacterSequence;

/**
 * An implementation of the CharacterSequence interface
 * based on a String as the source.
 *
 * @see FileMappedSequence
 * @author kenny liu
 */
public class StringSequence
	implements CharacterSequence
{
	private String source;
	
	/**
	 * Create a StringSequence based on the given String
	 */
	public StringSequence(String s)
	{
		this.source = s;
	}

	public void done()
	{
		this.source = null;
	}

	public int length()
	{
		return source.length();
	}
	
	public char charAt(int index)
	{
		return this.source.charAt(index);
	}

	public CharSequence subSequence(int start, int end)
	{
		return this.source.substring(start, end);
	}
	
}
