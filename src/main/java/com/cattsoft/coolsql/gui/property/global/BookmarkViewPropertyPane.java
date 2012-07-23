/**
 * 
 */
package com.cattsoft.coolsql.gui.property.global;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.gui.property.PropertyInterface;
import com.cattsoft.coolsql.pub.component.NumberEditor;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.log.LogProxy;


/**
 * property setting panel of bookmark view 
 * @author ��Т��(kenny liu)
 *
 * 2008-1-17 create
 */
public class BookmarkViewPropertyPane extends BasePropertySettingPane {

	private static final long serialVersionUID = 1L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(BookmarkViewPropertyPane.class);
	
	private static final String PROPERTY_COMMIT="commit";
	private static final String PROPERTY_ROLLBACK="rollback";
	
	private NumberEditor connectTimeOut;
	
	private JRadioButton switchCommitButton;
	private JRadioButton switchRollbackButton;
	
	private JRadioButton disconnectCommitButton;
	private JRadioButton disconnectRollbackButton; 
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
			TableLayout.FILL,b}};
		JPanel contentPane = new JPanel(new TableLayout(model));
		int rowIndex=1,columnIndex=1;
		int hCount=model[0].length;
		
		//Timeout of connecting 
		contentPane.add(new JLabel(stringMgr.getString("bookmark.setting.connectTimeout.label")),columnIndex+","+rowIndex);
		connectTimeOut=new NumberEditor(10,0);
		connectTimeOut.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_CONNECT_TIMEOUT, "60"));
		connectTimeOut.putClientProperty(PropertyInterface.PROPERTY_NAME, PropertyConstant.PROPERTY_VIEW_BOOKMARK_CONNECT_TIMEOUT);
		columnIndex++;
		contentPane.add(connectTimeOut,columnIndex+","+rowIndex+",l,c");
		columnIndex++;
		contentPane.add(new JLabel(stringMgr.getString("bookmark.setting.connectTimeout.tip")),columnIndex+","+rowIndex);
		
		rowIndex+=2;
		columnIndex=1;
		
		//before enable autocommit
		JPanel beforeAutocommit=createBeforeEnableAutoCommitPanel();
		contentPane.add(beforeAutocommit,columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex); 
		
		rowIndex+=2;
		columnIndex=1;
		
		//bookmark.setting.dothingbeforedisconnect
		JPanel beforeDisconnect=createBeforeDisconnectPanel();
		contentPane.add(beforeDisconnect,columnIndex+","+rowIndex+","+(hCount-2)+","+rowIndex); 
		
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
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_CONNECT_TIMEOUT,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_BEFORE_ENABLEAUTOCOMMIT,null);
		Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_BEFORE_DISCONNECT,null);
		refreshDisplay();
	}
	private void refreshDisplay()
	{
		connectTimeOut.setText(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_CONNECT_TIMEOUT, "60"));
		
		if(Setting.getInstance().getProperty(
				PropertyConstant.PROPERTY_VIEW_SQLEDITOR_BEFORE_ENABLEAUTOCOMMIT ,"commit")
				.equals("commit"))
		{
			switchCommitButton.setSelected(true);
		}else
		{
			switchRollbackButton.setSelected(true);
		}
		if(Setting.getInstance().getProperty(
				PropertyConstant.PROPERTY_VIEW_BOOKMARK_BEFORE_DISCONNECT ,"commit")
				.equals("commit"))
		{
			disconnectCommitButton.setSelected(true);
		}else
		{
			disconnectRollbackButton.setSelected(true);
		}
		
	}
	private void save()
	{
		assignValue(PropertyConstant.PROPERTY_VIEW_BOOKMARK_CONNECT_TIMEOUT);
		
		if(switchCommitButton.isSelected())
		{
			Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_BEFORE_ENABLEAUTOCOMMIT,"commit");
		}else
		{
			Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_BEFORE_ENABLEAUTOCOMMIT,"rollback");
		}
		if(disconnectCommitButton.isSelected())
		{
			Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_BEFORE_DISCONNECT,"commit");
		}else
		{
			Setting.getInstance().setProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_BEFORE_DISCONNECT,"rollback");
		}
	}
	private JPanel createBeforeEnableAutoCommitPanel()
	{
		JPanel p=new JPanel();
		p.setBorder(BorderFactory.createTitledBorder(stringMgr.getString("bookmark.setting.dothingbeforeautocommit")));
		
		p.setLayout(new GridLayout(2,1));
		switchCommitButton=new JRadioButton("commit");
		putClientProperty(PROPERTY_COMMIT, switchCommitButton);
		switchRollbackButton=new JRadioButton("rollback");
		putClientProperty(PROPERTY_ROLLBACK, switchRollbackButton);
		
		p.add(switchCommitButton);
		p.add(switchRollbackButton);
		
		ButtonGroup bGroup=new ButtonGroup();
		bGroup.add(switchCommitButton);
		bGroup.add(switchRollbackButton);
		
		if(Setting.getInstance().getProperty(
				PropertyConstant.PROPERTY_VIEW_SQLEDITOR_BEFORE_ENABLEAUTOCOMMIT ,"commit")
				.equals("commit"))
		{
			switchCommitButton.setSelected(true);
		}else
		{
			switchRollbackButton.setSelected(true);
		}
		
		return p;
	}
	private JPanel createBeforeDisconnectPanel()
	{
		JPanel p=new JPanel();
		p.setBorder(BorderFactory.createTitledBorder(stringMgr.getString("bookmark.setting.dothingbeforedisconnect")));
		
		p.setLayout(new GridLayout(2,1));
		disconnectCommitButton=new JRadioButton("commit");
		putClientProperty(PROPERTY_COMMIT, disconnectCommitButton);
		disconnectRollbackButton=new JRadioButton("rollback");
		putClientProperty(PROPERTY_ROLLBACK, disconnectRollbackButton);
		
		p.add(disconnectCommitButton);
		p.add(disconnectRollbackButton);
		
		ButtonGroup bGroup=new ButtonGroup();
		bGroup.add(disconnectCommitButton);
		bGroup.add(disconnectRollbackButton);
		
		if(Setting.getInstance().getProperty(
				PropertyConstant.PROPERTY_VIEW_BOOKMARK_BEFORE_DISCONNECT ,"commit")
				.equals("commit"))
		{
			disconnectCommitButton.setSelected(true);
		}else
		{
			disconnectRollbackButton.setSelected(true);
		}
		
		return p;
	}
}
