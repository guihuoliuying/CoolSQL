/*
 * Created on 2007-1-31
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.cattsoft.coolsql.data.display.action.BaseTableFindAction;
import com.cattsoft.coolsql.data.display.action.DefaultAdjustWidthAction;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.resultset.EditableTableHeaderUI;

/**
 * @author liu_xlin ��չ��ؼ������ӳ��õĹ��ܡ� 1��ͨ�����м�˫����������ѿ��
 */
public class BaseTable extends JTable {

	private static final long serialVersionUID = -5140592536380196024L;

	public static final String PROPERTY_DELETEDROW = "deletedRowChanged";
	
	private Action preferWidthAction; // ������ѿ�ȵ��¼�����

	private Action findAction; // ���ҵ�Ԫ����Ϣ

	/**
	 * determine whether row of table displays border.
	 */
	private boolean isDisplayRowBorder = Setting.getInstance().getBoolProperty(
			PropertyConstant.PROPERTY_VIEW_PUBLIC_ISSHOWTABLEROWBORDER, true);

	/**
	 * save the row number on which mouse is currently
	 */
	private int mouseOverRow = -1;
	
	private boolean isRowNumberVisible;
	
	private boolean isAllowResizeRowHeight;
	private TableRowDataChangeListener rowChangedListener;
	
