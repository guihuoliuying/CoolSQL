/**
 * 
 */
package com.cattsoft.coolsql.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import com.cattsoft.coolsql.pub.component.FileSelectDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.FileSelectFilter;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.jidesoft.swing.CheckBoxList;

/**
 * @author xiaolin
 *
 * 2008-6-26 create
 */
public class ExtraJarsManagePanel extends Panel {

	private static final long serialVersionUID = 1L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(ExtraJarsManagePanel.class);
	
	private JButton add;
	private JButton delete;
	private JButton edit;
	
	private CheckBoxList fileList;
	public ExtraJarsManagePanel()
	{
		initComponents(false);
	}
	public ExtraJarsManagePanel(boolean isSelected)
	{
		initComponents(isSelected);
	}
	protected void initComponents(final boolean isSelected)
	{
		/**
		 * The panel in which the operatable buttons are placed.
		 */
		JPanel operatePane=null;
		if(!isSelected)
		{
			operatePane = new JPanel();
			operatePane.setLayout(new BoxLayout(operatePane, BoxLayout.Y_AXIS));
			add = new RenderButton(stringMgr
					.getString("gui.extrafilemanage.add.label"));
			delete = new RenderButton(stringMgr
					.getString("gui.extrafilemanage.delete.label"));
			operatePane.add(add);
			add.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
		            FileSelectDialog chooser = new FileSelectDialog();
		            FileSelectFilter jarFilter = new FileSelectFilter(".jar",
		                    "jarFile(*.jar)");
		            chooser.addChoosableFileFilter(jarFilter);
		            chooser.addChoosableFileFilter(new FileSelectFilter(".zip",
		                    "zipFile(*.zip)"));
		            jarFilter = new FileSelectFilter(new String[]{".jar",".zip"},
		            "resourceFile(*.jar,*.zip)");
		            chooser.setFileFilter(jarFilter);
		            chooser
		                    .setCurrentDirectory(new File(
		                    		PropertyManage.getSystemProperty().getSelectFile_addDriver()));
		            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		            chooser.setMultiSelectionEnabled(true);
		            int select = chooser.showOpenDialog(GUIUtil.findLikelyOwnerWindow());
		            if (select == JFileChooser.APPROVE_OPTION) {
	
		                File[] tmp = chooser.getSelectedFiles();
		                if (tmp == null) {
		                    return;
		                }
		                PropertyManage.getSystemProperty().setSelectFile_addDriver(tmp[0].getParent());
		                for(File f:tmp)
		                {
		                	addExtraFiles(f.getAbsolutePath());
		                }
		            }
				}
			}
			);
			operatePane.add(Box.createVerticalStrut(12));
			operatePane.add(delete);
			delete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					boolean result=GUIUtil.getYesNo(stringMgr.getString("gui.extrafilemanage.delete.confirm",new Object[]{fileList.getCheckBoxListSelectedIndices().length}));
					if(result)
					{
						removeSelected();
					}
				}
			}
			);
			operatePane.add(Box.createVerticalStrut(12));
			edit=new RenderButton(stringMgr
					.getString("gui.extrafilemanage.edit.label"));
			edit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
		            FileSelectDialog chooser = new FileSelectDialog();
		            FileSelectFilter jarFilter = new FileSelectFilter(".jar",
		                    "jarFile(*.jar)");
		            chooser.addChoosableFileFilter(jarFilter);
		            chooser.addChoosableFileFilter(new FileSelectFilter(".zip",
		                    "zipFile(*.zip)"));
		            jarFilter = new FileSelectFilter(new String[]{".jar",".zip"},
		            "resourceFile(*.jar,*.zip)");
		            chooser.setFileFilter(jarFilter);
		            chooser
		                    .setCurrentDirectory(new File(
		                    		PropertyManage.getSystemProperty().getSelectFile_addDriver()));
		            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		            chooser.setMultiSelectionEnabled(false);
		            String selectedPath=fileList.getSelectedValue().toString();
		            chooser.setSelectedFile(new File(selectedPath));
		            int select = chooser.showOpenDialog(GUIUtil.findLikelyOwnerWindow());
		            if (select == JFileChooser.APPROVE_OPTION) {
	
		                File tmp = chooser.getSelectedFile();
		                String path=tmp.getAbsolutePath();
		                if(selectedPath.equals(path))
		                {
		                	return;
		                }
		                if(isExist(path))
		                {
		                	JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(), stringMgr.getString("gui.extrafilemanage.edit.fileexists"));
		                	return;
		                }
		                if (tmp == null) {
		                    return;
		                }
		                PropertyManage.getSystemProperty().setSelectFile_addDriver(tmp.getParent());
		                editFile(fileList.getSelectedIndex(),path);
		            }
				}
			}
			);
			operatePane.add(edit);
		}
		/**
		 * List component that lists all extra files.
		 */
		String[] files=LoadJar.getInstance().getExtraFiles();
		DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < files.length; i++) {
            model.addElement(files[i]);
        }
		fileList = new CheckBoxList(model) {
			private static final long serialVersionUID = 1L;

			@Override
            public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
                return -1;
            }

            @Override
            public boolean isCheckBoxEnabled(int index) {
                return true;
            }
        };
        fileList.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
			public void valueChanged(ListSelectionEvent e) {
				if(!isSelected)
					edit.setEnabled(fileList.getSelectedIndices().length==1);
			}
        }
        );
        
        /**
         * shortcut for Select All 
         */
        JPanel shortcutPanel=new JPanel();
        shortcutPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        final JCheckBox selectAllOrNot=new JCheckBox(stringMgr.getString("gui.extrafilemanage.selectAllornot"));
        selectAllOrNot.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				if(selectAllOrNot.isSelected())
					fileList.selectAll();
				else
					fileList.selectNone();
			}
		}
		);
        shortcutPanel.add(selectAllOrNot);
        
        /**
         * Main panel
         */
        setLayout(new BorderLayout());
        add(new JLabel(stringMgr.getString("gui.extrafilemanage.label")),BorderLayout.NORTH);
        add(fileList,BorderLayout.CENTER);
        if(!isSelected)
        	add(operatePane,BorderLayout.EAST);
        add(shortcutPanel,BorderLayout.SOUTH);
        
        if(!isSelected)
        	edit.setEnabled(fileList.getSelectedIndices().length==1);
	}
	public void dispose()
	{
		DefaultListModel model=(DefaultListModel)fileList.getModel();
		model.removeAllElements();
		removeAll();
	}
	private void addExtraFiles(String filePath)
	{
		if(filePath==null)
			return;
		filePath=filePath.trim();
		DefaultListModel model=(DefaultListModel)fileList.getModel();
		model.addElement(filePath);
	}
	private void editFile(int index,String filePath)
	{
		if(filePath==null)
			return;
		filePath=filePath.trim();
		DefaultListModel model=(DefaultListModel)fileList.getModel();
		model.setElementAt(filePath, index);
	}
	private void removeSelected()
	{
		DefaultListModel model=(DefaultListModel)fileList.getModel();
		Object[] selectedObjs=fileList.getCheckBoxListSelectedValues();
		for(Object value:selectedObjs)
		{
			model.removeElement(value);
		}
	}
	private boolean isExist(String filepath)
	{
		DefaultListModel model=(DefaultListModel)fileList.getModel();
		return model.contains(filepath);
	}
	public Object[] getSelectedFilePaths()
	{
		return (Object[])fileList.getCheckBoxListSelectedValues();
	}
	public void addToLoader()
	{
		DefaultListModel model=(DefaultListModel)fileList.getModel();
		String[] currentfiles=LoadJar.getInstance().getExtraFiles();
		
		String[] newArray=new String[model.getSize()];
		model.copyInto(newArray);
		
		for(int i=0;i<currentfiles.length;i++)
		{
			boolean isContain=isContain(currentfiles[i],newArray);
			if(isContain)
			{
				currentfiles[i]=null;
			}
		}
		
		/**
		 * New Files
		 */
		LoadJar loader=LoadJar.getInstance();
		for(int i=0;i<newArray.length;i++)
		{
			String path=newArray[i];
			if(path==null)
				continue;
				
			try {
				LoadJar.getInstance().addURL(new File(newArray[i]).toURI().toURL());
			} catch (MalformedURLException e) {
				LogProxy.errorLog("",e);
			}
			loader.addExtraFile(newArray[i]);
		}
		/**
		 * Delete files
		 */
		for(int i=0;i<currentfiles.length;i++)
		{
			String path=currentfiles[i];
			if(path==null)
				continue;
			loader.removeExtraFile(path);
		}
	}
	private boolean isContain(String key,String[] newArray)
	{
		if(newArray==null)
			return false;
		for(int i=0;i<newArray.length;i++)
		{
			if(key.equals(newArray[i]))
			{
				newArray[i]=null;
				return true;
			}
		}
		return false;
		
	}
	
}
