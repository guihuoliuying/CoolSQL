/**
 * 
 */
package com.cattsoft.coolsql.gui.property.global;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.gui.property.FontSelectPanel;
import com.cattsoft.coolsql.gui.property.PropertyInterface;
import com.cattsoft.coolsql.pub.component.ColorIconButton;
import com.cattsoft.coolsql.pub.component.NumberEditor;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.display.DelimiterDefinition;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 *  Property setting of sqleditor view
 * @author ��Т��(kenny liu)
 *
 * 2008-1-17 create
 */
public class SqlEditorViewPropertyPane extends BasePropertySettingPane {

	private static final long serialVersionUID = 1L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(SqlEditorViewPropertyPane.class);
	private JCheckBox isSaveEditorContent;   //whether save the content of editor when exiting.
	
	private NumberEditor scriptThroldSize;
	private TextEditor sqlDelimiter;
	private ColorIconButton selectionButton; //the color of seleted text.
	
	private TextEditor editorFont; //font of sql editor
	
	/** highlight panel component */
	private ColorIconButton comment1Color;
	private ColorIconButton comment2Color;
	private ColorIconButton keyword1Color;
	private ColorIconButton keyword2Color;
	private ColorIconButton keyword3Color;
	private ColorIconButton literal1Color;
//	private ColorIconButton literal2Color;
	private ColorIconButton operatorColor;
	private ColorIconButton numberColor;
	
	private JCheckBox comment1Bold;
	private JCheckBox comment2Bold;
	private JCheckBox keyword1Bold;
	private JCheckBox keyword2Bold;
	private JCheckBox keyword3Bold;
	private JCheckBox literal1Bold;
//	private JCheckBox literal2Bold;
	private JCheckBox operatorBold;
	private JCheckBox numberBold;
	
	private JCheckBox comment1Italic;
	private JCheckBox comment2Italic;
	private JCheckBox keyword1Italic;
	private JCheckBox keyword2Italic;
	private JCheckBox keyword3Italic;
	private JCheckBox literal1Italic;
//	private JCheckBox literal2Italic;
	private JCheckBox operatorItalic;
	private JCheckBox numberItalic;
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
		double[][] model=new double[][]{{b,TableLayout.PREFERRED,TableLayout.PREFERRED,TableLayout.FILL,b},
				{b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.PREFERRED,b,
			TableLayout.FILL,b}};
		JPanel contentPane = new JPanel(new TableLayout(model));
		int rowIndex=1,columnIndex=1;
		int hCount=model[0].length;
		
