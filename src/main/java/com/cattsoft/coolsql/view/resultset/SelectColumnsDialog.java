/**
 * Create date:2008-5-14
 */
package com.cattsoft.coolsql.view.resultset;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.BaseTable;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.jidesoft.swing.MultilineLabel;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-14 create
 */
public class SelectColumnsDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(SelectColumnsDialog.class);
	
	private static final String column0=stringMgr.getString("resultset.instantupdate.selectcolumndialog.table.column0");
	private static final String column1=stringMgr.getString("resultset.instantupdate.selectcolumndialog.table.column1");
	
	private JTable table;
	
	private boolean isDoSelection;
	public SelectColumnsDialog(String[] columns)
	{
		this(columns,null);
	}
	public SelectColumnsDialog(String[] columns,Boolean[] selectedFlag)
	{
		super(GUIUtil.getMainFrame(),true);
		setTitle("Select columns as keys");
		
		JPanel main=(JPanel)getContentPane();
		main.setLayout(new BorderLayout());
		
		MultilineLabel label=new MultilineLabel(stringMgr.getString("resultset.instantupdate.selectcolumndialog.info"));
		main.add(label,BorderLayout.NORTH);
		
		/** selectArea */
		JPanel selectArea=new JPanel(new BorderLayout());
		table=new BaseTable();
		table.setModel(new SelectColumnsTableModel(columns,selectedFlag));
		
		final JCheckBox isSelectAll=new JCheckBox(stringMgr.getString("resultset.instantupdate.selectcolumndialog.isselectall"));
		isSelectAll.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				((SelectColumnsTableModel)table.getModel()).setFlagForAll(isSelectAll.isSelected());
				table.repaint();
			}
		}
		);
		selectArea.add(isSelectAll,BorderLayout.SOUTH);
		
		selectArea.add(new TableScrollPane(table),BorderLayout.CENTER);
		
		main.add(selectArea,BorderLayout.CENTER);
		
		/**button area */
		JPanel buttonPane=new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton okButton=new RenderButton(PublicResource.getOkButtonLabel());
		okButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				doClose(true);
			}
		}
		);
		buttonPane.add(okButton);
		
		JButton quitButton=new RenderButton(PublicResource.getCancelButtonLabel());
		quitButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				doClose(false);
			}
		}
		);
		buttonPane.add(quitButton);
		
		main.add(buttonPane,BorderLayout.SOUTH);
		
		getRootPane().setDefaultButton(okButton);
		setSize(350,300);
		toCenter();
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				doClose(false);
			}
		}
		);
	}
	void doClose(boolean isDoSelection)
	{
		this.isDoSelection=isDoSelection;
		removeAll();
		dispose();
	}
	public String[] getSelectedColumns()
	{
		List<String> tmpList=new ArrayList<String>(table.getRowCount());
		TableModel model=table.getModel();
		for(int i=0;i<table.getRowCount();i++)
		{
			if((Boolean)model.getValueAt(i,1))
			{
				tmpList.add((String)model.getValueAt(i,0));
			}
		}
		return tmpList.toArray(new String[tmpList.size()]);
	}
	public boolean isDoSelection()
	{
		return isDoSelection;
	}
	public void setColumnSelected(int i, boolean flag)
	{
		((SelectColumnsTableModel)table.getModel()).selected[i] = flag;
	}
	private class SelectColumnsTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;
		
		String[] columns;
		Boolean[] selected;
		private int rows;
		
		public SelectColumnsTableModel(String[] columns)
		{
			this(columns,null);
		}
		public SelectColumnsTableModel(String[] columns,Boolean[] selectedFlag)
		{
			this.columns=columns;
			this.rows=columns.length;
			if(selectedFlag==null)
				this.selected = new Boolean[this.rows];
			else
				this.selected =selectedFlag;
		}
		public int getColumnCount()
		{
			return 2;
		}
		public Object getValueAt(int row, int column)
		{
			if (column == 0)
			{
				return this.columns[row];
			}
			else if (column == 1)
			{
				return Boolean.valueOf(this.selected[row]);
			}
			return "";
		}

		public void selectAll()
		{
			this.setFlagForAll(true);
		}

		public void selectNone()
		{
			this.setFlagForAll(false);
		}
		
		private void setFlagForAll(boolean flag)
		{
			for (int i=0; i < this.rows; i++)
			{
				this.selected[i] = flag;
			}
		}
		public int getRowCount()
		{
			return this.rows;
		}
		public Class<?> getColumnClass(int columnIndex)
		{
			if (columnIndex == 0) return String.class;
			else return Boolean.class;
		}
		
		public String getColumnName(int columnIndex)
		{
			if (columnIndex == 0) return column0;
			return column1;
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return (columnIndex == 1);
		}
		public void setValueAt(Object aValue, int rowIndex, int columnIndex)
		{
			if (columnIndex == 1 && aValue instanceof Boolean)
			{
				Boolean b = (Boolean)aValue;
				this.selected[rowIndex] = b.booleanValue();
			}
		}
	}
}
