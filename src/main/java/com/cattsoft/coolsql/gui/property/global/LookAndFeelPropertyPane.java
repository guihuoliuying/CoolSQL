/**
 * Create date:2008-4-23
 */
package com.cattsoft.coolsql.gui.property.global;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.gui.property.PropertyInterface;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.lookandfeel.LookAndFeelMetaData;
import com.cattsoft.coolsql.system.lookandfeel.SystemLookAndFeel;
import com.jidesoft.swing.MultilineLabel;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-23 create
 */
public class LookAndFeelPropertyPane extends BasePropertySettingPane {
	
	public static final String DEFAULTLAF="defaultLAF";
	
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(LookAndFeelPropertyPane.class);
	private static final long serialVersionUID = 1L;

	private TextEditor nameText;
	private TextEditor classText;
	private TextEditor pathText;
	
	private JComboBox availableLAFs;
	
	private JLabel statusLabel;
	public LookAndFeelPropertyPane()
	{
		super();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#apply()
	 */
	public void apply() {
		save();
	}

	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
	 */
	public boolean isNeedApply() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#set()
	 */
	public boolean set() {
		save();
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
	 */
	public void setData(Object ob) {
		double b=10;
		JPanel contentPane = new JPanel(new TableLayout(new double[][]{{b,80,TableLayout.FILL,TableLayout.PREFERRED,b},
			{b,TableLayout.PREFERRED,
			b,TableLayout.PREFERRED,
			b,TableLayout.PREFERRED,
			b,TableLayout.PREFERRED,
			b,TableLayout.PREFERRED,
			b,TableLayout.FILL,
			b,TableLayout.FILL,
			b}}));
		
		SystemLookAndFeel systemLAF=SystemLookAndFeel.getInstance();
		availableLAFs=new JComboBox(systemLAF.getLAFNames().toArray());
		availableLAFs.putClientProperty(PropertyInterface.PROPERTY_NAME, DEFAULTLAF);
		availableLAFs.setEditable(false);
		availableLAFs.setSelectedItem(systemLAF.getChangedLAFName());
		availableLAFs.addItemListener(new ItemListener()
		{

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					updateDisplay();
				}
				
			}
			
		}
		);
		contentPane.add(availableLAFs,"1,1,2,1");
		
		nameText=new TextEditor(50);
		nameText.setEditable(false);
		classText=new TextEditor(50);
		classText.setEditable(false);
		pathText=new TextEditor(50);
		pathText.setEditable(false);
		contentPane.add(new JLabel("name:"),"1,3,c,b");
		contentPane.add(nameText,"2,3,3,3");
		contentPane.add(new JLabel("class:"),"1,5,c,b");
		contentPane.add(classText,"2,5,3,5");
		contentPane.add(new JLabel("library:"),"1,7,c,b");
		contentPane.add(pathText,"2,7,l,b");
		
		contentPane.add(new MultilineLabel(stringMgr.getString("propertysetting.systemlaf.prompt")),"1,9,3,9");
		
		statusLabel=new JLabel();
		contentPane.add(statusLabel,"2,11,3,11");
		
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(contentPane), BorderLayout.CENTER);
		
		updateDisplay();
		updateWarning();
	}
	private void updateDisplay()
	{
		nameText.setText(availableLAFs.getSelectedItem().toString());
		LookAndFeelMetaData data=SystemLookAndFeel.getInstance().getLAFMetaDataByName(availableLAFs.getSelectedItem().toString());
		classText.setText(data.getLafClass());
		pathText.setText(data.getResourcePath());
	}
	private void updateWarning()
	{
		SystemLookAndFeel systemLAF=SystemLookAndFeel.getInstance();
		if(systemLAF.isLookAndFeelChanged())
		{
			statusLabel.setText(stringMgr.getString("propertysetting.systemlaf.isvalid"));
		}else
		{
			statusLabel.setText("");
		}
	}
	private void save()
	{
		if(isChanged())
		{
			String selectedLAF=(String)getPropertyMap().get(DEFAULTLAF);
			if(selectedLAF!=null)
			{
				SystemLookAndFeel.getInstance().changeLookAndFeel(selectedLAF);
			}
		}
		updateWarning();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.global.BasePropertySettingPane#restoreToDefault()
	 */
	@Override
	protected void restoreToDefault() {
		// TODO Auto-generated method stub
		
	}
}
