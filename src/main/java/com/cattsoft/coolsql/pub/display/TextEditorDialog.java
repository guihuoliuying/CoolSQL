/**
 * Create date:2008-5-18
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-18 create
 */
public class TextEditorDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private Inputer inputer;
	
	private WrapTextPane editor;
	
	private JButton okButton;
	public TextEditorDialog(JDialog owner,Inputer inputer)
	{
		this(owner,inputer,"",true);
	}
	public TextEditorDialog(JFrame owner,Inputer inputer)
	{
		this(owner,inputer,"",true);
	}
	public TextEditorDialog(JDialog owner,Inputer inputer,String text)
	{
		this(owner,inputer,text,true);
	}
	public TextEditorDialog(JFrame owner,Inputer inputer,String text)
	{
		this(owner,inputer,text,true);
	}
	public TextEditorDialog(JDialog owner,Inputer inputer,String text,boolean isReadOnly)
	{
		super(owner,false);
		setTitle("Text editor");
		this.inputer=inputer;
		
		initComponents(isReadOnly);
		
		editor.setText(text);
		editor.setEditable(!isReadOnly);
	}
	public TextEditorDialog(JFrame owner,Inputer inputer,String text,boolean isReadOnly)
	{
		super(owner,false);
		setTitle("Text editor");
		this.inputer=inputer;
		
		initComponents(isReadOnly);
		editor.setText(text);
		editor.setEditable(!isReadOnly);
	}
	protected void initComponents(boolean isReadOnly)
	{
		editor=new WrapTextPane();
		Font defaultfont=new Font("Dialog",0,12);
		editor.setFont(Setting.getInstance().getFontProperty(PropertyConstant.PROPERTY_TEXTEDITOR_FONT, defaultfont));
		JPanel main=(JPanel)getContentPane();
		main.setLayout(new BorderLayout());
		main.add(new JScrollPane(editor),BorderLayout.CENTER);
		
		GUIUtil.installDefaultTextPopMenu(editor);
		
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		okButton=new RenderButton(PublicResource.getOkButtonLabel());
		okButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				if(inputer!=null)
					inputer.setData(editor.getText());
				
				close();
			}
			
		}
		);
		okButton.setEnabled(!isReadOnly);
		buttonPanel.add(okButton);
		JButton quitButton=new RenderButton(PublicResource.getCancelButtonLabel());
		quitButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				close();
			}
			
		});
		buttonPanel.add(quitButton);
		main.add(buttonPanel,BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		}
		);
		getRootPane().setDefaultButton(okButton);
		setSize(300,250);
		toCenter();
	}
	public void setReadOnly()
	{
		editor.setEditable(false);
		okButton.setEnabled(false);
	}
	public void enableEditable()
	{
		editor.setEditable(true);
		okButton.setEnabled(true);
	}
	public void setEditable(boolean isEditable)
	{
		if(isEditable)
		{
			enableEditable();
		}else
		{
			setReadOnly();
		}
	}
	public boolean isReadOnly()
	{
		return editor.isEditable();
	}
	public void close()
	{
		removeAll();
		dispose();
	}
}
