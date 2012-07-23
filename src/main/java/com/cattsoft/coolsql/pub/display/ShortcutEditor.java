/*
 * ShortcutEditor.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2007, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.cattsoft.coolsql.action.framework.ShortcutDefinition;
import com.cattsoft.coolsql.action.framework.ShortcutManager;
import com.cattsoft.coolsql.action.framework.StoreableKeyStroke;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.Setting;

/**
 * @author kenny liu
 *
 */
public class ShortcutEditor
	extends JPanel
	implements ActionListener, ListSelectionListener, WindowListener, MouseListener
{
	
	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(ShortcutEditor.class);
	private CommonDataTable keysTable;
	private JDialog window;
	private Frame parent;
	
	private JButton okButton;
	private JButton cancelButton;
	private JButton assignButton;
	private JButton resetButton;
	private JButton resetAllButton;
	private JButton clearButton;
	
	public ShortcutEditor(Frame parent)
	{
		this.parent = parent;
	}
	
	public void showWindow()
	{
		window = new JDialog(parent, "Configure shortcuts", true);
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(this);
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		this.setLayout(new BorderLayout());
		
		this.createTable();
		JScrollPane scroll = new TableScrollPane(this.keysTable);
		contentPanel.add(scroll, BorderLayout.CENTER);
		
		this.cancelButton = new RenderButton(stringMgr.getString("shortcuteditor.cancelbutton.label"));
		this.cancelButton.addActionListener(this);

		
		this.okButton = new RenderButton(stringMgr.getString("shortcuteditor.okbutton.label"));
		this.okButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(this.okButton);
		buttonPanel.add(this.cancelButton);
		this.add(buttonPanel, BorderLayout.SOUTH);

		Box b = Box.createHorizontalBox();
		Box editBox = Box.createVerticalBox();

		Dimension min = new Dimension(120, 24);
		Dimension max = new Dimension(160, 24);
		
		this.assignButton =new RenderButton(stringMgr.getString("shortcuteditor.assignbutton.label"));
		this.assignButton.setToolTipText(stringMgr.getString("shortcuteditor.assignbutton.tooltip"));
		this.assignButton.addActionListener(this);
		this.assignButton.setEnabled(false);
		this.assignButton.setPreferredSize(min);
		this.assignButton.setMinimumSize(min);
		this.assignButton.setMaximumSize(max);

		this.clearButton = new RenderButton(stringMgr.getString("shortcuteditor.clearbutton.label"));
		this.clearButton.setToolTipText(stringMgr.getString("shortcuteditor.clearbutton.tooltip"));
		this.clearButton.addActionListener(this);
		this.clearButton.setEnabled(false);
		this.clearButton.setPreferredSize(min);
		this.clearButton.setMinimumSize(min);
		this.clearButton.setMaximumSize(max);
		
		this.resetButton =new RenderButton(stringMgr.getString("shortcuteditor.resetbutton.label"));
		this.resetButton.setToolTipText(stringMgr.getString("shortcuteditor.resetbutton.tooltip"));
		this.resetButton.addActionListener(this);
		this.resetButton.setEnabled(false);
		this.resetButton.setPreferredSize(min);
		this.resetButton.setMinimumSize(min);
		this.resetButton.setMaximumSize(max);

		this.resetAllButton =new RenderButton(stringMgr.getString("shortcuteditor.resetallbutton.label"));
		this.resetAllButton.setToolTipText(stringMgr.getString("shortcuteditor.resetallbutton.tooltip"));
		this.resetAllButton.addActionListener(this);
		this.resetAllButton.setPreferredSize(min);
		this.resetAllButton.setMinimumSize(min);
		this.resetAllButton.setMaximumSize(max);
		
		editBox.add(Box.createVerticalStrut(2));
		editBox.add(this.assignButton);
		editBox.add(Box.createVerticalStrut(2));
		editBox.add(this.clearButton);
		editBox.add(Box.createVerticalStrut(15));
		editBox.add(this.resetButton);
		editBox.add(Box.createVerticalStrut(2));
		editBox.add(this.resetAllButton);
		editBox.add(Box.createVerticalGlue());
		b.add(Box.createHorizontalStrut(15));
		b.add(editBox);
		b.add(Box.createHorizontalStrut(5));
		contentPanel.add(b, BorderLayout.EAST);

//		JPanel p = new JPanel();
//		Dimension d = new Dimension(1,20);
//		p.setMinimumSize(d);
//		p.setPreferredSize(d);
//		p.setBorder(new DividerBorder(DividerBorder.HORIZONTAL_MIDDLE));
		contentPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);

		JPanel p = new JPanel();
		Dimension d = new Dimension(5,1);
		p.setMinimumSize(d);
		p.setPreferredSize(d);
		contentPanel.add(p, BorderLayout.WEST);

		p = new JPanel();
		d = new Dimension(1,5);
		p.setMinimumSize(d);
		p.setPreferredSize(d);
		contentPanel.add(p, BorderLayout.NORTH);
		
		this.add(contentPanel, BorderLayout.CENTER);
		
		window.getContentPane().add(this);
		window.setSize(600,400);

		GUIUtil.centerToOwnerWindow(window);
		window.setVisible(true);
	}
	
	private void createTable()
	{
		ShortcutManager mgr = Setting.getInstance().getShortcutManager();
		ShortcutDefinition[] keys = mgr.getDefinitions();
		
		Vector<String> headerVector=new Vector<String>(3);
		headerVector.add(stringMgr.getString("shortcuteditor.table.column0"));
		headerVector.add(stringMgr.getString("shortcuteditor.table.column1"));
		headerVector.add(stringMgr.getString("shortcuteditor.table.column2"));
		
		Vector<Vector<?>> dataVector=new Vector<Vector<?>>(3);
		for (int i=0; i < keys.length; i++)
		{
			String cls = keys[i].getActionClass();
			String title = mgr.getActionNameForClass(cls);
			String tooltip = mgr.getTooltip(cls);
			ActionDisplay disp = new ActionDisplay(title, tooltip);
			Vector<Object> rowData=new Vector<Object>();
			rowData.add(disp);
			rowData.add( new ShortcutDisplay(keys[i], ShortcutDisplay.TYPE_PRIMARY_KEY));
			rowData.add( new ShortcutDisplay(keys[i], ShortcutDisplay.TYPE_DEFAULT_KEY));
			
			dataVector.add(rowData);
		}
		this.keysTable = new CommonDataTable(dataVector,headerVector);
		this.keysTable.setPopMenuDisplay(true);
		this.keysTable.setEnableToolTip(false);
		this.keysTable.setRowSelectionAllowed(true);
		this.keysTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.keysTable.addMouseListener(this);
		this.keysTable.adjustPerfectWidth();
//		TableColumn col = this.keysTable.getColumnModel().getColumn(0);
//		col.setCellRenderer(new ActionDisplayRenderer());
		this.keysTable.getSelectionModel().addListSelectionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		
		if (source == this.cancelButton) 
		{
			this.closeWindow();
		}
		else if (source == this.okButton)
		{
			this.saveShortcuts();
			this.closeWindow();
		}
		else if (source == this.assignButton)
		{
			this.assignKey();
		}
		else if (source == this.resetButton)
		{
			this.resetCurrentKey();
		}
		else if (source == this.resetAllButton)
		{
			this.resetAllKeys();
		}
		else if (source == this.clearButton)
		{
			this.clearKey();
		}
//		else if (e.getActionCommand().equals(escActionCommand))
//		{
//			this.closeWindow();
//		}
	}

