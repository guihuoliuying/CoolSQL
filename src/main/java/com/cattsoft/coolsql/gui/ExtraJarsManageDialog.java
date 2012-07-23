/**
 * 
 */
package com.cattsoft.coolsql.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author xiaolin
 *
 * 2008-6-26 create
 */
public class ExtraJarsManageDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;
	
	private ExtraJarsManagePanel managePanel;
	public ExtraJarsManageDialog()
	{
		super(GUIUtil.getMainFrame(),true);
		setTitle("Manage extra class path.");
		
		
		JPanel panel=(JPanel)getContentPane();
		panel.setLayout(new BorderLayout());
		
		managePanel=new ExtraJarsManagePanel();
		panel.add(managePanel,BorderLayout.CENTER);
		
        /**
         * Button panel that places buttons for confirmation.
         */
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JButton okBtn=new RenderButton(PublicResource.getOkButtonLabel());
        okBtn.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		managePanel.addToLoader();
        		closeFrame();
        	}
        }
        );
        JButton cancelBtn=new RenderButton(PublicResource.getCancelButtonLabel());
        cancelBtn.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		closeFrame();
        	}
        }
        );
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        panel.add(buttonPanel,BorderLayout.SOUTH);
        
        addWindowListener(new WindowAdapter()
        {
        	@Override
        	public void windowClosing(WindowEvent e)
        	{
        		closeFrame();
        	}
        }
        );
        setSize(300,340);
        toCenter();
	}
	private void closeFrame()
	{
		managePanel.dispose();
		removeAll();
		dispose();
	}
}
