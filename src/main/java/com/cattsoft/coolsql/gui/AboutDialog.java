package com.cattsoft.coolsql.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.CommonDataTable;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.FileUtil;
import com.cattsoft.coolsql.system.VersionLabel;

/**
 * About dialog
 * @author ��Т�� kenny liu
 *
 * 2007-12-24 create
 */
public class AboutDialog extends BaseDialog
{
	private static final long serialVersionUID = 1L;

	public AboutDialog(JFrame frame)
	{
		super(frame,true);
		super.setTitle("About dialog");
		initDialog();
	}
	private void initDialog()
	{
		JPanel content=(JPanel)getContentPane();
		
		JTabbedPane tabPane=new JTabbedPane();
		
		/**
		 *-------------------- Logo tab
		 */
		JPanel logoTab=new JPanel();
		logoTab.setLayout(new BorderLayout());
        /** The theme icon is placed on the top of frame*/
		Icon themeIcon=PublicResource.getIcon("system.splash.icon");
        JLabel themeLabel=new JLabel(themeIcon);
        logoTab.add(themeLabel,BorderLayout.CENTER);
        
        /** version information*/
        VersionLabel version=new VersionLabel();
        logoTab.setBackground(new Color(216,233,236));
        logoTab.add(version,BorderLayout.SOUTH);
        
        tabPane.addTab(PublicResource.getUtilString("system.aboutdialog.tab.about"), logoTab);
        
        /**
         * Credits tab
         */
        JTextArea contentArea=new JTextArea();
        contentArea.setEditable(false);
        contentArea.setText(getCreditInfo());
        
        tabPane.addTab("credit", contentArea);
        
        /**
         * ---------------------system property tab
         */
        Vector<String> head=new Vector<String>();
        head.add("key");
        head.add("value");

        Vector<Vector<String>> data=new Vector<Vector<String>>();
        Properties props=System.getProperties();
        Iterator<Object> it=props.keySet().iterator();
        while(it.hasNext())
        {
        	Vector<String> rowData=new Vector<String>();
        	String key=(String)it.next();
        	rowData.add(key);
        	rowData.add(props.getProperty(key));
        	data.add(rowData);
        }
               
        CommonDataTable table=new CommonDataTable(data,head);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.adjustPerfectWidth();
        tabPane.addTab(PublicResource.getUtilString("system.aboutdialog.tab.systemproperty"), new TableScrollPane(table));
        
        content.add(tabPane,BorderLayout.CENTER);
        /**
         * button panel(okbutton)
         */
        JPanel buttonPane=new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        RenderButton okBtn=new RenderButton(PublicResource.getString("propertyframe.button.ok"));
        okBtn.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		dispose();
        	}
        }
        );
        buttonPane.add(okBtn);
        content.add(buttonPane,BorderLayout.SOUTH);
        
        pack();
//        setResizable(false);
        toCenter();
	}
	private String getCreditInfo()
	{
		InputStream inputStream=getClass().getResourceAsStream("/com/coolsql/resource/credit");
		try {
			byte[] b=FileUtil.readBytes(inputStream);
			return new String(b);
		} catch (IOException e) {
			return "";
		}
		
	}
}