//	private void saveSettings()
//	{
//		Settings.getInstance().storeWindowSize(this.window, KEY_WINDOW_SIZE);
//	}

	private void saveShortcuts()
	{
		ShortcutManager mgr = Setting.getInstance().getShortcutManager();
		int count = this.keysTable.getRowCount();
		for (int row = 0; row < count; row++)
		{
			ShortcutDisplay d = (ShortcutDisplay)this.keysTable.getValueAt(row, 1);
			ShortcutDefinition def = d.getShortcut();
			if (d.isModified())
			{	
				if (d.isCleared())
				{
					mgr.removeShortcut(def.getActionClass());
				}
				else if (d.doReset())
				{
					mgr.resetToDefault(def.getActionClass());
				}
				else
				{
					mgr.assignKey(def.getActionClass(), d.getNewKey().getKeyStroke());
				}
			}
		}
		mgr.updateActions();
	}
	
	private void closeWindow()
	{
//		this.saveSettings();
		this.window.setVisible(false);
		this.window.dispose();
	}

	public void valueChanged(ListSelectionEvent e)
	{
		boolean enabled = (e.getFirstIndex() >= 0);
		this.resetButton.setEnabled(enabled);
		this.assignButton.setEnabled(enabled);
		this.clearButton.setEnabled(enabled);
	}
	
	private void assignKey()
	{
		int row = this.keysTable.getSelectedRow();
		if (row < 0) return;
		final KeyboardMapper mapper = new KeyboardMapper();
		EventQueue.invokeLater(new Runnable() 
		{
			public void run()
			{
				mapper.grabFocus();
			}
		});
		String[] options = new String[] { stringMgr.getString("shortcuteditor.okbutton.label"), 
				stringMgr.getString("shortcuteditor.cancelbutton.label")};
		
		JOptionPane overwritePane = new JOptionPane(mapper, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options);
		JDialog dialog = overwritePane.createDialog(this, stringMgr.getString("shortcuteditor.assigndialog.title"));

		dialog.setResizable(true);
		dialog.setVisible(true);
		Object result = overwritePane.getValue();
		dialog.dispose();
		
		if (options[0].equals(result))
		{
			KeyStroke key = mapper.getKeyStroke();
			int oldrow = this.findKey(key);
			DefaultTableModel model=(DefaultTableModel)this.keysTable.getModel();
			if (oldrow > -1)
			{
				String name = this.keysTable.getValueAt(oldrow, 0).toString();
				String msg = stringMgr.getString("shortcuteditor.assigndialog.alreadyassigned",name);
				boolean choice = GUIUtil.getYesNo(this, msg);
				if (!choice) return;
				ShortcutDisplay old = (ShortcutDisplay)this.keysTable.getValueAt(oldrow, 1);
				old.clearKey();
				model.fireTableRowsUpdated(oldrow, oldrow);
			}
			ShortcutDisplay d = (ShortcutDisplay)this.keysTable.getValueAt(row, 1);
			d.setNewKey(key);
			model.fireTableRowsUpdated(row, row);
		}
	}
	
	private void clearKey()
	{
		int row = this.keysTable.getSelectedRow();
		if (row < 0) return;
		ShortcutDisplay old = (ShortcutDisplay)this.keysTable.getValueAt(row, 1);
		old.clearKey();
		DefaultTableModel model=(DefaultTableModel)this.keysTable.getModel();
		model.fireTableRowsUpdated(row, row);
	}
	
	private void resetCurrentKey()
	{
		int row = this.keysTable.getSelectedRow();
		if (row < 0) return;
		ShortcutDisplay d = (ShortcutDisplay)this.keysTable.getValueAt(row, 1);
		d.resetToDefault();
		DefaultTableModel model=(DefaultTableModel)this.keysTable.getModel();
		model.fireTableRowsUpdated(row, row);
//		WbSwingUtilities.repaintNow(this);
	}

	private void resetAllKeys()
	{
		int selected = this.keysTable.getSelectedRow();
		int count = this.keysTable.getRowCount();
		DefaultTableModel model=(DefaultTableModel)this.keysTable.getModel();
		for (int row=0; row < count; row++)
		{	
			ShortcutDisplay d = (ShortcutDisplay)this.keysTable.getValueAt(row, 1);
			d.resetToDefault();
		}
		model.fireTableDataChanged();
		if (selected > -1)
		{	
			this.keysTable.getSelectionModel().setSelectionInterval(selected, selected);
		}
	}
	
	private int findKey(KeyStroke key)
	{
		int count = this.keysTable.getRowCount();
		for (int row = 0; row < count; row++)
		{
			ShortcutDisplay d = (ShortcutDisplay)this.keysTable.getValueAt(row, 1);
			if (!d.isCleared() && d.isMappedTo(key))
			{
				return row;
			}
		}
		return -1;
	}

	public void windowActivated(WindowEvent e)
	{
	}

	public void windowClosed(WindowEvent e)
	{
	}

	public void windowClosing(WindowEvent e)
	{
		this.closeWindow();	
	}

	public void windowDeactivated(WindowEvent e)
	{
	}

	public void windowDeiconified(WindowEvent e)
	{
	}

	public void windowIconified(WindowEvent e)
	{
	}

	public void windowOpened(WindowEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() == this.keysTable && 
				e.getClickCount() == 2 &&
				e.getButton() == MouseEvent.BUTTON1)
		{
			this.assignKey();
		}
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}
	class KeyboardMapper extends JPanel implements KeyListener {
		private static final long serialVersionUID = 1L;
		private JTextField display;
		private KeyStroke newkey;

		public KeyboardMapper() {
			this.display = new JTextField(20);
			this.display.addKeyListener(this);
			this.display.setEditable(false);
			this.display.setDisabledTextColor(Color.BLACK);
			this.display.setBackground(Color.WHITE);
			this.add(display);
		}

		public void grabFocus() {
			this.display.grabFocus();
			this.display.requestFocusInWindow();
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			int modifier = e.getModifiers();
			int code = e.getKeyCode();

			// only allow function keys without modifier!
			if (modifier == 0) {
				if (code < KeyEvent.VK_F1 || code > KeyEvent.VK_F12)
					return;
			}

			// keyReleased is also called when the Ctrl or Shift keys are
			// release
			// in that case the keycode is 0 --> ignore it
			if (code >= 32) {
				String key = "";
				if (modifier > 0)
					key = KeyEvent.getKeyModifiersText(modifier) + "-";
				key = key + KeyEvent.getKeyText(code);
				this.newkey = KeyStroke.getKeyStroke(code, modifier);
				this.display.setText(key);
			}
		}

		public KeyStroke getKeyStroke() {
			return this.newkey;
		}

		public void keyTyped(KeyEvent e) {
		}

	}
}
class ActionDisplay implements Comparable<Object> {

