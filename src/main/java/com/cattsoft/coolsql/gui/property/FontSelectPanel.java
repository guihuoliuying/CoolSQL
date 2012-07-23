/**
 * Create date:2008-5-5
 */
package com.cattsoft.coolsql.gui.property;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.system.Setting;
import com.l2fprod.common.swing.JFontChooser;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-5 create
 */
public class FontSelectPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private TextEditor fontDisplay;
	public FontSelectPanel(TextEditor fontDisplay,Font initValue)
	{
		if(fontDisplay==null)
		{
			this.fontDisplay=new TextEditor(23);
		}else
			this.fontDisplay=fontDisplay;
		this.fontDisplay.setEditable(false);
		this.fontDisplay.setMaximumSize(new java.awt.Dimension(45, 22));
		this.fontDisplay.setMinimumSize(new java.awt.Dimension(45, 22));
		this.fontDisplay.setPreferredSize(new java.awt.Dimension(45, 22));
		
		if(initValue!=null)
		{
			this.fontDisplay.setFont(initValue);
			this.fontDisplay.setText(Setting.font2String(initValue));
		}
		
		initComponents();
	}
	protected void initComponents()
	{
		setLayout(new FlowLayout());
		add(fontDisplay);
		
		JButton changeButton=new JButton("...");
		changeButton.setMaximumSize(new java.awt.Dimension(22, 22));
		changeButton.setMinimumSize(new java.awt.Dimension(22, 22));
		changeButton.setPreferredSize(new java.awt.Dimension(22, 22));
		changeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				Font selectedFont=JFontChooser.showDialog(GUIUtil.findLikelyOwnerWindow(), 
						"Font selector", fontDisplay.getFont());
				if(selectedFont!=null)
				{
					fontDisplay.setText(Setting.font2String(selectedFont));
					fontDisplay.setFont(selectedFont);
				}
			}
			
		});
		add(changeButton);
	}
	public void changeFont(Font f)
	{
		if(f==null)
			return;
		fontDisplay.setText(Setting.font2String(f));
		fontDisplay.setFont(f);
	}
	public Font getSelectedFont()
	{
		return fontDisplay.getFont();
	}
}
