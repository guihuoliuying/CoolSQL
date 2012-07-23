package com.cattsoft.coolsql.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.cattsoft.coolsql.action.framework.ShortcutManager;
import com.cattsoft.coolsql.bookmarkBean.BookmarkEvent;
import com.cattsoft.coolsql.bookmarkBean.BookmarkListener;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.FontChangedListener;
import com.cattsoft.coolsql.pub.util.MyLocale;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.lookandfeel.SystemLookAndFeel;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * Config setting.
 * @author ��Т��(kenny liu)
 *
 * 2008-9-13 create
 */
public class Setting implements ISetting{

	private static Setting setting=null;
	
	private ParamProperties props;
	private List<FontChangedListener> fontChangeListeners = new LinkedList<FontChangedListener>();
	private String configDir;
	
	private String filename;
	private Map<String,DatabaseSetting> dbProperties;
	
	private ShortcutManager keyManager;
	private Setting(){
		
		props=new ParamProperties();
		configDir=SystemConstant.userPath;
		this.filename=SystemConstant.SETTING_FILE;
		dbProperties=Collections.synchronizedMap(new HashMap<String,DatabaseSetting>());

		try
		{
			
			props.loadTextFile(this.filename);
		}
		catch (IOException e)
		{
			props=getDefaultProperties();
		}
		getShortcutManager();//init shortcut information
		
	}
	public ShortcutManager getShortcutManager()
	{
		if (this.keyManager == null)
		{
			this.keyManager = new ShortcutManager(this.getShortcutFilename());
		}
		return this.keyManager;
	}
	public synchronized static Setting getInstance()
	{
		if(setting==null)
		{
			setting=new Setting();
			BookmarkManage.getInstance().addBookmarkListener(new BookmarkListener()
			{

				public void bookmarkAdded(BookmarkEvent e) {
					String bookmark=e.getBookmark().getAliasName();
					setting.dbProperties.put(bookmark, new DatabaseSetting());
						
				}

				public void bookmarkDeleted(BookmarkEvent e) {
					String bookmark=e.getBookmark().getAliasName();
					setting.dbProperties.remove(bookmark);	
				}

				public void bookMarkUpdated(BookmarkEvent e) {
					
				}
				
			}
			);
		}
		return setting;
	}
	public void reload() {
		
	}
	public Font getEditorFont()
	{
		return this.getFontProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_EDITOR_FONT, new Font("DialogInput",Font.PLAIN,12));
	}
	public String getProperty(String aProperty, String aDefault)
	{
		return System.getProperty(aProperty, this.props.getProperty(aProperty, aDefault));
	}
	public boolean getRightClickMovesCursor()
	{
		return this.getBoolProperty("view.editor.rightclickmovescursor", false);
	}
	public Color getEditorSelectionColor()
	{
		return getColorProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SELECTIONCOLOR, new Color(0xccccff));
	}
	public Color getEditorErrorColor()
	{
		return getColorProperty("view.editor.color.error", Color.RED.brighter());
	}
	public int getFormatterMaxColumnsInSelect()
	{
		return getIntProperty("sql.formatter.select.columnsperline", 1);
	}
	public String getEditorNoWordSep()
	{
		return getProperty("view.editor.nowordsep", "");
	}
	public int getRectSelectionModifier()
	{
		String mod = getProperty("view.editor.rectselection.modifier", "alt");
		if (mod.equalsIgnoreCase("ctrl"))
		{
			return InputEvent.CTRL_MASK;
		}
		return InputEvent.ALT_MASK;
	}
	public String getDefaultFileEncoding()
	{
		String def = System.getProperty("file.encoding");
		return getProperty("coolsql.file.encoding", def);
	}
	public int getEditorTabWidth()
	{
		return getIntProperty(PropertyConstant.PROPERTY_EDITOR_TAB_WIDTH, 2);
	}
	/**
	 * Indicate whether the line number should be displayed in the SQL editor.
	 * @return return true if the line number should be displayed, otherwise return false.
	 */
	public boolean getShowLineNumbers()
	{
		return getBoolProperty(PropertyConstant.PROPERTY_SHOW_LINE_NUMBERS, true);
	}
	public int getInMemoryScriptSizeThreshold()
	{
		// Process scripts up to 1 MB in memory
		// this is used by the ScriptParser
		return getIntProperty("sql.script.inmemory.maxsize", 1024 * 1024);
	}

	public void setInMemoryScriptSizeThreshold(int size)
	{
		setIntProperty("sql.script.inmemory.maxsize", size);
	}
	public int getFormatterMaxSubselectLength()
	{
		return getIntProperty("sql.formatter.subselect.maxlength", 60);
	}

	public void setFormatterMaxSubselectLength(int value)
	{
		setIntProperty("sql.formatter.subselect.maxlength", value);
	}
	public int getIntProperty(String aProperty, int defaultValue)
	{
		String sysValue = System.getProperty(aProperty, null);
		if (sysValue != null)
		{
			return StringUtil.getIntValue(sysValue, defaultValue);
		}
		return this.props.getIntProperty(aProperty, defaultValue);
	}
	public long getLongProperty(String aProperty, long defaultValue)
	{
		String sysValue = System.getProperty(aProperty, null);
		if (sysValue != null)
		{
			return StringUtil.getLongValue(sysValue, defaultValue);
		}
		return this.props.getLongProperty(aProperty, defaultValue);
	}
	public boolean getBoolProperty(String property)
	{
		return getBoolProperty(property, false);
	}

	public boolean getBoolProperty(String property, boolean defaultValue)
	{
		String sysValue = System.getProperty(property, null);
		if (sysValue != null)
		{
			return StringUtil.stringToBool(sysValue);
		}
		return this.props.getBoolProperty(property, defaultValue);
	}
	
	public Color getColorProperty(String aColorKey, Color defaultColor)
	{
		String value = this.getProperty(aColorKey, null);
		if (value == null) return defaultColor;
		String[] colors = value.split(",");
		if (colors.length != 3) return defaultColor;
		
		return getColorByString(value, defaultColor);

	}
	/**
	 * Transfer the string to a color object. The specified string must match the format: "red,green,blue".
	 * @param colorStr a string value
	 * @param defaultValue return this value if the specified string value is llegal.
	 * @return return a color object. It'll return a default color value if the specified string value is llegal.
	 */
	private Color getColorByString(String colorStr, Color defaultValue) {
		if (colorStr == null) {
			return defaultValue;
		} else {
			String[] colors = colorStr.split(",");
			if (colors.length != 3) {
				return defaultValue;
			}
			try
			{
				int r = StringUtil.getIntValue(colors[0]);
				int g = StringUtil.getIntValue(colors[1]);
				int b = StringUtil.getIntValue(colors[2]);
				return new Color(r,g,b);
			}
			catch (Exception e)
			{
				return defaultValue;
			}
		}
			
	}
	public Color setColorProperty(String key, Color c)
	{
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		String value = Integer.toString(r) + "," + Integer.toString(g) + "," + Integer.toString(b);
		Object oldValue = this.setProperty(key, value);
		if (oldValue == null) {
			return null;
		} else {
			return getColorByString(oldValue.toString(), c);
		}
	}
	public Object setProperty(String property, String aValue)
	{
		return this.props.setProperty(property, aValue);
	}
	public Boolean setBooleanProperty(String property, boolean value)
	{
		Object oldValue = this.props.setProperty(property, value);
		if (oldValue != null) {
			return new Boolean(oldValue.toString());
		}else {
			return null;
		}
	}
	public Integer setIntProperty(String aProperty, int aValue)
	{
		Object oldValue = this.props.setProperty(aProperty, Integer.toString(aValue));
		if (oldValue != null) {
			return new Integer(oldValue.toString());
		}else {
			return null;
		}
	}
	public Long setLongProperty(String aProperty, long aValue)
	{
		Object oldValue = this.props.setProperty(aProperty, Long.toString(aValue));
		if (oldValue != null) {
			return new Long(oldValue.toString());
		}else {
			return null;
		}
	}
	public void fireFontChangedEvent(String aKey, Font aFont)
	{
		FontChangedListener listener=null;
		for (int i=0;i< fontChangeListeners.size();i++)
		{
			if (listener != null)	listener.fontChanged(aKey, aFont);
		}
	}
	public void addFontChangedListener(FontChangedListener aListener)
	{
		this.fontChangeListeners.add(aListener);
	}

	public synchronized void removeFontChangedListener(FontChangedListener aListener)
	{
		this.fontChangeListeners.remove(aListener);
	}
	public synchronized void addPropertyChangeListener(PropertyChangeListener l,String property)
	{
		this.props.addPropertyChangeListener(l, property);
	}
	public synchronized void addPropertyChangeListener(PropertyChangeListener l,String[] properties)
	{
		this.props.addPropertyChangeListener(l, properties);
	}
	public void removePropertyChangeLister(PropertyChangeListener l)
	{
		this.props.removePropertyChangeListener(l);
	}
	private String getShortcutFilename()
	{
		return new File(this.configDir, "shortcuts.xml").getAbsolutePath();
	}
	/**
	 *	Returns the font configured for this keyword
	 */
	public Font getFont(String aFontName, String defaultValue)
	{
		String sf=getProperty(aFontName, defaultValue);
		return transString2Font(sf);
	}
	public Font getFontProperty(String aFontName, Font f)
	{
		String sf=this.props.getProperty(aFontName);
		if(sf==null)
			return f;
		return transString2Font(sf);
	}
	public Font getSystemFont()
	{
		String sf=getProperty("system.font", "Courier,0,12");
		return transString2Font(sf);
	}
	public void setSystemFont(Font font)
	{
		String str=font.getFamily()+","+font.getStyle()+","+font.getSize();
		setProperty("system.font", str);
	}
	public Font setFontProperty(String propertyName,Font f)
	{
		String str=font2String(f);
		Object oldValue = setProperty(propertyName, str);
		if (oldValue == null) {
			return null;
		} else {
			return transString2Font(oldValue.toString());
		}
	}
	public void saveSetting()
	{
		if(keyManager!=null)
			keyManager.saveSettings();
		try
		{
			this.props.saveToFile(this.filename);
		}
		catch (IOException e)
		{
			LogProxy.errorLog("Error saving Settings file '" + filename + "'", e);
		}
		SystemLookAndFeel.getInstance().saveToFile();
	}
	public static String font2String(Font f)
	{
		if(f==null)
			return "";
		return f.getFamily()+","+f.getStyle()+","+f.getSize();
	}
	public static Font transString2Font(String str)
	{
		str=StringUtil.trim(str);
		if(str.equals(""))
			return null;
		String[] s=str.split(",");
		if(s.length!=3)
			return null;
		int style=Font.PLAIN;
		try
		{
			style=Integer.parseInt(s[1]);
		}catch(Exception e){}
		int size=12;
		try
		{
			size=Integer.parseInt(s[2]);
		}catch(Exception e)
		{}
		return new Font(s[0],style,size);
	}
	/**
	 * ��ȡȱʡ����������
	 * @return
	 */
	private ParamProperties getDefaultProperties()
	{
		ParamProperties defProps = new ParamProperties(this);
		InputStream in = getDefaultPropertiesFile();
		try
		{
			defProps.load(in);
		}
		catch (IOException e)
		{
			e.printStackTrace();
//			logger.error("Could not read default settings", e);
		}
		finally
		{
			try { 
				if(in!=null)
					in.close(); 
			} catch(Throwable th) {}
		}
		return defProps;
	}
	/**
	 * return the  setting of database specified by bookmark;
	 * @param bookmark --bookmark name that represents database
	 * @return
	 */
	public DatabaseSetting getDatabaseSetting(String bookmark)
	{
		if(bookmark==null)
			return null;
		DatabaseSetting dbp=dbProperties.get(bookmark);
		if(dbp==null)
		{
			dbp=new DatabaseSetting();
			dbProperties.put(bookmark, dbp);
		}
		return dbp;
	}