	public String text;
	public String tooltip;

	public ActionDisplay(String txt, String tip) {
		text = txt;
		tooltip = tip;
	}

	public int compareTo(Object other) {
		ActionDisplay a = (ActionDisplay) other;
		return text.compareToIgnoreCase(a.text);
	}

	public String toString() {
		return text;
	}
}
class ActionDisplayRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	public ActionDisplayRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		try {
			ActionDisplay d = (ActionDisplay) value;
			this.setToolTipText(d.tooltip);
			if (isSelected) {
				this.setBackground(table.getSelectionBackground());
				this.setForeground(table.getSelectionForeground());
			} else {
				this.setForeground(table.getForeground());
			}
			return super.getTableCellRendererComponent(table, d.text,
					isSelected, hasFocus, row, column);
		} catch (Exception e) {
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
		}

	}

	public int getDisplayWidth() {
		return getText().length();
	}

	public String getDisplayValue() {
		return getText();
	}
}

class ShortcutDisplay
{
	public static final int TYPE_DEFAULT_KEY = 1;
	public static final int TYPE_PRIMARY_KEY = 2;
	public static final int TYPE_ALTERNATE_KEY = 3;
	
	private boolean isModified = false;
	private int displayType;
	private ShortcutDefinition shortcut;
	private boolean clearKey = false;
	private boolean resetToDefault = false;
	