	private RowHeightResizeHander resizeHander;
	public BaseTable() {
		super();
		initTable();
	}
	public BaseTable(int rows, int columns) {
		super(rows, columns);
		initTable();
	}
	public BaseTable(Object[][] data, Object[] header) {
		super(data, header);
		initTable();
	}
	public BaseTable(TableModel model) {
		super(model);
		initTable();
	}
	public BaseTable(TableModel model, TableColumnModel columnModel) {
		super(model, columnModel);
		initTable();
	}
	public BaseTable(TableModel model, TableColumnModel columnModel,
			ListSelectionModel sm) {
		super(model, columnModel, sm);
		initTable();
	}
	public BaseTable(Vector data, Vector header) {
		super(data, header);
		initTable();
	}
	protected void initTable() {
		getTableHeader().setUI(new EditableTableHeaderUI());
		resetDefaultCellRender();

		addMouseListener(new MouseExitListener());
		addMouseMotionListener(new MouseMoveListener());
		GUIUtil.bindShortKey(this, "alt W", getDefaultAdjustWidthAction(),
				false);
		GUIUtil.bindShortKey(this, "alt F", getDefaultFindAction(), false);
		
		//Initializing the showlinenumber, showhorizontalline, showverticalline property.
		isRowNumberVisible=Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_ISDISPLAYLINENUMBER, true);
		setShowHorizontalLines(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_HORIZONTALLINE, true));
		setShowVerticalLines(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_DISPLAY_VERTICALLINE, true));
		isAllowResizeRowHeight=Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_COMMONTABLE_RESIZEROWHEIGHT, false);
		if(isAllowResizeRowHeight)
		{
			resizeHander=new RowHeightResizeHander(this);
		}
		
		setRowHeight(Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_COMMONTABLE_ROWHEIGHT, 18));
	}
	/**
	 * ȱʡ�Ŀ�ȵ�������
	 * 
	 * @return
	 */
	public synchronized Action getDefaultAdjustWidthAction() {
		if (preferWidthAction == null)
			preferWidthAction = new DefaultAdjustWidthAction(this);
		return preferWidthAction;
	}
	/**
	 * ���������Ϣ���¼�����
	 * 
	 * @return
	 */
	public synchronized Action getDefaultFindAction() {
		if (findAction == null)
			findAction = new BaseTableFindAction(this);
		return findAction;
	}
	public boolean isAllowResizeRowHeight()
	{
		return isAllowResizeRowHeight;
	}
	public void setAllowResizeRowHeight(boolean b)
	{
		boolean old=isAllowResizeRowHeight;
		isAllowResizeRowHeight=b;
		if(isAllowResizeRowHeight==old)
		{
			return;
		}
		if(isAllowResizeRowHeight)
		{
			if(resizeHander!=null)
				resizeHander.done();
			resizeHander=new RowHeightResizeHander(this);
		}else
		{
			if(resizeHander!=null)
			{
				resizeHander.done();
				resizeHander=null;
			}
		}
	}
	/**
	 * ��������ÿ�еĵ�Ԫ����Ⱦ��
	 * 
	 */
	private void resetDefaultCellRender() {
		TableColumnModel columnModel = getColumnModel();
		BaseTableCellRender render = new BaseTableCellRender();
		for (int i = 0; i < getColumnCount(); i++) {
			columnModel.getColumn(i).setCellRenderer(render);
		}
	}
	/**
	 * ������еĿ��
	 * 
	 * @param columnIndex
	 *            --������
	 * @param width
	 *            --�µĿ��ֵ
	 */
	public void adjustColumnWidth(int columnIndex, int width) {
		columnModel.getColumn(columnIndex).setPreferredWidth(width);
	}
	/**
	 * ��ָ���еĿ�ȵ��������
	 * 
	 * @param columnIndex
	 *            --ָ���е�����
	 */
	public void columnsToFitWidth(int columnIndex) {
		if (columnIndex < 0)
			return;

		TableColumnModel columnModel = this.getColumnModel();
		int[] width = GUIUtil.getPreferredColumnWidth(this,
				new int[]{columnIndex});
		columnModel.getColumn(columnIndex).setPreferredWidth(width[0]);
	}
	public void columnsToFitWidth(int[] columnIndex) {
		if (columnIndex == null || columnIndex.length < 1)
			return;

		TableColumnModel columnModel = this.getColumnModel();
		int[] width = GUIUtil.getPreferredColumnWidth(this, columnIndex);
		for (int i = 0; i < width.length; i++)
			columnModel.getColumn(columnIndex[i]).setPreferredWidth(width[i]);
	}
	/**
	 * adjust all column to perfect width.
	 * 
	 */
	public void adjustPerfectWidth() {
		int cols = getColumnCount();
		int[] columns = new int[cols];
		for (int i = 0; i < cols; i++) {
			columns[i] = i;
		}
		columnsToFitWidth(columns);
	}
	/**
	 * @return the isDisplayRowBorder
	 */
	public boolean isDisplayRowBorder() {
		return this.isDisplayRowBorder;
	}
	/**
	 * @param isDisplayRowBorder
	 *            the isDisplayRowBorder to set
	 */
	public void setDisplayRowBorder(boolean isDisplayRowBorder) {
		this.isDisplayRowBorder = isDisplayRowBorder;
		repaint();
	}
	/**
	 * @return the mouseOverRow
	 */
	public int getMouseOverRow() {
		return this.mouseOverRow;
	}
	/**
	 * @param mouseOverRow
	 *            the mouseOverRow to set
	 */
	public void setMouseOverRow(int mouseOverRow) {
		this.mouseOverRow = mouseOverRow;
	}
	/**
	 * Return the icon of specified row displayed with row number in the left section of scroll pane.
	 * @param row the index of row in the view.
	 */
	public Icon getRowIcon(int row) {
		return null;
	}
	public boolean isRowNumberVisible()
	{
		return isRowNumberVisible;
	}
	/**
	 * Set row line number of table visible or invisible
	 * @param isVisible
	 */
	public void setTableRowNumberVisible(boolean isVisible)
	{
		isRowNumberVisible=isVisible;
		Container con=getParent();
    	if(!(con instanceof JViewport))
    	{
    		return;
    	}
    	con=con.getParent();
    	if(!(con instanceof JScrollPane))
    	{
    		return;
    	}
    	JScrollPane scrollPane=(JScrollPane)con;
        if(isVisible)  //set header component
        {
        	JViewport rowHeaderView=scrollPane.getRowHeader();
        	if(rowHeaderView!=null&&rowHeaderView.getView() instanceof PrivateList)
        	{
        		return;
        	}else
        	{
        		JList list=getRowHeadList();
        		list.setBackground(scrollPane.getBackground());
        		scrollPane.setRowHeaderView(list);
        		if(rowChangedListener==null)
        			rowChangedListener=new TableRowDataChangeListener(list);
        		rowChangedListener.updateList(list);
        		getModel().addTableModelListener(rowChangedListener);
        	}
        	
        }else //remove header component
        {
        	JViewport rowHeaderView=scrollPane.getRowHeader();
        	if(rowHeaderView!=null&&rowHeaderView.getView() instanceof JList)
        	{
        		if(rowChangedListener!=null)
        			getModel().removeTableModelListener(rowChangedListener);
        		rowHeaderView.remove(rowHeaderView.getView());
        		scrollPane.getParent().validate();
        	}
        }
	}
	JList getRowHeadList() {
		int tableRowCount = getRowCount();
		final PrivateList rowHeader = new PrivateList(tableRowCount);

		rowHeader.setFixedCellHeight(getRowHeight());
		
		RowHeaderRenderer cellRender=new RowHeaderRenderer(this);
		rowHeader.setCellRenderer(cellRender);
		int with=(int)cellRender.getListCellRendererComponent(rowHeader, null, tableRowCount-1, false, false)
						.getPreferredSize().getWidth() + 16;
		rowHeader.setFixedCellWidth(with);

		addPropertyChangeListener(new PropertyChangeListener()
		{

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("rowHeight")) {
					rowHeader.setFixedCellHeight(getRowHeight());
				}
				
			}
			
		}
		);
		
		return rowHeader;
	}
	class PrivateList extends JList
	{
		private static final long serialVersionUID = 1L;
		 public PrivateList(int length)
		 {
			 super(new RowNumberListModel(length));
			 RowNumberListModel model=(RowNumberListModel)getModel();
			 model.setListObject(this);
			 
			 BaseTable.this.addPropertyChangeListener(PROPERTY_DELETEDROW, new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					repaint();
				}
				 
			 });
		 }
	}
	class RowNumberListModel extends AbstractListModel
	{
		private static final long serialVersionUID = 1L;
		private int length;
		private JList list;
		public RowNumberListModel(int length)
		{
			this.length=length;
		}
		public RowNumberListModel(int length,JList list)
		{
			this.length=length;
			this.list=list;
		}
		public void setListObject(JList list)
		{
			this.list=list;
		}
		/* (non-Javadoc)
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		public Object getElementAt(int index) {
			return null;
		}
		/* (non-Javadoc)
		 * @see javax.swing.ListModel#getSize()
		 */
		public int getSize() {
			
			return length;
		}
		public void setSize(int length)
		{
			if(length!=this.length)
			{
				this.length=length;
				list.repaint();
			}
		}
	}
	class RowHeaderRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = 1L;

		private JTable table = null;
		RowHeaderRenderer(JTable table) {
			super();
			
			this.table = table;
			
			JTableHeader header = table.getTableHeader();
			setOpaque(true);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(CENTER);
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
			if (table instanceof BaseTable) {
				setHorizontalTextPosition(SwingConstants.RIGHT);
			}
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText("" + (index + 1));
			if (table instanceof BaseTable) {
				BaseTable t = (BaseTable) table;
				Icon icon = t.getRowIcon(index);
				setIcon(icon);
				
				if (icon != null) {
					int preferedWidth = this.getPreferredSize().width + 2;
					if (list.getFixedCellWidth() < preferedWidth ) {
						list.setFixedCellWidth(preferedWidth);
					}
				}
			}
			return this;
		}
	}
	class TableRowDataChangeListener implements TableModelListener {
		private JList list;
		public TableRowDataChangeListener(JList list) {
			this.list = list;
		}
		void updateList(JList list){
			this.list=list;
		}
		public void tableChanged(TableModelEvent e) {

			RowNumberListModel listModel = (RowNumberListModel) list.getModel();
			int listRowCount = listModel.getSize();
			int tableRowCount = getRowCount();
			if (tableRowCount ==listRowCount)
				return;
			
			listModel.setSize(tableRowCount);
			ListCellRenderer cellRender=list.getCellRenderer();
			int with=(int)cellRender.getListCellRendererComponent(list, null, tableRowCount-1, false, false)
			.getPreferredSize().getWidth() + 16;
			list.setFixedCellWidth(with);
		}
	}
	/**
	 * listen to moving of mouse on the table
	 * 
	 * @author ��Т��
	 * 
	 * 2008-1-9 create
	 */
	private class MouseMoveListener extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			processMouseMotion(e);
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			processMouseMotion(e);
		}
		private void processMouseMotion(MouseEvent e) {
			JTable table = (JTable) e.getSource();

			int row = table.rowAtPoint(e.getPoint());
			if (row != mouseOverRow) {
				mouseOverRow = row;
				table.repaint();
			}
		}
	}
	/**
	 * listen to exiting of mouse on the table
	 * 
	 * @author ��Т��
	 * 
	 * 2008-1-9 create
	 */
	private class MouseExitListener extends MouseAdapter {
		@Override
		public void mouseExited(MouseEvent e) {
			mouseOverRow = -1;
			repaint();
		}
	}
}
