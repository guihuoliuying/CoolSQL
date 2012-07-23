/**
 * 
 */
package com.cattsoft.coolsql.system.menubuild;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * ��ȡ�����ļ��д�ŵ�ͼ����Ϣ�����ָ����key�ҵ���Ӧ��ͼ���ļ�
 * @author kenny liu
 *
 * 2007-11-11 create
 */
public class IconResource {
	public static final Icon ICON_COPY=PublicResource.getIcon("TextMenu.icon.copy");
	public static final Icon ICON_CUT=PublicResource.getIcon("TextMenu.icon.cut");
	public static final Icon ICON_PASTE=PublicResource.getIcon("TextMenu.icon.paste");
	
	private static ResourceBundle rb=null;
	private static Icon blankIcon;
	static
	{
		initBundler();
	}
	private static void initBundler()
	{
		rb=ResourceBundle.getBundle("com.coolsql.system.icon");
	}
	public static Icon getIcon(String key)
	{
		String path=null;
		try
		{
		  path=rb.getString(key);
		}catch(Exception e)
		{
			e.printStackTrace();
			LogProxy.getProxy().error(e);
			return null;
		}
		URL url=IconResource.class.getResource(path);
        if(url != null)
            return new ImageIcon(url);
        return null;
	}
	/**
	 * �ӱ��ػ�ȡ�ļ�
	 * @param path
	 * @return  --�����Ҳ�����Ӧ��ͼƬ�ļ���������null
	 */
	public static Icon getLocalIcon(String path)
	{
		File file=new File(path);
		if(!file.exists())
		{
			return null;
		}
		return new ImageIcon(path);
	}
	public static Icon getIconResource(String path)
	{
		return getIconResource(path,null);
	}
	public static Icon getIconResource(String path,Class loadClass)
	{
		if(loadClass==null)
			loadClass=IconResource.class;
		URL url=loadClass.getResource(path);
        if(url != null)
            return new ImageIcon(url);
        return null;
	}
	/**
	 * ��ȡû��ɫ�ص�icon
	 * @return
	 */
	public static Icon getBlankIcon()
	{
		if(blankIcon==null)
			blankIcon=getIcon("system.blankicon");
		return blankIcon;
	}
}
