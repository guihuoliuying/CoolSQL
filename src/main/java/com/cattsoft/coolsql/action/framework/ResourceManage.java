/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-4-8
 */
package com.cattsoft.coolsql.action.framework;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cattsoft.coolsql.pub.parse.StringManager;

/**
 * @author Kenny Liu
 *
 * 2009-4-8 create
 */
public class ResourceManage {

	/** Logger for this class. */
	private static Logger s_log = Logger.getLogger(StringManager.class);

	/** Contains the localised strings. */
	private Properties resources;
	private String _bundleBaseName;

	ResourceManage()
	{
		super();
		resources = new Properties();
		
		_bundleBaseName = "com.coolsql.action.framework.I18NStrings";
		ResourceBundle _rsrcBundle = ResourceBundle.getBundle(_bundleBaseName, Locale.getDefault());

		addAllResource(_rsrcBundle);
	}
	/**
	 * Add resource from ResourceBundle into current resource repository.
	 * @param _rsrcBundle the resource bundle object.
	 */
	public void addAllResource(ResourceBundle _rsrcBundle) {
		Enumeration<String> en = _rsrcBundle.getKeys();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			resources.put(key, _rsrcBundle.getString(key));
		}
	}
	/**
	 * Retrieve the localized string for the passed key. If it isn't found
	 * an error message is returned instead.
	 *
	 * @param	key		Key to retrieve string for.
	 *
	 * @return	Localized string or error message.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown if <TT>null</TT> <TT>key</TT> passed.
	 */
	public String getString(String key)
	{
		if (key == null)
		{
			throw new IllegalArgumentException("key == null");
		}

		String value = resources.getProperty(key);
		if (value == null) {
			String message = "No resource string found for key '" + key + "' in bundle " + _bundleBaseName + "\n\n";
			if(s_log.isDebugEnabled())
			{
				s_log.error(message);
			}
		}
		
		return value;
	}

    /**
     * Retrieve the localized string for the passed key and format it with the
     * passed arguments.
     *
     * @param   key     Key to retrieve string for.
     * @param   args    Any string arguments that should be used as values to 
     *                  parameters found in the localized string.
     *                   
     * @return  Localized string or error message.
     *
     * @throws  IllegalArgumentException
     *          Thrown if <TT>null</TT> <TT>key</TT> passed.
     */    
    public String getString(String key, String[] args) 
    {
        return getString(key, (Object[])args);
    }
    
	/**
	 * Retrieve the localized string for the passed key and format it with the
	 * passed arguments.
	 *
	 * @param	key		Key to retrieve string for.
     * @param   args    Any string arguments that should be used as values to 
     *                  parameters found in the localized string. 
	 *
	 * @return	Localized string or error message.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown if <TT>null</TT> <TT>key</TT> passed.
	 */
	public String getString(String key, Object... args)
	{
		if (key == null)
		{
			throw new IllegalArgumentException("key == null");
		}

		if (args == null)
		{
			args = new Object[0];
		}

		final String str = getString(key);
		try
		{
			return MessageFormat.format(str, args);
		}
		catch (IllegalArgumentException ex)
		{
			String msg = "Error formatting i18 string. Key is '" + key + "'";
			s_log.error(msg, ex);
			return msg + ": " + ex.toString();
		}
	}
}