//	public void setLanguage(Locale locale)
//	{
//		setProperty(PropertyConstant.PROPERTY_LANGUAGE, locale.getLanguage());
//	}
	
	public List<MyLocale> getLanguages()
	{
		String prop = getProperty("coolsql.language.available", "en,zh");
		List<String> codes = StringUtil.stringToList(prop, ",", true, true, false);
		List<MyLocale> result = new ArrayList<MyLocale>(codes.size());
		for (String c : codes)
		{
			try
			{
				result.add(new MyLocale(new Locale(c)));
			}
			catch (Exception e)
			{
				LogProxy.errorReport("Invalid locale specified: " + c, e);
			}
		}
		return result;
	}
	
	public Locale getLanguage()
	{
		String lanCode =PropertyManage.getLaunchSetting().getLanguage();
		if(lanCode==null)
			lanCode="en";
//			getProperty(PropertyConstant.PROPERTY_LANGUAGE, "cn");
		Locale l = null;
		try
		{
			l = new Locale(lanCode);
		}
		catch (Exception e)
		{
			LogProxy.errorReport("Error creating Locale for language=" + lanCode, e);
			l = new Locale("en");
		}
		return l;
	}
	/**
	 * ϵͳȱʡ��������á�
	 * @return
	 */
	public static InputStream getDefaultPropertiesFile()
	{
		return Setting.class.getResourceAsStream("/com/coolsql/pub/display/defaultsetting.properties");
	}
}
