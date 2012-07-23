package com.cattsoft.coolsql.action.framework;

import java.beans.BeanInfo;
import java.beans.ExceptionListener;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cattsoft.coolsql.view.log.LogProxy;

public class CSPersistence
	implements ExceptionListener
{
	private String filename;

	public CSPersistence()
	{
	}
	
	public CSPersistence(String file)
	{
		filename = file;
	}

	/**
	 * Makes a property of the given class transient, so that it won't be written
	 * into the XML file when saved using WbPersistence
	 * @param clazz
	 * @param property
	 */
	public static void makeTransient(Class<?> clazz, String property)
	{
		try
		{
			BeanInfo info = Introspector.getBeanInfo( clazz );
			PropertyDescriptor propertyDescriptors[] = info.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++)
			{
				PropertyDescriptor pd = propertyDescriptors[i];
				if ( pd.getName().equals(property) )
				{
					pd.setValue( "transient", Boolean.TRUE );
				}
			}
		}
		catch ( IntrospectionException e )
		{
		}
	}

	public Object readObject()
		throws Exception
	{
		if (this.filename == null) throw new IllegalArgumentException("No filename specified!");
		InputStream in = new BufferedInputStream(new FileInputStream(filename), 32*1024);
		return readObject(in);
	}

	public Object readObject(InputStream in)
		throws Exception
	{
		try
		{
			XMLDecoder e = new XMLDecoder(in, null, this);
			Object result = e.readObject();
			e.close();
			return result;
		}
		finally
		{
			try { in.close(); } catch (Throwable th) {}
		}
	}

	public void writeObject(Object aValue)
		throws IOException
	{
		if (aValue == null) return;

		BufferedOutputStream out = null;
		try
		{
			out = new BufferedOutputStream(new FileOutputStream(filename), 32*1024);
			XMLEncoder e = new XMLEncoder(out);
			e.writeObject(aValue);
			e.close();
		}
		finally
		{
			try { out.close(); } catch (Throwable th) {}
		}
	}

	public void exceptionThrown(Exception e)
	{
		LogProxy.errorReport( "Error reading file " + filename, e);
	}
	
}
