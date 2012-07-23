/**
 * 
 */
package com.cattsoft.coolsql.gui.property.global;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.gui.property.PropertyInterface;
import com.cattsoft.coolsql.pub.component.NumberEditor;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.pub.util.MyLocale;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.system.Setting;
import com.l2fprod.common.swing.JFontChooser;

/**
 * property setting for global applying
 * @author ��Т��(kenny liu)
 *
 * 2008-1-17 create
 */
public class GeneralPropertyPane extends BasePropertySettingPane {

	public static final String PROPERTY_LANGUAGE="language";
	public static final String PROPERTY_SHOW_TABLE_LINE_NUMBER="showtablelinenumber";
	public static final String PROPERTY_SHOW_TABLE_HORIZONTAL_LINE="showhorizontalline";
	public static final String PROPERTY_SHOW_TABLE_VERTICAL_LINE="showverticalline";
	
	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(GeneralPropertyPane.class);

	private TextEditor fontName;
	private JComboBox languageBox;
	private JCheckBox showTableLineNumber;
	private JCheckBox showHorizontalLine;
	private JCheckBox showVerticalLine;
	private NumberEditor historySQLDays;
	private NumberEditor sqlHistorySize;
	private NumberEditor maxsizeOfFavorite;
	private JCheckBox resizeRowHeight;
	private NumberEditor tableRowHeight;
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyPane#initContent()
	 */
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#apply()
	 */
	public void apply() {
		save();
	}


	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#set()
	 */
	public boolean set() {
		try
		{
			save();
			return true;
		}catch(Throwable e)
		{
			return false;
		}
		
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
	 */
	public void setData(Object ob) {
		double b=10;
		double[][] model=new double[][]{{b,TableLayout.PREFERRED,TableLayout.PREFERRED,TableLayout.FILL,b},
				{b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.FILL,b}};
		JPanel contentPane = new JPanel(new TableLayout(model));
		int rowIndex=0,columnIndex=0;
		
		//language .
		rowIndex++;
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.language.label")),
				""+columnIndex+","+rowIndex+",l,c");
		languageBox=new JComboBox();
		languageBox.putClientProperty(PropertyInterface.PROPERTY_NAME,PROPERTY_LANGUAGE);
		List<MyLocale> languages=Setting.getInstance().getLanguages();
		for(MyLocale language:languages)
		{
			languageBox.addItem(language);
		}
		languageBox.setSelectedItem(new MyLocale(Setting.getInstance().getLanguage()));
		columnIndex++;
		contentPane.add(languageBox,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		//system font
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.systemfont.label")),
				""+columnIndex+","+rowIndex+",l,c");
		fontName = new TextEditor(25);
		fontName.setEditable(false);
	    fontName.setMaximumSize(new java.awt.Dimension(45, 22));
	    fontName.setMinimumSize(new java.awt.Dimension(45, 22));
	    fontName.setPreferredSize(new java.awt.Dimension(45, 22));
	    Font f=Setting.getInstance().getSystemFont();
		fontName.setText(transFont2String(f));
		fontName.setFont(f);
	    fontName.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_SYSTEM_FONT);
		columnIndex++;
		contentPane.add(fontName,""+columnIndex+","+rowIndex+",l,c");
		columnIndex++;
		JButton changeButton=new JButton("...");
		changeButton.setMaximumSize(new java.awt.Dimension(22, 22));
		changeButton.setMinimumSize(new java.awt.Dimension(22, 22));
		changeButton.setPreferredSize(new java.awt.Dimension(22, 22));
		changeButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				Font selectedFont=JFontChooser.showDialog(GUIUtil.findLikelyOwnerWindow(), 
						"Select system font", transString2Font(fontName.getText()));
				if(selectedFont!=null)
				{
					fontName.setText(transFont2String(selectedFont));
					fontName.setFont(selectedFont);
				}
			}
			
		});
		contentPane.add(changeButton,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=3;
		rowIndex+=2;
		
		//Show line number in table component.
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.showlinenumberintable.label")),
				""+columnIndex+","+rowIndex+",l,c");
		showTableLineNumber=new JCheckBox();
		showTableLineNumber.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_ISDISPLAYLINENUMBER,true));
		showTableLineNumber.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_COMMONTABLE_ISDISPLAYLINENUMBER);
		columnIndex++;
		contentPane.add(showTableLineNumber,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		//show horizontal line
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.showhorizontalline.label")),
				""+columnIndex+","+rowIndex+",l,c");
		showHorizontalLine=new JCheckBox();
		showHorizontalLine.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_HORIZONTALLINE,true));
		showHorizontalLine.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_HORIZONTALLINE);
		columnIndex++;
		contentPane.add(showHorizontalLine,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		//show vertical line
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.showverticalline.label")),
				""+columnIndex+","+rowIndex+",l,c");
		showVerticalLine=new JCheckBox();
		showVerticalLine.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_VERTICALLINE,true));
		showVerticalLine.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_VERTICALLINE);
		columnIndex++;
		contentPane.add(showVerticalLine,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		// reserved days for history sql
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.historyreserveddays.label")),
				""+columnIndex+","+rowIndex+",l,c");
		historySQLDays=new NumberEditor(3,0);
		historySQLDays.setText(Setting.getInstance().
				getProperty(PropertyConstant.PROPERTY_SYSTEM_HISTORYDAYS,"15"));
		historySQLDays.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_SYSTEM_HISTORYDAYS);
		columnIndex++;
		contentPane.add(historySQLDays,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		//sqlHistorySize
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.sqlhistorysize.label")),
				""+columnIndex+","+rowIndex+",l,c");
		sqlHistorySize=new NumberEditor(3,0);
		sqlHistorySize.setText(Setting.getInstance().
				getProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE,"200"));
		sqlHistorySize.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE);
		columnIndex++;
		contentPane.add(sqlHistorySize,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		//maxsizeOfFavorite
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.maxsizeOfFavorite.label")),
				""+columnIndex+","+rowIndex+",l,c");
		maxsizeOfFavorite=new NumberEditor(3,0);
		maxsizeOfFavorite.setText(Setting.getInstance().
				getProperty(PropertyConstant.PROPERTY_FAVORITE_DISPLAY_MAXSIZE,"15"));
		maxsizeOfFavorite.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_FAVORITE_DISPLAY_MAXSIZE);
		columnIndex++;
		contentPane.add(maxsizeOfFavorite,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		//enable resizing the row height of table.
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.resizetablerowheight.label")),
				""+columnIndex+","+rowIndex+",l,c");
		resizeRowHeight=new JCheckBox();
		resizeRowHeight.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_RESIZEROWHEIGHT,false));
		resizeRowHeight.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_COMMONTABLE_RESIZEROWHEIGHT);
		columnIndex++;
		contentPane.add(resizeRowHeight,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		//tableRowHeight
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("global.setting.general.tablerowheight")),
				""+columnIndex+","+rowIndex+",l,c");
		tableRowHeight=new NumberEditor(5,0);
		tableRowHeight.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_COMMONTABLE_ROWHEIGHT, "18"));
		tableRowHeight.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_COMMONTABLE_ROWHEIGHT);
		columnIndex++;
		contentPane.add(tableRowHeight,""+columnIndex+","+rowIndex+",l,c");
		
		columnIndex-=2;
		rowIndex+=2;
		
		//restore to default
		columnIndex+=3;
		JButton restoreButton=createRestoreButton();
		contentPane.add(restoreButton,""+columnIndex+","+rowIndex+",r,b");
		
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(contentPane), BorderLayout.CENTER);
	}
	@Override
	protected void restoreToDefault()
	{
//		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_LANGUAGE, null);
		Setting.getInstance().setProperty("system.font", null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_COMMONTABLE_ISDISPLAYLINENUMBER, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_HORIZONTALLINE, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_VERTICALLINE, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_SYSTEM_HISTORYDAYS,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_FAVORITE_DISPLAY_MAXSIZE,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_COMMONTABLE_RESIZEROWHEIGHT,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_COMMONTABLE_ROWHEIGHT,null);
		refreshDisplay();
	}
	private void refreshDisplay()
	{
		Font f=Setting.getInstance().getSystemFont();
		fontName.setText(transFont2String(f));
		fontName.setFont(f);
		
//		languageBox.setSelectedItem(new MyLocale(Setting.getInstance().getLanguage()));
		
		showTableLineNumber.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_ISDISPLAYLINENUMBER,true));
		
		showHorizontalLine.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_HORIZONTALLINE,true));
		
		showVerticalLine.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_VERTICALLINE,true));
		
		historySQLDays.setText(Setting.getInstance().
				getProperty(PropertyConstant.PROPERTY_SYSTEM_HISTORYDAYS,"15"));
		sqlHistorySize.setText(Setting.getInstance().
				getProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE,"200"));
		
		maxsizeOfFavorite.setText(Setting.getInstance().
				getProperty(PropertyConstant.PROPERTY_FAVORITE_DISPLAY_MAXSIZE,"15"));
		resizeRowHeight.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_RESIZEROWHEIGHT,false));
		tableRowHeight.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_COMMONTABLE_ROWHEIGHT, "18"));
	}
	private void save()
	{
		MyLocale locale=(MyLocale)getPropertyMap().get(PROPERTY_LANGUAGE);
		if(locale!=null)
			PropertyManage.getLaunchSetting().setLanguage(locale.getLocale().getLanguage());
		
		String fontStr=(String)getPropertyMap().get(PropertyConstant.PROPERTY_SYSTEM_FONT);
		if(fontStr!=null)
		{
			Setting.getInstance().setSystemFont(transString2Font(fontStr));
		}
		
		assignValue(PropertyConstant.PROPERTY_COMMONTABLE_ISDISPLAYLINENUMBER);
		assignValue(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_HORIZONTALLINE);
		assignValue(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_VERTICALLINE);
		assignValue(PropertyConstant.PROPERTY_SYSTEM_HISTORYDAYS);
		assignValue(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE);
		assignValue(PropertyConstant.PROPERTY_FAVORITE_DISPLAY_MAXSIZE);
		assignValue(PropertyConstant.PROPERTY_COMMONTABLE_RESIZEROWHEIGHT);
		assignValue(PropertyConstant.PROPERTY_COMMONTABLE_ROWHEIGHT);
	}
	private String transFont2String(Font f)
	{
		if(f==null)
			return "";
		String str=f.getFamily()+","+f.getStyle()+","+f.getSize();
		return str;
	}
	private Font transString2Font(String str)
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
}