		//isSaveEditorContent
		isSaveEditorContent=new JCheckBox(stringMgr.getString("global.setting.sqleditor.issaveeditorcontent.label"));
		isSaveEditorContent.setToolTipText(stringMgr.getString("global.setting.sqleditor.issaveeditorcontent.tt"));
		isSaveEditorContent.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_ISSAVEEDITORCONTENT, true));
		isSaveEditorContent.putClientProperty(PropertyInterface.PROPERTY_NAME,
				PropertyConstant.PROPERTY_VIEW_SQLEDITOR_ISSAVEEDITORCONTENT);
		contentPane.add(isSaveEditorContent,columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex);
		
		rowIndex+=2;
		columnIndex=1;
		
		//scriptThroldSize
		contentPane.add(new JLabel(stringMgr.getString("global.setting.sqleditor.scriptthreshold.label")),columnIndex+","+rowIndex);
		scriptThroldSize=new NumberEditor(5,0);
		scriptThroldSize.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SCRIPT_SQLTHRESHOLD, "2"));
		scriptThroldSize.putClientProperty(PropertyInterface.PROPERTY_NAME,
				PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SCRIPT_SQLTHRESHOLD);
		columnIndex++;
		contentPane.add(scriptThroldSize,columnIndex+","+rowIndex+",l,c"); //4
		
		rowIndex+=2;
		columnIndex=1;
		
		//sqlDelimiter
		contentPane.add(new JLabel(stringMgr.getString("global.setting.sqleditor.sqldelimiter.label")),columnIndex+","+rowIndex);
		sqlDelimiter=new TextEditor(20);
		sqlDelimiter.setText(Setting.getInstance().getProperty(
				PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER,
				DelimiterDefinition.STANDARD_DELIMITER.getDelimiter()));
		sqlDelimiter.putClientProperty(PropertyInterface.PROPERTY_NAME,
				PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER);
		columnIndex++;
		contentPane.add(sqlDelimiter,columnIndex+","+rowIndex+",l,c"); //4
		
		rowIndex+=2;
		columnIndex=1;
		
		//selectionButton
		JLabel selectionColorLabel=new JLabel(stringMgr.getString("global.setting.sqleditor.selectioncolor.label"));
		contentPane.add(selectionColorLabel,columnIndex+","+rowIndex);  //3
		selectionButton=new ColorIconButton(Setting.getInstance().getEditorSelectionColor());
		selectionButton.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SELECTIONCOLOR);
		columnIndex++;
		contentPane.add(selectionButton,columnIndex+","+rowIndex+",l,c"); //4
		
		rowIndex+=2;
		columnIndex=1;
		
		//editor font
		contentPane.add(new JLabel(stringMgr.getString("global.setting.sqleditor.font.label")),
				""+columnIndex+","+rowIndex+",l,c");
		editorFont = new TextEditor(25);
		Font f=Setting.getInstance().getEditorFont();
		editorFont.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_VIEW_SQLEDITOR_EDITOR_FONT);
		FontSelectPanel editorFontPanel=new FontSelectPanel(editorFont,f);
		columnIndex++;
		contentPane.add(editorFontPanel,columnIndex+","+rowIndex+",l,c");
