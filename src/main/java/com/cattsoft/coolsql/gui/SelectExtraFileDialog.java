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
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author xiaolin
 *
 * 2008-7-3 create
 */
public class SelectExtraFileDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private ExtraJarsManagePanel selectPanel;
	
	private File[] selectedFiles;
	public SelectExtraFileDialog()
	{
		super(GUIUtil.getMainFrame(),true);
		setTitle("Select files from extra classpath");
		initComponents();
	}
	protected void initComponents()
	{
		JPanel panel=(JPanel)getContentPane();
		panel.setLayout(new BorderLayout());
		
		selectPanel=new ExtraJarsManagePanel(true);
		panel.add(selectPanel,BorderLayout.CENTER);
		
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
        		Object[] tmp= selectPanel.getSelectedFilePaths();
        		if(tmp==null||tmp.length==0)
        		{
        			selectedFiles=null;
        			return;
        		}else
        		{
	        		selectedFiles=new File[tmp.length];
	        		for(int i=0;i<tmp.length;i++)
	        		{
	        			selectedFiles[i]=new File(tmp[i].toString());
	        		}
        		}
        		closeFrame();
        	}
        }
        );
        JButton cancelBtn=new RenderButton(PublicResource.getCancelButtonLabel());
        cancelBtn.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		selectedFiles=null;
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
		selectPanel.dispose();
		removeAll();
		dispose();
	}
	/**
	 * Select files from classpath.
	 * @return return null if cancelling or no file was selected
	 */
	public static File[] selectExtraFile()
	{
		SelectExtraFileDialog dialog=new SelectExtraFileDialog();
		dialog.toCenter();
		dialog.setVisible(true);
		return dialog.selectedFiles;
	}
}
