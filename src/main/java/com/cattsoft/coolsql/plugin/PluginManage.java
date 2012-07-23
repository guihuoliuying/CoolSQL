/**
 * 
 */
package com.cattsoft.coolsql.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.log4j.Logger;

import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * This class manage the activities and life cycle of all plugins.
 * @author ��Т��(Kenny liu)
 *
 * 2008-1-10 create
 */
public class PluginManage {
	private static final Logger logger=Logger.getLogger("plugin");
	
	private static PluginManage pm=null;
	
	private Map<String,PluginInfo> pluginMap;
	private PluginManage()
	{
		pluginMap=new HashMap<String,PluginInfo>();
	}
	public static synchronized PluginManage getInstance()
	{
		if(pm==null)
			pm=new PluginManage();
		
		return pm;
	}
	public void loadAllPlugin() throws PluginException
	{
		boolean isMac = System.getProperty("os.name").toLowerCase().startsWith(
        "mac");
		String debugPluginFile = System.getProperty("plugin.debug");
//			PublicResource.getSystemString("system.plugin.mode");
		if(debugPluginFile != null)  //debug mode,load plugin entrys from local directory.
		{
			debugPluginFile = debugPluginFile.trim();
			if (debugPluginFile.equals("")) {
				debugPluginFile = "plugin.properties";
			}
			File file = new File(debugPluginFile);
			if(!file.exists())
				return ;
			LineNumberReader reader;
			try {
				reader=new LineNumberReader(new FileReader(file));
				String lineStr = null;
				while((lineStr=reader.readLine()) != null)
				{
					if(lineStr.equals(""))
						continue;
					
					Object ob = null;
					try {
						Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(lineStr);
						ob = ConstructorUtils.invokeConstructor(clazz, null);
					} catch (Exception e) {
						LogProxy.errorReport("System can't load plugin : " + lineStr, e);
						continue;
					} 
					if(!(ob instanceof IPlugin))
						continue;
					IPlugin plugin=(IPlugin)ob;
					plugin.load();
					pluginMap.put(plugin.getInternalName(), new PluginInfo(plugin,true));
				}
					
			} catch (FileNotFoundException e) {
				LogProxy.errorReport(e);
			} catch (IOException e) {
				LogProxy.errorReport(e);
			}
			
		}else
		{
			List<URL> tmp=new ArrayList<URL>();
			String configfile=PublicResource.getSystemString("system.plugin.normal.directory");
			File f=new File(configfile);
			if(f.exists()&&f.isDirectory())
			{
				File[] files=f.listFiles();
				if(files==null||files.length==0)
					return;
				
				for(int i=0;i<files.length;i++)
				{
					if(files[i].exists())
					{
						checkPlugin(files[i],tmp,isMac);
					}
				}
			}
			if(tmp.size()==0)
				return;
			URL[] urls=(URL[])tmp.toArray(new URL[tmp.size()]);
			LoadJar.getInstance().addURL(urls);
			Class<?>[] classes=LoadJar.getInstance().getAssignableClass(urls,IPlugin.class);
			for(int i=0;i<classes.length;i++)
			{
				try
				{
					loadPlugin(classes[i]);
				}catch(InstantiationException e)
				{
					throw new PluginException(e);
				}catch(IllegalAccessException e)
				{
					throw new PluginException(e);
				}
			}
		}
	}
	
	private void loadPlugin(Class<?> pluginClass) throws InstantiationException, IllegalAccessException, PluginException
	{
		if(IPlugin.class.isAssignableFrom(pluginClass))
		{
			IPlugin plugin=(IPlugin)pluginClass.newInstance();
			PluginInfo info=new PluginInfo(plugin);
			plugin.load();
			info.setLoaded(true);
			pluginMap.put(info.getInternalName(), info);
		}
	}
    private void checkPlugin(File pluginFile, List<URL> pluginUrls,
			boolean isMac) {
		final String fileName = pluginFile.getAbsolutePath();
		if (!fileName.toLowerCase().endsWith("src.jar")
				&& (fileName.toLowerCase().endsWith(".zip") || fileName
						.toLowerCase().endsWith(".jar"))) {
			try {

				final String fullFilePath = pluginFile.getAbsolutePath();
				final String internalName = StringUtil
						.removeFileNameSuffix(pluginFile.getName());
				PluginInfo pi = pluginMap.get(internalName);
				if (pi != null) {
					return;
				}
				// if (!isMac && internalName.startsWith("macosx")) {
				// s_log.info(
				// "Detected MacOS X plugin on non-Mac platform - skipping");
				// return;
				// }
				pluginUrls.add(pluginFile.toURL());

				final String pluginDirName = StringUtil
						.removeFileNameSuffix(fullFilePath);
				final File libDir = new File(pluginDirName, "lib");
				addPluginLibraries(libDir, pluginUrls);

			} catch (IOException ex) {
				LogProxy.errorReport(logger, ex);
			}
		}
	}
    private void addPluginLibraries(File libDir, List<URL> pluginUrls) {
        if (libDir.exists() && libDir.isDirectory()) {
            File[] libDirFiles = libDir.listFiles();
            for (int j = 0; j < libDirFiles.length; ++j) {
                if (libDirFiles[j].isFile()) {
                    final String fn = libDirFiles[j].getAbsolutePath();
                    if (fn.toLowerCase().endsWith(".zip")
                            || fn.toLowerCase().endsWith(".jar")) {
                        try {
                            pluginUrls.add(libDirFiles[j].toURL());
                        } catch (IOException ex) {
                           LogProxy.errorReport(logger, ex);
                        }
                    }
                }
            }
        }

    }
    public void initializePlugins() throws PluginException {
    	Collection<PluginInfo> plugins=pluginMap.values();
    	for(PluginInfo info:plugins)
    	{
    		if(info.isLoaded())
    		{
    			info.getPlugin().initialize();
    		}
    	}
    }
    public void unloadPlugins()
    {
    	Collection<PluginInfo> plugins=pluginMap.values();
    	for(PluginInfo info:plugins)
    	{
    		if(info.isLoaded())
    		{
    			info.getPlugin().unload();
    		}
    	}
    }
    /**
     * get all plugins that has been loaded on startup
     * @return
     */
    public Collection<PluginInfo> getPlugins()
    {
    	return pluginMap.values();
    }
    /**
     * get plugin by parameter:name 
     * @param name --key that has been binded to plugin
     * @return
     */
    public PluginInfo getPlugin(String name)
    {
    	return pluginMap.get(name);
    }
}