//				+(columnIndex+1)+","+rowIndex);
		
		rowIndex+=2;
		columnIndex=1;
		
		//highlight panel
		JPanel highlightPane=createEditorHighlightPanel();
		contentPane.add(highlightPane,columnIndex+","+rowIndex+","+(columnIndex+1)+","+rowIndex);
		
		rowIndex+=2;
		columnIndex=1;
		
		//restoreButton
		columnIndex=+3;
		JButton restoreButton=createRestoreButton();
		contentPane.add(restoreButton,""+columnIndex+","+rowIndex+",r,b");
		
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(contentPane), BorderLayout.CENTER);
	}
	private JPanel createEditorHighlightPanel()
	{
		JPanel hPanel=new JPanel();
		hPanel.setLayout(new GridLayout(0,2));
		hPanel.setBorder(BorderFactory.createTitledBorder(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.bordertitle")));
		
		Setting sett = Setting.getInstance();
		//comment1
		JPanel p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		hPanel.add(new JLabel(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.comment1label"),JLabel.RIGHT));
		comment1Color=new ColorIconButton(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_COLOR, Color.GRAY));
		comment1Color.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_COLOR);
		p.add(comment1Color);
		comment1Italic=new JCheckBox("isItalic");
		comment1Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISITALIC, true));
		comment1Italic.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISITALIC);
		p.add(comment1Italic);
		comment1Bold=new JCheckBox("isBold");
		comment1Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISBOLD, false));
		comment1Bold.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISBOLD);
		p.add(comment1Bold);
		hPanel.add(p);
		//comment2
		p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		hPanel.add(new JLabel(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.comment2label"),JLabel.RIGHT));
		comment2Color=new ColorIconButton(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_COLOR, Color.GRAY));
		comment2Color.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_COLOR);
		p.add(comment2Color);
		comment2Italic=new JCheckBox("isItalic");
		comment2Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISITALIC, true));
		comment2Italic.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISITALIC);
		p.add(comment2Italic);
		comment2Bold=new JCheckBox("isBold");
		comment2Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISBOLD, false));
		comment2Bold.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISBOLD);
		p.add(comment2Bold);
		hPanel.add(p);
		
		//keyword1
		p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		hPanel.add(new JLabel(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.keyword1label"),JLabel.RIGHT));
		keyword1Color=new ColorIconButton(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_COLOR, Color.BLUE));
		keyword1Color.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_COLOR);
		p.add(keyword1Color);
		keyword1Italic=new JCheckBox("isItalic");
		keyword1Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISITALIC, false));
		keyword1Italic.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISITALIC);
		p.add(keyword1Italic);
		keyword1Bold=new JCheckBox("isBold");
		keyword1Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISBOLD, false));
		keyword1Bold.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISBOLD);
		p.add(keyword1Bold);
		hPanel.add(p);
		//keyword2
		p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		hPanel.add(new JLabel(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.keyword2label"),JLabel.RIGHT));
		keyword2Color=new ColorIconButton(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_COLOR, Color.MAGENTA));
		keyword2Color.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_COLOR);
		p.add(keyword2Color);
		keyword2Italic=new JCheckBox("isItalic");
		keyword2Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISITALIC, false));
		keyword2Italic.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISITALIC);
		p.add(keyword2Italic);
		keyword2Bold=new JCheckBox("isBold");
		keyword2Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISBOLD, false));
		keyword2Bold.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISBOLD);
		p.add(keyword2Bold);
		hPanel.add(p);
		//keyword3
		p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		hPanel.add(new JLabel(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.keyword3label"),JLabel.RIGHT));
		keyword3Color=new ColorIconButton(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_COLOR, new Color(0x009600)));
		keyword3Color.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_COLOR);
		p.add(keyword3Color);
		keyword3Italic=new JCheckBox("isItalic");
		keyword3Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISITALIC, false));
		keyword3Italic.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISITALIC);
		p.add(keyword3Italic);
		keyword3Bold=new JCheckBox("isBold");
		keyword3Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISBOLD, false));
		keyword3Bold.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISBOLD);
		p.add(keyword3Bold);
		hPanel.add(p);
		
		//literal1
		p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		hPanel.add(new JLabel(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.literal1label"),JLabel.RIGHT));
		literal1Color=new ColorIconButton(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_COLOR, new Color(0x650099)));
		literal1Color.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_COLOR);
		p.add(literal1Color);
		literal1Italic=new JCheckBox("isItalic");
		literal1Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISITALIC, false));
		literal1Italic.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISITALIC);
		p.add(literal1Italic);
		literal1Bold=new JCheckBox("isBold");
		literal1Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISBOLD, false));
		literal1Bold.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISBOLD);
		p.add(literal1Bold);
		hPanel.add(p);
		
		//operator
		p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		hPanel.add(new JLabel(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.operatorlabel"),JLabel.RIGHT));
		operatorColor=new ColorIconButton(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_COLOR,  new Color(228,27,47)));
		operatorColor.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_COLOR);
		p.add(operatorColor);
		operatorItalic=new JCheckBox("isItalic");
		operatorItalic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISITALIC, false));
		operatorItalic.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISITALIC);
		p.add(operatorItalic);
		operatorBold=new JCheckBox("isBold");
		operatorBold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISBOLD, false));
		operatorBold.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISBOLD);
		p.add(operatorBold);
		hPanel.add(p);
		
		//number
		p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		hPanel.add(new JLabel(stringMgr.
				getString("global.setting.sqleditor.highlightpanel.numberlabel"),JLabel.RIGHT));
		numberColor=new ColorIconButton(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_COLOR, Color.RED));
		numberColor.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_COLOR);
		p.add(numberColor);
		numberItalic=new JCheckBox("isItalic");
		numberItalic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISITALIC, false));
		numberItalic.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISITALIC);
		p.add(numberItalic);
		numberBold=new JCheckBox("isBold");
		numberBold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISBOLD, false));
		numberBold.putClientProperty(PropertyInterface.PROPERTY_NAME,PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISBOLD);
		p.add(numberBold);
		hPanel.add(p);
		
		return hPanel;
	}
	@Override
	protected void restoreToDefault()
	{
		Setting sett = Setting.getInstance();
		
		sett.setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_ISSAVEEDITORCONTENT,null);
		sett.setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SCRIPT_SQLTHRESHOLD,null);
		sett.setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER,null);
		sett.setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SELECTIONCOLOR,null);
		sett.setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_EDITOR_FONT,null);
		
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_COLOR,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_COLOR,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_COLOR,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_COLOR,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_COLOR,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_COLOR,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_COLOR,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_COLOR,null);
		
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISBOLD,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISBOLD,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISBOLD,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISBOLD,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISBOLD,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISBOLD,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISBOLD,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISBOLD,null);
		
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISITALIC,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISITALIC,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISITALIC,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISITALIC,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISITALIC,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISITALIC,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISITALIC,null);
		sett.setProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISITALIC,null);
		refreshDisplay();
	}
	private void refreshDisplay()
	{
		isSaveEditorContent.setSelected(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_ISSAVEEDITORCONTENT, true));
		scriptThroldSize.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SCRIPT_SQLTHRESHOLD, "2"));
		sqlDelimiter.setText(Setting.getInstance().getProperty(
				PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER,
				DelimiterDefinition.STANDARD_DELIMITER.getDelimiter()));
		
		selectionButton.setIconColor(Setting.getInstance().getEditorSelectionColor());
		
		Font f=Setting.getInstance().getEditorFont();
		editorFont.setText(Setting.font2String(f));
		editorFont.setFont(f);
		
		Setting sett = Setting.getInstance();
		
		comment1Color.setIconColor(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_COLOR, Color.GRAY));
		comment2Color.setIconColor(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_COLOR, Color.GRAY));
		keyword1Color.setIconColor(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_COLOR, Color.BLUE));
		keyword2Color.setIconColor(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_COLOR, Color.MAGENTA));
		keyword3Color.setIconColor(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_COLOR, new Color(0x009600)));
		literal1Color.setIconColor(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_COLOR, new Color(0x650099)));
		operatorColor.setIconColor(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_COLOR,new Color(228,27,47)));
		numberColor.setIconColor(sett.getColorProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_COLOR, Color.RED));
		
		comment1Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISITALIC, true));
		comment2Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISITALIC, true));
		keyword1Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISITALIC, false));
		keyword2Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISITALIC, false));
		keyword3Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISITALIC, false));
		literal1Italic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISITALIC, false));
		operatorItalic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISITALIC, false));
		numberItalic.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISITALIC, false));
		
		comment1Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISBOLD, false));
		comment2Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISBOLD, false));
		keyword1Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISBOLD, false));
		keyword2Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISBOLD, false));
		keyword3Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISBOLD, false));
		literal1Bold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISBOLD, false));
		operatorBold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISBOLD, false));
		numberBold.setSelected(sett.getBoolProperty(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISBOLD, false));
	}
	private void save()
	{
		assignValue(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_ISSAVEEDITORCONTENT);
		assignValue(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SCRIPT_SQLTHRESHOLD);
		assignValue(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_DELIMITER);
		assignValue(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SELECTIONCOLOR);
		assignValue(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_EDITOR_FONT);
		
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_COLOR);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_COLOR);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_COLOR);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_COLOR);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_COLOR);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_COLOR);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_COLOR);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_COLOR);
		
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISITALIC);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISITALIC);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISITALIC);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISITALIC);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISITALIC);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISITALIC);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISITALIC);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISITALIC);
		
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT1_ISBOLD);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_COMMENT2_ISBOLD);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD1_ISBOLD);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD2_ISBOLD);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_KEYWORD3_ISBOLD);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_LITERAL1_ISBOLD);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_OPERATOR_ISBOLD);
		assignValue(PropertyConstant.PROPERTY_SQLEDITOR_HIGHLIGHT_NUMBER_ISBOLD);
	}
}