	private StoreableKeyStroke newKey = null;
	
	ShortcutDisplay(ShortcutDefinition def, int type)
	{
		this.shortcut = def;
		this.displayType = type;
	}
	
	public ShortcutDefinition getShortcut()
	{
		return this.shortcut;
	}

	public boolean isModified() { return this.isModified; }
	public boolean isCleared()
	{
		return this.clearKey;
	}
	
	public void clearKey()
	{
		this.newKey = null;
		this.clearKey = true;
		this.isModified = true;
		this.resetToDefault = false;
	}
	
	public void setNewKey(KeyStroke aKey)
	{
		this.newKey = new StoreableKeyStroke(aKey);
		this.isModified = true;
		this.resetToDefault = false;
		this.clearKey = false;
	}

	public StoreableKeyStroke getNewKey()
	{
		return this.newKey;
	}
	
	public boolean isMappedTo(KeyStroke aKey)
	{
		boolean mapped = false;
		if (newKey != null)
		{
			mapped = newKey.equals(aKey);
		}
		if (!mapped)
		{
			mapped = this.shortcut.isMappedTo(aKey);
		}
		return mapped;
	}
	
	public boolean doReset()
	{
		return this.resetToDefault;
	}
	
	public void resetToDefault()
	{
		this.isModified = true;
		this.newKey = null;
		this.clearKey = false;
		this.resetToDefault = true;
	}
	
	public String toString()
	{
		StoreableKeyStroke key = null;
		switch (this.displayType)
		{
			case TYPE_DEFAULT_KEY:
				key = this.shortcut.getDefaultKey();
				break;
			case TYPE_PRIMARY_KEY:
				if (this.clearKey)
				{
					key = null;
				}
				else if (this.resetToDefault)
				{
					key = this.shortcut.getDefaultKey();
				}
				else if (this.newKey == null)
				{
					key = this.shortcut.getActiveKey();
				}
				else 
				{
					key = this.newKey;
				}
				break;
			case TYPE_ALTERNATE_KEY:
				key = this.shortcut.getAlternateKey();
				break;
		}
		if (key == null) return "";
		return key.toString();
	}
}
