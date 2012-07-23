/**
 * 
 */
package com.cattsoft.coolsql.gui.property.global;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.exportdata.excel.ExcelComponentSet;
import com.cattsoft.coolsql.gui.property.PropertyInterface;
import com.cattsoft.coolsql.pub.component.ColorIconButton;
import com.cattsoft.coolsql.pub.component.NumberEditor;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * Property setting for resultset view
 * @author ��Т��(kenny liu)
 *
 * 2008-1-17 create
 */
public class ResultsetViewPropertyPane extends BasePropertySettingPane {

	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(ResultsetViewPropertyPane.class);
	
	private static final String RADIO_TABDELIMITER="radio.tabdelimiter";
	private static final String RADIO_ELSEDELIMITER="radio.elsedelimiter";
	
	private JCheckBox isDisplayInfoBar=null; // check if display the bar used to display meta information result
	
	private ColorIconButton modifedCellHighLightColor;
	
	private TextEditor pageSize;// max length of size of result that displayed on the result panel
	
	private NumberEditor maxColumnWidth;
	
	//Set page size of html export
	private NumberEditor pageSizeOfHtmlExport;
	
	private JCheckBox isDisplayHeadOnTextExport;
	//Set delimiter used to delimit data in exporting table cell data to local file.
	private JRadioButton tabDelimiter;
	private JRadioButton elseDelimiter;
	private TextEditor elseDelimiterText;
	
	//excel export setting
	private JCheckBox isDisplayHeadOnExcelExport;
	private NumberEditor excelMaxWriteRows;
	private ColorIconButton excelHeadColor;
	
	private JCheckBox isDirectModify;
	private JCheckBox isSortable;
	
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
		double p = TableLayout.PREFERRED;
		double vg = 10;

		double[][] size=new double[][]{{b,p,p,TableLayout.FILL,b},
				{b,
				p,vg,
				p,vg,
				p,vg,
				p,vg,
				p,vg,
				p,vg,
				p,vg,
				p,vg,
				p,vg,
				p,vg,
				TableLayout.FILL,b}};
				//1,3,4
		JPanel contentPane = new JPanel(new TableLayout(size));
		int hCount=size[0].length;
		int rowIndex=1,columnIndex=1;
		
