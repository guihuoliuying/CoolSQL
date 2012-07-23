/**
 * 
 */
package com.cattsoft.coolsql.action.framework;

import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.menubuild.IconResource;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-14 create
 */
public class ActionResourceMgr {

	private ResourceManage stringMgr = null;
	private static ActionResourceMgr arMgr=null;
	
	public static ActionResourceMgr getInstance()
	{
		if(arMgr==null)
		{
			arMgr=new ActionResourceMgr();
		}
		return arMgr;
	}
	
	private ActionResourceMgr()
	{
		stringMgr = new ResourceManage();
	}
	/**
	 * Add all resource from ResourceBundle into current resource repository.
	 * @param _rsrcBundle the resource bundle object.
	 */
	public void addAllResource(ResourceBundle _rsrcBundle) {
		if (_rsrcBundle == null) {
			return;
		}
		stringMgr.addAllResource(_rsrcBundle);
	}
	protected String getString(String key)
	{
		return stringMgr.getString(key);
	}
	public String getLabel(String key)
	{
		return getString(key+"_label");
	}
	public ImageIcon getIcon(String key)
	{
		String path=null;
		try
		{
		  path=stringMgr.getString(key+"_icon");
		  if(path==null)
			  return null;
		}catch(Exception e)
		{
			LogProxy.getProxy().error(e);
			return null;
		}
		URL url=IconResource.class.getResource(path);
        if(url != null)
            return new ImageIcon(url);
        return null;
	}
	public ImageIcon getIcon(String key, Class<?> actionClass) {
		String path = null;
		try {
			path = stringMgr.getString(key + "_icon");
			if (path == null)
				return null;
		} catch (Exception e) {
			LogProxy.getProxy().error(e);
			return null;
		}
		URL url = actionClass.getResource(path);
		if (url != null)
			return new ImageIcon(url);
		return null;
	}
	public String getTooltip(String key)
	{
		return getString(key+"_tt");
	}
	public KeyStroke getKeyStroke(String key)
	{
		return KeyStroke.getKeyStroke(getString(key+"_ks"));
	}
	public String getDescription(String aKey)
	{
		return getDescription(aKey, false);
	}
	
	/**
	 *    Returns the description associcate with the given key.
	 *    This is used for Tooltips which are associated with a
	 *    certain menu text etc.
	 */
	public String getDescription(String aKey, boolean replaceModifiers)
	{
		String value = getString("d_" + aKey);
		if (replaceModifiers)
		{
			value = replaceModifierText(value);
		}
		return value;
	}

	public InputStream getDefaultSettings()
	{
		InputStream in = ActionResourceMgr.class.getResourceAsStream("default.properties");

		return in;
	}

	public ImageIcon getLargeImage(String aKey)
	{
		return retrieveImage(aKey + "24", ".gif");
	}
	private static ImageIcon retrieveImage(String filename, String extension)
	{
		URL imageIconUrl = ActionResourceMgr.class.getClassLoader().getResource("workbench/resource/images/" + filename + extension);
		if (imageIconUrl != null)
		{
			return new ImageIcon(imageIconUrl);
		}
		else
		{
			imageIconUrl = ActionResourceMgr.class.getClassLoader().getResource(filename);
			if (imageIconUrl != null)
			{
				return new ImageIcon(imageIconUrl);
			}
		}
		return null;
	}
	public String replaceModifierText(String msg)
	{
		msg = StringUtil.replace(msg, "%shift%", KeyEvent.getKeyModifiersText(KeyEvent.SHIFT_MASK));
		msg = StringUtil.replace(msg, "%control%", KeyEvent.getKeyModifiersText(KeyEvent.CTRL_MASK));
		return msg;
	}
}
