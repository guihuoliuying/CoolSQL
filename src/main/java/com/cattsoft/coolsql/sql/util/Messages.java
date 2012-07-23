package com.cattsoft.coolsql.sql.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages
{

    private Messages()
    {
    }

    public static String getString(Class resourceClass, String key)
    {
        return getString(createKey(resourceClass, key));
    }

    private static String createKey(Class resourceClass, String key)
    {
        return resourceClass.getName() + (key.startsWith(".") ? key : "." + key);
    }

    public static String getString(String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch(MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }

    public static String getString(Class resourceClass, String key, Object arguments[])
    {
        return getString(createKey(resourceClass, key), arguments);
    }

    public static String getString(String key, Object arguments[])
    {
        String string = getString(key);
        return MessageFormat.format(string, arguments);
    }
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.coolsql.resource.resource");

}