		//Set whether display meta data information.
		isDisplayInfoBar=new JCheckBox(stringMgr.getString("global.setting.resultset.isdisplaymetainfo.label"));
		isDisplayInfoBar.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_VIEW_RESULTSET_DISPLAYMETAINFO);
		isDisplayInfoBar.setSelected(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DISPLAYMETAINFO,true));
		contentPane.add(isDisplayInfoBar,columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex);  //1
		
		rowIndex+=2;
		columnIndex=1;
		
		//modifedCellHighLightColor
		contentPane.add(new JLabel(stringMgr.getString("global.setting.resultset.modifiedcellhighlight.color")),columnIndex+","+rowIndex); 
		modifedCellHighLightColor=new ColorIconButton();
		modifedCellHighLightColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT,Color.BLUE));
		modifedCellHighLightColor.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT);
		columnIndex++;
		contentPane.add(modifedCellHighLightColor,columnIndex+","+rowIndex+",l,c");
		
		rowIndex+=2;
		columnIndex=1;
		
		//maxColumnWidth
		contentPane.add(new JLabel(stringMgr.getString("global.setting.resultset.maxcolumnwidth.label")),columnIndex+","+rowIndex); 
		maxColumnWidth=new NumberEditor(6,0);
		maxColumnWidth.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH);
		maxColumnWidth.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH, "2048"));
		columnIndex++;
		contentPane.add(maxColumnWidth,columnIndex+","+rowIndex+",l,c"); //4
		
		rowIndex+=2;
		columnIndex=1;
		
		//label and pagesize editor
		JLabel pageSizeLabel=new JLabel(stringMgr.getString("global.setting.resultset.displaysizeofquery.label"));
		contentPane.add(pageSizeLabel,columnIndex+","+rowIndex);  //3
		pageSize=new NumberEditor(7,0);
		pageSize.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_VIEW_RESULTSET_NUMBEROFROWSPERPAGE);
		pageSize.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_NUMBEROFROWSPERPAGE, "200"));
		columnIndex++;
		contentPane.add(pageSize,columnIndex+","+rowIndex+",l,c"); //4
		
		rowIndex+=2;
		columnIndex=1;
		
		//pageSizeOfHtmlExport
		contentPane.add(new JLabel(stringMgr.getString("global.setting.system.pagesizeofhtmlexport.label")),columnIndex+","+rowIndex);
		pageSizeOfHtmlExport=new NumberEditor(10);
		putClientProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_HTML_PAGESIZE, pageSizeOfHtmlExport);
		columnIndex++;
		pageSizeOfHtmlExport.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_HTML_PAGESIZE,"50"));
		contentPane.add(pageSizeOfHtmlExport,columnIndex+","+rowIndex+",l,c,");  //1
		
		rowIndex+=2;
		columnIndex=1;
		
		//isDisplayHeadOnTextExport
		isDisplayHeadOnTextExport=new JCheckBox(stringMgr.getString("global.setting.system.isDisplayHeadOnTextExport.label"));
		putClientProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_ISDISPLAYHEAD, isDisplayHeadOnTextExport);
		
		isDisplayHeadOnTextExport.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_ISDISPLAYHEAD,true));
		contentPane.add(isDisplayHeadOnTextExport,columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex);  //1
		
		rowIndex+=2;
		columnIndex=1;
		
		//export_DataDelimiter
		contentPane.add(createExportTextDelimiterPanel(),columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex); 
		
		rowIndex+=2;
		columnIndex=1;
		
		//excel export setting
		contentPane.add(createExcelExportPanel(),columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex); 
		
		rowIndex+=2;
		columnIndex=1;
		
		isDirectModify = new JCheckBox(stringMgr.getString("global.setting.resultset.datasettable.isdirectmodify.label"));
		putClientProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISDIRECTMODIFY, isDirectModify);
		isDirectModify.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISDIRECTMODIFY, true));
		contentPane.add(isDirectModify,columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex);  //1
		
		rowIndex+=2;
		columnIndex=1;
		
		isSortable = new JCheckBox(stringMgr.getString("global.setting.resultset.datasettable.issortable.label"));
		putClientProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISSORTABLE, isSortable);
		isSortable.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISSORTABLE, true));
		contentPane.add(isSortable,columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex);  //1
		
		rowIndex+=2;
		columnIndex=1;
		
		//restore button
		columnIndex=2;
		JButton restoreButton=createRestoreButton();
		contentPane.add(restoreButton,""+(hCount-2)+","+rowIndex+",r,b");
		
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(contentPane), BorderLayout.CENTER);
	}
	@Override
	protected void restoreToDefault()
	{
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DISPLAYMETAINFO, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_NUMBEROFROWSPERPAGE, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER, null);
		
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_ISDISPLAYHEAD, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_HTML_PAGESIZE, null);
		
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_ISDISPLAYHEAD, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_MAXROWSWRITE, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_HEADCOLOR, null);
		
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISDIRECTMODIFY, null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISSORTABLE, null);
		
		refreshDisplay();
	}
	private void refreshDisplay()
	{
		isDisplayInfoBar.setSelected(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DISPLAYMETAINFO, true));
		modifedCellHighLightColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT,Color.BLUE));
		pageSize.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_NUMBEROFROWSPERPAGE, "200"));
		maxColumnWidth.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH, "2048"));
		
		pageSizeOfHtmlExport.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_HTML_PAGESIZE,"50"));
		
		isDisplayHeadOnTextExport.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_ISDISPLAYHEAD,true));
		String delimiter=Setting.getInstance().getProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER,"\t");
		if(delimiter.equals("\t"))
		{
			elseDelimiterText.setText("");
			elseDelimiterText.setEnabled(false);
			tabDelimiter.setSelected(true);
		}else
		{
			elseDelimiterText.setText(delimiter);
			elseDelimiter.setSelected(true);
		}
		
		isDisplayHeadOnExcelExport.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_ISDISPLAYHEAD,true));
		excelMaxWriteRows.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_MAXROWSWRITE,
        		""+ExcelComponentSet.DEFAULT_WRITE));
		excelHeadColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_HEADCOLOR, Color.RED));
		
		isDirectModify.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISDIRECTMODIFY, true));
		isSortable.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISSORTABLE, true));
	}
	private void save()
	{
		assignValue(PropertyConstant.PROPERTY_VIEW_RESULTSET_DISPLAYMETAINFO);
		assignValue(PropertyConstant.PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT);
		assignValue(PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH);
		assignValue(PropertyConstant.PROPERTY_VIEW_RESULTSET_NUMBEROFROWSPERPAGE);
		assignValue(PropertyConstant.PROPERTY_SYSTEM_EXPORT_HTML_PAGESIZE);
		
		assignValue(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_ISDISPLAYHEAD);
		if(tabDelimiter.isSelected())
		{
			Setting.getInstance().setProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER,"\t");
		}else
		{
			assignValue(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER);
		}
		assignValue(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_ISDISPLAYHEAD);
		assignValue(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_MAXROWSWRITE);
		assignValue(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_HEADCOLOR);
		
		assignValue(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISDIRECTMODIFY);
		assignValue(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISSORTABLE);
	}
	private JPanel createExportTextDelimiterPanel()
	{
		JPanel exportDelimterPanel=new JPanel(new GridLayout(2,1));
		exportDelimterPanel.setBorder(BorderFactory.createTitledBorder(stringMgr.getString("global.setting.system.exportdelimiter.label")));
		
		ButtonGroup bg=new ButtonGroup();
		ActionListener exportRadioAction=new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				elseDelimiterText.setEnabled(!tabDelimiter.isSelected());
			}
			
		};
		JPanel tabPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabDelimiter=new JRadioButton(stringMgr.getString("global.setting.system.exportdelimiter.tab.label"));
		tabDelimiter.putClientProperty(PropertyInterface.PROPERTY_NAME,RADIO_TABDELIMITER);
		tabPanel.add(tabDelimiter);
		exportDelimterPanel.add(tabPanel);
		
		JPanel tmpPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		elseDelimiter=new JRadioButton(stringMgr.getString("global.setting.system.exportdelimiter.else.label")); 
		elseDelimiter.putClientProperty(PropertyInterface.PROPERTY_NAME,RADIO_ELSEDELIMITER);
		tmpPanel.add(elseDelimiter);
		elseDelimiterText=new TextEditor(10);
		putClientProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER, elseDelimiterText);
		tmpPanel.add(elseDelimiterText);
		String delimiter=Setting.getInstance().getProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_TEXT_DELIMITER,"\t");
		if(delimiter.equals("\t"))
		{
			elseDelimiterText.setText("");
			elseDelimiterText.setEnabled(false);
			tabDelimiter.setSelected(true);
		}else
		{
			elseDelimiterText.setText(delimiter);
			elseDelimiter.setSelected(true);
		}
		
		elseDelimiter.addActionListener(exportRadioAction);
		tabDelimiter.addActionListener(exportRadioAction);
		bg.add(tabDelimiter);
		bg.add(elseDelimiter);
		
		exportDelimterPanel.add(tmpPanel);
		
		return exportDelimterPanel;
	}
	private JPanel createExcelExportPanel()
	{
		JPanel exportDelimterPanel=new JPanel(new GridLayout(3,1));
		exportDelimterPanel.setBorder(BorderFactory.createTitledBorder(stringMgr.getString("global.setting.system.excel.paneltitle")));
		
		//isDisplayHeadOnExcelExport
		isDisplayHeadOnExcelExport=new JCheckBox(stringMgr.getString("global.setting.system.isDisplayHeadOnExcelExport.label"));
		putClientProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_ISDISPLAYHEAD, isDisplayHeadOnExcelExport);
		isDisplayHeadOnExcelExport.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_ISDISPLAYHEAD,true));
		exportDelimterPanel.add(isDisplayHeadOnExcelExport);
		
		//excelMaxWriteRows
		JPanel tmpPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		tmpPanel.add(new JLabel(stringMgr.getString("global.setting.system.excel.maxrowswrite.label")));
		excelMaxWriteRows=new NumberEditor(5,0);
		putClientProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_MAXROWSWRITE, excelMaxWriteRows);
		excelMaxWriteRows.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_MAXROWSWRITE,
        		""+ExcelComponentSet.DEFAULT_WRITE));
		tmpPanel.add(excelMaxWriteRows);
		tmpPanel.add(new JLabel(stringMgr.getString("global.setting.system.excel.maxrowswrite.tt")));
		exportDelimterPanel.add(tmpPanel);
		
		//excelHeadColor
		tmpPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		tmpPanel.add(new JLabel(stringMgr.getString("global.setting.system.excel.headcolor.label")));
		excelHeadColor=new ColorIconButton();
		putClientProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_HEADCOLOR, excelHeadColor);
		excelHeadColor.setIconColor(Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_SYSTEM_EXPORT_EXCEL_HEADCOLOR, Color.RED));
		tmpPanel.add(excelHeadColor);
		exportDelimterPanel.add(tmpPanel);
		
		return exportDelimterPanel;
	}
}
