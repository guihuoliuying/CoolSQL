/*
 * �������� 2006-9-12
 */
package com.cattsoft.coolsql.gui.property.database;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.DriverInfo;
import com.cattsoft.coolsql.gui.property.PropertyInterface;
import com.cattsoft.coolsql.gui.property.PropertyPane;
import com.cattsoft.coolsql.pub.component.CommonFrame;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.BookMarkwizard.BookMarkWizardFrame;

/**
 * Driver information property panel
 * @author liu_xlin
 */
public class DriverProperty extends PropertyPane implements PropertyInterface,
        ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(DriverProperty.class);

	private TextEditor driverName;

    private TextEditor driverVer;

    private TextEditor driverClass;

    private TextEditor path;

    private TextEditor type;

    private Bookmark bookmark;

    private boolean isChanged=false;
    
    private RenderButton changeDriver;
    /**
     * @param ob
     */
    public DriverProperty() {
        super();
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.gui.property.PropertyPane#initContent(java.lang.Object)
     */
    public JPanel initContent() {
        JPanel pane = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        pane.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.drivername"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        driverName = new TextEditor();
        driverName.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(driverName, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.driverversion"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        driverVer = new TextEditor();
        driverVer.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(driverVer, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.driverclass"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        driverClass = new TextEditor();
        driverClass.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(driverClass, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.driverpath"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        path = new TextEditor();
        path.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(path, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.drivertype"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        type = new TextEditor();
        type.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(type, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        changeDriver= new RenderButton("changeDriver");
        changeDriver.addActionListener(this);
        pane.add(changeDriver, gbc);
        return pane;
    }

    private void initData(Bookmark bm) {
        DriverInfo driver = bm.getDriver();
        setField(driver);
        
        changeDriver.setEnabled(!bm.isConnected());
    }

    /**
     * �����ı����ֵ
     * 
     * @param driver
     */
    private void setField(DriverInfo driver) {
        driverName.setText(driver.getDriverName() == null ? "" : driver
                .getDriverName());
        driverVer.setText(driver.getDriverVersion() == null ? "" : driver
                .getDriverVersion());
        driverClass.setText(driver.getClassName() == null ? "" : driver
                .getClassName());
        path.setText(driver.getFilePath() == null ? "" : driver.getFilePath());
        type.setText(driver.getType() == null ? "" : driver.getType());
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.gui.property.PropertyInterface#set()
     */
    public boolean set() {
        if(isChanged)
        {
           
			int result=JOptionPane.showConfirmDialog(this, PublicResource
					.getSQLString("sql.propertyset.driverchange")
					, "confirm!", JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION)
			{
			    getBookMark().setClassName(driverClass.getText());
			    isChanged=false;
			    
			    JOptionPane.showMessageDialog(this, stringMgr.getString("property.driver.promptrewrite"));
				return false;
			}else
			    getBookMark().getDriver().setClassName(driverClass.getText());
			isChanged=false;
        }
        return true;
    }

    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#apply()
     */
    public void apply() {
        getBookMark().setClassName(driverClass.getText());
        isChanged=false;
        JOptionPane.showMessageDialog(this, stringMgr.getString("property.driver.promptrewrite"));
    }
    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.gui.property.PropertyInterface#quit()
     */
    public void cancel() {

    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if (ob == null)
            return;
        bookmark = (Bookmark) ob;
        initData(bookmark);
    }

    public Bookmark getBookMark() {
        return this.bookmark;
    }

    /*
     * ���� Javadoc��
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (bookmark == null) {
            JOptionPane.showMessageDialog(this, "no bookmark information!",
                    "error", 0);
            return;
        }
        
        //���¶�����ѡ�񴰿�
        BookMarkWizardFrame bwf = new BookMarkWizardFrame(
                (JDialog)CommonFrame.getParentWindow(this,1) , null, getBookMark()) {
					private static final long serialVersionUID = 1L;

			public void shutDialogProcess(Object ob) {
				
				JTable driverTable=driverPanel.getDriverList();
                int row = driverTable.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, PublicResource
                            .getString("bookmark.noselectdriver"), "warning!",
                            2);
                    return;
                }
                String className = (String) driverTable.getValueAt(row, 0);
                String oldClass=driverClass.getText();
//                	bookmark.getDriver().getClassName();
                if(className.equals(oldClass))
                {
                    this.dispose();
                    return;
                }
                isChanged=true;
                DriverInfo driver = new DriverInfo(className);
                setField(driver);
                this.dispose();
            }

            /**
             * ����ʾ��һ����ť
             * 
             * @return
             */
            protected boolean displayPreButton() {
                return false;
            }

            /**
             * ����ʾ��һ����ť
             * 
             * @return
             */
            protected boolean displayNextButton() {
                return false;
            }
        };
        bwf.enableOkButton(new DriverSelectAction(bwf), true);
        bwf.setVisible(true);

    }
    private class DriverSelectAction extends PublicAction
    {
		private static final long serialVersionUID = 1L;
		
		private BookMarkWizardFrame driverSelect;
        public DriverSelectAction(BookMarkWizardFrame b)
        {
            driverSelect=b;
        }
        /* ���� Javadoc��
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            driverSelect.shutDialogProcess(null);
            
        }
        
    }
    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
     */
    public boolean isNeedApply() {
        return true;
    }
}
