/**
 * 
 */
package com.cattsoft.coolsql.system.lookandfeel;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.cattsoft.coolsql.pub.component.BasePanel;
import com.cattsoft.coolsql.pub.component.MyMetalTheme;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.xml.XMLBeanUtil;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.SystemConstant;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.BasicOffice2003Theme;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.utils.SystemInfo;

/**
 * System look and feel manage.
 * 
 * @author kenny liu
 * 
 * 2007-11-11 create
 */
public class SystemLookAndFeel {

	public static final String DEFAULT_LAF_NAME_METAL = "Metal";
	public static final String DEFAULT_LAF_NAME_OFFICE2003 = "office2003";
	public static final Color DEFAULT_THEMECOLOR = new Color(155, 204, 181);
	public static final Color THEME_COLOR_WINDOW = new Color(169,200,235);
	
	private static SystemLookAndFeel instance;
	
	private Map<String,LookAndFeelMetaData> lafMap;
	
	//Current look and feel which has been applied to application.
	private String currentLAFName;
	//User may change the current Look and feel. 
	//This variable saves the value selected by user before application quits.
	private String oldLAFName;
	
	private List<String> systemLAF;
	
	private Color viewThemeColor;
	public synchronized static SystemLookAndFeel getInstance()
	{
		if(instance==null)
			instance=new SystemLookAndFeel();
		
		return instance;
	}
	private SystemLookAndFeel()
	{
		lafMap=new HashMap<String,LookAndFeelMetaData>();
		
		systemLAF=new ArrayList<String>();
		/**
		 *installed look and feel by jdk.
		 */
		LookAndFeelInfo[] infos=UIManager.getInstalledLookAndFeels();
		for(LookAndFeelInfo info:infos)
		{
			LookAndFeelMetaData data = new LookAndFeelMetaData(info.getName(),
					info.getClassName(), "rt.jar");
			addLAF(data);
			systemLAF.add(data.getName());
		}
		
		/**Add jide look and feel. */
		//xerto
		LookAndFeelMetaData data = new LookAndFeelMetaData("xerto",
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel", "jide-oss-x.jar");
		addLAF(data);
		systemLAF.add(data.getName());
		//office2003
		data = new LookAndFeelMetaData("office2003",
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel", "jide-oss-x.jar");
		addLAF(data);
		systemLAF.add(data.getName());
		//vsnet
		data = new LookAndFeelMetaData("vsnet",
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel", "jide-oss-x.jar");
		addLAF(data);
		systemLAF.add(data.getName());
		
		loadFromFile();
	}
	/**
	 * Validate whether specified LAF is IDE look and feel.
	 */
	private boolean isIDELookAndFeel(String lookAndFeelName)
	{
		return lookAndFeelName.equals("xerto")||lookAndFeelName.equals("office2003")||lookAndFeelName.equals("vsnet");
	}
	/**
	 * Change the look and feel, this method only saves the name of look and feel without taking effect
	 */
	public void changeLookAndFeel(String lookAndFeel)
	{
		this.oldLAFName=lookAndFeel;
	}
	public boolean isLookAndFeelChanged()
	{
		if(oldLAFName==null)
			return false;
		return !currentLAFName.equals(oldLAFName);
	}
	public boolean isIDELookAndFeelInstalled()
	{
		Boolean b=(Boolean)UIManager.get(LookAndFeelFactory.JIDE_EXTENSION_INSTALLLED);
		if(b==null)
			return false;
		else
			return b;
	}
	public boolean isMetalLookAndFeel()
	{
		return currentLAFName.equals(DEFAULT_LAF_NAME_METAL);
	}
	public void installLookAndFeel(String name)
	{
		currentLAFName=name;
		LookAndFeelMetaData data=lafMap.get(currentLAFName);
		if(!isAvailableLookAndFeel(data.getLafClass()))
		{
			currentLAFName=DEFAULT_LAF_NAME_METAL;
			data=lafMap.get(currentLAFName);
		}
		if(isIDELookAndFeel(data.getName()))
		{
			if("xerto".equals(data.getName()))
			{
				 LookAndFeelFactory.setDefaultStyle(LookAndFeelFactory.XERTO_STYLE);
			}else if("office2003".equals(data.getName()))
			{
				LookAndFeelFactory.setDefaultStyle(LookAndFeelFactory.OFFICE2003_STYLE);
				BasicOffice2003Theme theme = new BasicOffice2003Theme("Custom");
			    theme.setBaseColor(Color.green, true, "custom");
		        ((Office2003Painter) Office2003Painter.getInstance()).addTheme(theme);
			}else if("vsnet".equals(data.getName()))
			{
				LookAndFeelFactory.setDefaultStyle(LookAndFeelFactory.VSNET_STYLE);
			}
			LookAndFeelFactory.installDefaultLookAndFeelAndExtension();;
			installDefaultWindowsLookAndFeel();
			viewThemeColor=THEME_COLOR_WINDOW;
		}else
		{
			if("Metal".equals(data.getName()))
			{
				MetalLookAndFeel.setCurrentTheme(new MyMetalTheme());
			}
			try {
				UIManager.setLookAndFeel(data.getLafClass());
			} catch (Exception e) {
				LogProxy.errorLog("install look and feel failed!"+e.getMessage(),e);
			} 
			if("Windows".equals(data.getName())||"Windows Classic".equals(data.getName()))
			{
//				installDefaultWindowsLookAndFeel();
				viewThemeColor=THEME_COLOR_WINDOW;
			}
			installDefaultWindowsLookAndFeel();
		}
		BasePanel.setThemeColor(viewThemeColor);
	}
	public void addLAF(LookAndFeelMetaData lafData)
	{
		if(lafData==null)
			return;
		String name=lafData.getName();
		if(name==null||name.trim().equals(""))
			return;
		
		lafMap.put(name, lafData);
	}
	public List<String> getLAFNames()
	{
		List<String> list=new ArrayList<String>(lafMap.keySet());
		return list;
	}
	public LookAndFeelMetaData getLAFMetaDataByName(String name)
	{
		return lafMap.get(name);
	}
	public int size()
	{
		return lafMap.size();
	}
	public Collection<LookAndFeelMetaData> getMetaDatas()
	{
		return lafMap.values();
	}
	public Color getViewThemeColor()
	{
		return viewThemeColor;
	}
	@SuppressWarnings("unused")
	private String changeColorToString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append(viewThemeColor.getAlpha()+",").append(viewThemeColor.getRed()+",")
			.append(viewThemeColor.getGreen()+",").append(viewThemeColor.getBlue());
		
		return sb.toString();
	}
	@SuppressWarnings("unused")
	private Color stringToColor(String str)
	{
		if(str==null)
			return DEFAULT_THEMECOLOR;
		String[] parts=str.split(",");
		try
		{
			return new Color(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),Integer.parseInt(parts[3]),Integer.parseInt(parts[0]));
		}catch(Exception e)
		{
			return DEFAULT_THEMECOLOR;
		}
	}
	private static final String LAF_DEFAULT="defaultlaf";
//	private static final String VIEWTHEMECOLOR="viewThemeColor";
	@SuppressWarnings("unchecked")
	private void loadFromFile()
	{
		try {
			Map<Object,Object> tmpData=(Map<Object,Object>)new XMLBeanUtil().importBeanFromXML(SystemConstant.userPath+"LAF.xml");
			
			if(tmpData!=null)
			{
				currentLAFName=(String)tmpData.remove(LAF_DEFAULT);
//				viewThemeColor=stringToColor((String)tmpData.remove(VIEWTHEMECOLOR));
				Iterator it=tmpData.keySet().iterator();
				while(it.hasNext())
				{
					String key=(String)it.next();
					Object ob=tmpData.get(key);
					if(ob instanceof LookAndFeelMetaData)
					{
						lafMap.put(key, (LookAndFeelMetaData)ob);
					}
				}
			}
			if(currentLAFName==null)
			{
				if (SystemInfo.isWindows()) {
					currentLAFName = DEFAULT_LAF_NAME_OFFICE2003;
				} else {
					currentLAFName = DEFAULT_LAF_NAME_METAL;
				}
			}
			if(viewThemeColor==null)
			{
				viewThemeColor = DEFAULT_THEMECOLOR;
			}
		} catch (XMLException e) {
			LogProxy.errorReport("Load look and feel information error:"+e.getMessage(),e);
		}
	}
	/**
	 * Save all look and feel information into local file.
	 * file path is $userpath+LAF.xml.
	 */
	public void saveToFile()
	{
		//Remove system look and feel before saving information into local file.
		for(String name:systemLAF)
		{
			lafMap.remove(name);
		}
		
		try {
			Map<Object,Object> tempMap=new HashMap<Object,Object>();
			tempMap.putAll(lafMap);
			
			if(oldLAFName!=null)
				currentLAFName=oldLAFName;
			tempMap.put(LAF_DEFAULT, getCurrentLAFName());
//			tempMap.put(VIEWTHEMECOLOR, changeColorToString());
			new XMLBeanUtil().exportBeanToXML(new File(SystemConstant.userPath+"LAF.xml"), tempMap, "laf");
		} catch (XMLException e) {
			LogProxy.errorReport("Save the look and feel information error:"+e.getMessage(),e);
		}
	}
	private static void installDefaultWindowsLookAndFeel()
	{
		UIDefaults defaults=UIManager.getLookAndFeelDefaults();
		Font f=Setting.getInstance().getSystemFont();
		Object[] keys = defaults.keySet().toArray(new Object[defaults.size()]);
		Map<Object, Object> tmpValues = new HashMap<Object, Object>();
		for (Object key : keys) {
			Object v = defaults.get(key);
			if (v instanceof Font) {
				tmpValues.put(key, f);
			}
		}
		
		defaults.putAll(tmpValues);
	}
	public static void changeOffice2003Theme(Color newColor,String prefix)
	{
		BasicOffice2003Theme theme = new BasicOffice2003Theme("Custom");
		theme.setBaseColor(newColor, true, prefix);
		((Office2003Painter) Office2003Painter.getInstance()).addTheme(theme);
		LookAndFeelFactory.setDefaultStyle(LookAndFeelFactory.OFFICE2003_STYLE);
		LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
	}
	/**
	 * 
	 * @param laf the class name of a look and feel 
	 * @return return true if the specified look and feel is supported by current operating system, otherwise return false.
	 */
	public static boolean isAvailableLookAndFeel(String laf) {
		try {
			Class<?> lnfClass = LoadJar.getInstance().getClassLoader().loadClass(laf);
			LookAndFeel newLAF = (LookAndFeel) (lnfClass.newInstance());
			return newLAF.isSupportedLookAndFeel();
		} catch (Exception e) { // If ANYTHING weird happens, return false
			return false;
		}
	}
	/**
	 * @return the currentLAFName
	 */
	public String getCurrentLAFName() {
		return this.currentLAFName;
	}
	public String getChangedLAFName()
	{
		if(oldLAFName!=null)
			return oldLAFName;
		else
			return currentLAFName;
	}
	/**
	 * @param viewThemeColor the viewThemeColor to set
	 */
	public void setViewThemeColor(Color viewThemeColor) {
		this.viewThemeColor = viewThemeColor;
	}
}
