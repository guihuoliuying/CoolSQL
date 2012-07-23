/**
 * 
 */
package com.cattsoft.coolsql.plugin;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * default plugin resource class,and it manage all resource such as string message,icon file.
 * @author ��Т��(kenny liu)
 *
 * 2008-1-23 create
 */
public class PluginResource implements  Resource{

	private ResourceBundle resource;
	private IPlugin plugin;
	public PluginResource(String resourceFile,IPlugin plugin)
	{
		this.plugin=plugin;
		if(resourceFile==null)
			throw new IllegalArgumentException("you must specify a resource file!");
		
		resource=ResourceBundle.getBundle(resourceFile,Locale.getDefault(),getClassLoader(plugin));
		if(resource==null)
			throw new IllegalArgumentException("the resource file is not valid");
	}
	/* (non-Javadoc)
	 * @see com.smartsql.plugin.Resource#getIcon(java.lang.String)
	 */
	public Icon getIcon(String name) {
		String path=getString(name);
	    URL url = this.plugin.getClass().getResource(path);
        if(url != null)
            return new ImageIcon(url);
        return null;
	}
	/* (non-Javadoc)
	 * @see com.smartsql.plugin.Resource#getPlugin()
	 */
	public IPlugin getPlugin() {
		return plugin;
	}
	/* (non-Javadoc)
	 * @see com.smartsql.plugin.Resource#getString(java.lang.String)
	 */
	public String getString(String key) {
		return resource.getString(key);
	}
	private static ClassLoader getClassLoader(IPlugin plugin)
	{
		if (plugin == null)
		{
			throw new IllegalArgumentException("Null IPlugin passed");
		}
		return plugin.getClass().getClassLoader();
	}
	
}
