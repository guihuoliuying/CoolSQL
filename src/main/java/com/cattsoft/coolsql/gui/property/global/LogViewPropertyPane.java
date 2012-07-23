/**
 * 
 */
package com.cattsoft.coolsql.gui.property.global;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.gui.property.PropertyInterface;
import com.cattsoft.coolsql.pub.component.ColorIconButton;
import com.cattsoft.coolsql.pub.component.NumberEditor;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.SystemConstant;
import com.cattsoft.coolsql.view.log.LogDocument;
import com.cattsoft.coolsql.view.log.LogProxy;


/**
 * propert setting of log view
 * @author ��Т��(kenny liu)
 *
 * 2008-1-17 create
 */
public class LogViewPropertyPane extends BasePropertySettingPane {

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(LogViewPropertyPane.class);
	private static final long serialVersionUID = 1L;

	private NumberEditor maxLogSize;
	private JCheckBox isSaveLogToFile;
	private TextEditor logFilePath;
	
	private ColorIconButton infoColor;
	private ColorIconButton errorColor;
	private ColorIconButton warnColor;
	
	private JComboBox logLevel;
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#apply()
	 */
	public void apply() {
		if(!check())
			return;
		save();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#set()
	 */
	public boolean set() {
		try
		{
			if(!check())
				return false;
			save();
			return true;
		}catch(Exception e)
		{
			LogProxy.errorReport(e);
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
//			TableLayout.PREFERRED,b,
//			TableLayout.PREFERRED,b,
			TableLayout.FILL,b}};
		JPanel contentPane = new JPanel(new TableLayout(model));
		int rowIndex=1,columnIndex=1;
		
		//max log size
		contentPane.add(new JLabel(stringMgr.getString("global.setting.log.maxlogsize.label")),columnIndex+","+rowIndex);
		maxLogSize=new NumberEditor(10,0);
		maxLogSize.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_LOG_MAXLOGTEXT, ""+LogDocument.DEFAULT_MAXLENGTH));
		maxLogSize.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_LOG_MAXLOGTEXT);
		maxLogSize.setToolTipText(stringMgr.getString("global.setting.log.maxlogsize.tt"));
		columnIndex++;
		contentPane.add(maxLogSize,columnIndex+","+rowIndex+",l,c");
		
		rowIndex+=2;
		columnIndex=1;
		
		//whether save the log into local file
		isSaveLogToFile=new JCheckBox(stringMgr.getString("global.setting.log.issavelogtofile.label"));
		isSaveLogToFile.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_LOG_ISSAVETOFILE);
		isSaveLogToFile.setSelected(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_VIEW_LOG_ISSAVETOFILE,false));
		contentPane.add(isSaveLogToFile,columnIndex+","+rowIndex+",l,c");
		
		rowIndex+=2;
		columnIndex=1;
		
		//file path
		columnIndex=2;
		
		JPanel logFilePanel=new JPanel();
		logFilePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		logFilePanel.add(new JLabel(stringMgr.getString("global.setting.log.logfilepath.label")));
		
		logFilePath=new TextEditor(45);
		logFilePath.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_LOG_FILEPATH);
		logFilePath.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_LOG_FILEPATH, SystemConstant.LOGVIEW_LOGFILE));
		logFilePanel.add(logFilePath);
		final JButton selectBtn=new JButton("...");
		selectBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				
				File initFile=new File(logFilePath.getText());
				JFileChooser fc = new JFileChooser(initFile.getParent());
				fc.setMultiSelectionEnabled(false); // ֻ����ѡ��һ���ļ�
				fc.setSelectedFile(initFile);
				int result=fc.showSaveDialog(GUIUtil.findLikelyOwnerWindow());
				if(result!= JFileChooser.APPROVE_OPTION)
					return;
				logFilePath.setText(fc.getSelectedFile().getAbsolutePath());
			}
			
		}
		);
		logFilePanel.add(selectBtn);
		if(!isSaveLogToFile.isSelected())
		{
			logFilePath.setEnabled(false);
			selectBtn.setEnabled(false);
		}
		isSaveLogToFile.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				logFilePath.setEnabled(isSaveLogToFile.isSelected());
				selectBtn.setEnabled(isSaveLogToFile.isSelected());
			}
			
		}
		);
		contentPane.add(logFilePanel,columnIndex+","+rowIndex+",3,"+rowIndex);
		
		rowIndex+=2;
		columnIndex=1;
		
		//info color
		contentPane.add(new JLabel(stringMgr.getString("global.setting.log.color.info.label")),columnIndex+","+rowIndex);
		infoColor=new ColorIconButton();
		infoColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR,LogProxy.DEFAULT_INFO_COLOR));
		infoColor.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR);
		columnIndex++;
		contentPane.add(infoColor,columnIndex+","+rowIndex+",l,c");
		
		rowIndex+=2;
		columnIndex=1;
		
		//warn color
		contentPane.add(new JLabel(stringMgr.getString("global.setting.log.color.warn.label")),columnIndex+","+rowIndex);
		warnColor=new ColorIconButton();
		warnColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR,LogProxy.DEFAULT_WARN_COLOR));
		warnColor.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR);
		columnIndex++;
		contentPane.add(warnColor,columnIndex+","+rowIndex+",l,c");
		
		rowIndex+=2;
		columnIndex=1;
		
		//error color
		contentPane.add(new JLabel(stringMgr.getString("global.setting.log.color.error.label")),columnIndex+","+rowIndex);
		errorColor=new ColorIconButton();
		errorColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR,LogProxy.DEFAULT_ERROR_COLOR));
		errorColor.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR);
		columnIndex++;
		contentPane.add(errorColor,columnIndex+","+rowIndex+",l,c");
		
		rowIndex+=2;
		columnIndex=1;
		
		//loglevel
		contentPane.add(new JLabel(stringMgr.getString("global.setting.log.level.label")),columnIndex+","+rowIndex);
		LogLevelItem[] leveData=new LogLevelItem[4];
		leveData[0]=new LogLevelItem("DEBUG",LogProxy.DEBUG);
		leveData[1]=new LogLevelItem("INFO",LogProxy.INFO);
		leveData[2]=new LogLevelItem("WARN",LogProxy.WARN);
		leveData[3]=new LogLevelItem("ERROR",LogProxy.ERROR);
		logLevel=new JComboBox(leveData);
		logLevel.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_LOG_LEVEL);
		logLevel.setSelectedIndex(Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_LOG_LEVEL, LogProxy.INFO));
		columnIndex++;
		contentPane.add(logLevel,columnIndex+","+rowIndex+",l,c");
		
		rowIndex+=2;
		columnIndex=1;
		
		//restore button.
		columnIndex=3;
		JButton restoreButton=createRestoreButton();
		contentPane.add(restoreButton,columnIndex+","+rowIndex+",r,b");
		
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(contentPane), BorderLayout.CENTER);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.global.BasePropertySettingPane#restoreToDefault()
	 */
	@Override
	protected void restoreToDefault() {
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_LOG_MAXLOGTEXT,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_LOG_ISSAVETOFILE,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_LOG_FILEPATH,null);
		
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR,null);
		
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_LOG_LEVEL,null);
		
		refreshDisplay();
	}
	private void refreshDisplay()
	{
		maxLogSize.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_LOG_MAXLOGTEXT,  ""+LogDocument.DEFAULT_MAXLENGTH));
		isSaveLogToFile.setSelected(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_VIEW_LOG_ISSAVETOFILE,true));
		logFilePath.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_LOG_FILEPATH, SystemConstant.userPath));
		
		infoColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR,LogProxy.DEFAULT_INFO_COLOR));
		warnColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR,LogProxy.DEFAULT_WARN_COLOR));
		errorColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR,LogProxy.DEFAULT_ERROR_COLOR));
		
		logLevel.setSelectedIndex(Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_LOG_LEVEL, LogProxy.INFO));
	}
	private void save()
	{
		assignValue(PropertyConstant.PROPERTY_VIEW_LOG_MAXLOGTEXT);
		assignValue(PropertyConstant.PROPERTY_VIEW_LOG_ISSAVETOFILE);
		if(isSaveLogToFile.isSelected())
		{
			assignValue(PropertyConstant.PROPERTY_VIEW_LOG_FILEPATH);
		}
		
		assignValue(PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR);
		assignValue(PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR);
		assignValue(PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR);
		
		Setting.getInstance().setIntProperty(PropertyConstant.PROPERTY_VIEW_LOG_LEVEL, logLevel.getSelectedIndex());
	}
	private boolean check()
	{
		if(isSaveLogToFile.isSelected())
		{
			String filePath=logFilePath.getText();
			if(filePath.trim().equals(""))
			{
				JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(),
						stringMgr.getString("global.setting.log.logfilepath.error.empty"),"warning",JOptionPane.WARNING_MESSAGE);
				return false;
			}
			File file=new File(filePath);
			if(file.isDirectory())
			{
				JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(), 
						stringMgr.getString("global.setting.log.logfilepath.error.directory"),"warning",JOptionPane.WARNING_MESSAGE);
				return false;
			}
			return true;
		}else
			return true;
	}
	private class LogLevelItem 
	{
		String name;
		int value;
		public LogLevelItem(String name,int value)
		{
			this.name=name;
			this.value=value;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the value
		 */
		public int getValue() {
			return this.value;
		}
		/**
		 * @param value the value to set
		 */
		public void setValue(int value) {
			this.value = value;
		}
		@Override
		public String toString()
		{
			return name;
		}
	}
}
