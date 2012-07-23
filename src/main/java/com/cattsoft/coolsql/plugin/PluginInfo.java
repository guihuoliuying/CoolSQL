/**
 * 
 */
package com.cattsoft.coolsql.plugin;

/**
 * @author ��Т��
 *
 * 2008-1-12 create
 */
public class PluginInfo {

	private IPlugin plugin;  //plugin object
	private boolean isLoaded;  //flag indicate whethe the plugin has been loaded
	/**
	 * 
	 */
	public PluginInfo() {
		super();
	}
	PluginInfo(IPlugin plugin)
	{
		this(plugin,false);
	}
	PluginInfo(IPlugin plugin,boolean isLoaded) throws IllegalArgumentException
	{
		super();
		if (plugin == null)
		{
			throw new IllegalArgumentException("plugin can't be null");
		}

		this.plugin=plugin;
		this.isLoaded=isLoaded;
	}
	public String getPluginClassName()
	{
		return plugin.getClass().getName();
	}
	public String getVersion()
	{
		return plugin.getVersion();
	}
	/**
	 * @return the isLoaded
	 */
	public boolean isLoaded() {
		return this.isLoaded;
	}
	/**
	 * @param isLoaded the isLoaded to set
	 */
	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}
	/**
	 * @return the plugin
	 */
	public IPlugin getPlugin() {
		return this.plugin;
	}
	/**
	 * @param plugin the plugin to set
	 */
	public void setPlugin(IPlugin plugin) {
		this.plugin = plugin;
	}
	/**
	 * @return
	 * @see com.coolsql.plugin.IPlugin#getAuthor()
	 */
	public String getAuthor() {
		return this.plugin.getAuthor();
	}
	/**
	 * @return
	 * @see com.coolsql.plugin.IPlugin#getContributors()
	 */
	public String getContributors() {
		return this.plugin.getContributors();
	}
	/**
	 * @return
	 * @see com.coolsql.plugin.IPlugin#getDescriptiveName()
	 */
	public String getDescriptiveName() {
		return this.plugin.getDescriptiveName();
	}
	/**
	 * @return
	 * @see com.coolsql.plugin.IPlugin#getInternalName()
	 */
	public String getInternalName() {
		return this.plugin.getInternalName();
	}
	/**
	 * @return
	 * @see com.coolsql.plugin.IPlugin#getWebSite()
	 */
	public String getWebSite() {
		return this.plugin.getWebSite();
	}
	
}
