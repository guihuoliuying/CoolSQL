package com.cattsoft.coolsql.pub.util;

import java.util.Locale;

public class MyLocale
	implements Comparable<MyLocale>
{
	private final Locale locale;
	
  public MyLocale(Locale l)
  {
		this.locale = l;
  }
	
	public Locale getLocale() 
	{
		return locale;
	}
	
	public String toString()
	{
		String lang = StringUtil.capitalize(locale.getDisplayLanguage(locale));
		return lang;
	}
	
	public int compareTo(MyLocale other)
	{
		return this.toString().compareTo(other.toString());
	}
	
	public boolean equals(Object other)
	{
		if (other == null) return false;
		if (other instanceof MyLocale)
		{
			return this.locale.equals(((MyLocale)other).locale);
		}
		return false;
	}
	
	public int hashCode()
	{
		return locale.hashCode();
	}
}
