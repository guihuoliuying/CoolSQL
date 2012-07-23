/*
 * �������� 2006-6-25
 *
 */
package com.cattsoft.coolsql.view.resultset;

/**
 * @author liu_xlin
 *  
 */
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.cattsoft.coolsql.pub.util.StringUtil;

public class SortableTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTYNAME_ORDER = "order";
	/**
     * ��¼�޸ĺ����� key=row+column value=newvalue
     */
    protected Map<String,Object> modifyData = null;
    /**
     * The rows that should be deleted.
     */
    protected Set<Integer> deletedRows = null;

    private MyTableModelListener tableListener;

    protected int _iColumn;

    protected boolean _bAscending;

    private DefaultTableModel _actualModel;

    protected Integer[] _indexes;

    protected Vector<PropertyChangeListener> pcs;

    /**
     * �бȽ���
     */
    private TableModelComparator comparator=null;
    public TableModel getActualModel() {
        return _actualModel;
    }

    public SortableTableModel() {
        this(null);
    }

    @SuppressWarnings("serial")
	public SortableTableModel(DefaultTableModel model) {
        super();
        
        deletedRows = new HashSet<Integer>() {
        	@Override
        	public boolean add(Integer v) {
        		boolean isAdded = super.add(v);
        		
        		if (isAdded) {
	        		TableModelModifyEvent event=new TableModelModifyEvent(
	        				this,TableModelModifyEvent.ACTION_TYPE_DELETE, null, v, true);
	        		fireDataChanged(event);
        		}
        		
        		return isAdded;
        	}
        	@Override
        	public boolean remove(Object v) {
        		boolean isRemoved = super.remove(v);
        		
        		if (isRemoved) {
        			TableModelModifyEvent event=new TableModelModifyEvent(
	        				this,TableModelModifyEvent.ACTION_TYPE_CANCEL_DELETE, null, v, size() > 0 || modifyData.size() > 0);
	        		fireDataChanged(event);
        		}
        		
        		return isRemoved;
        	}
        	@Override
        	public void clear() {
        		int size  = size();
        		super.clear();
        		if (size > 0) {
        			TableModelModifyEvent event=new TableModelModifyEvent(
	        				this,TableModelModifyEvent.ACTION_TYPE_CANCEL_DELETE, null, size, modifyData.size() > 0);
	        		fireDataChanged(event);
        		}
        	}
        	@Override
        	public boolean addAll(Collection<? extends Integer> c) {
        		boolean isAdded = super.addAll(c);
        		
        		if (isAdded) {
        			TableModelModifyEvent event=new TableModelModifyEvent(
	        				this,TableModelModifyEvent.ACTION_TYPE_DELETE, null, c, true);
	        		fireDataChanged(event);
        		}
        		
        		return isAdded;
        	}
        };
        /**Ensure that user can be notified when modifyData takes a change */
        modifyData = new HashMap<String,Object>()
        {
			private static final long serialVersionUID = 1L;
			@Override
        	public Object put(String key,Object value)
        	{
        		Object oldValue=super.put(key, value);
        		if(oldValue==null)
        		{
        			if(value==null)
        				return null;
        		}else
        		{
        			if(oldValue.equals(value))
        				return oldValue;
        		}
        		TableModelModifyEvent event=new TableModelModifyEvent(
        				this,TableModelModifyEvent.ACTION_TYPE_MODIFY,oldValue,value,true);
        		fireDataChanged(event);
        		return oldValue;
        	}
			@Override
			public void putAll(Map<? extends String, ? extends Object> t)
			{
				int oldSize = size();
				super.putAll(t);
				if (oldSize != size()) {
					TableModelModifyEvent event=new TableModelModifyEvent(
	        				this,TableModelModifyEvent.ACTION_TYPE_MODIFY,null,t,true);
	        		fireDataChanged(event);
				}
			}
        	@Override
        	public Object remove(Object key)
        	{
        		Object value = super.remove(key);
        		if (value != null) {
	        		TableModelModifyEvent event=new TableModelModifyEvent(
	        				this,TableModelModifyEvent.ACTION_TYPE_RESTORE,value, null, size() > 0 || deletedRows.size() > 0);
	        		fireDataChanged(event);
        		}
        		return value;
        	}
        	@Override
        	public void clear()
        	{
        		int oldSize = size();
        		super.clear();
        		if (oldSize != 0) {
	        		TableModelModifyEvent event=new TableModelModifyEvent(
	        				this,TableModelModifyEvent.ACTION_TYPE_RESTROE_ALL, null, null, deletedRows.size() > 0);
	        		fireDataChanged(event);
        		}
        	}
        }
        ;
        tableListener = new MyTableModelListener();
        _iColumn = -1;
        _indexes = new Integer[0];
        _bAscending = true;
        setActualModel(model);
        pcs = new Vector<PropertyChangeListener>();
        comparator=new TableModelComparator();
    }

    /**
     * ��������ı�ģ��
     * 
     * @param newModel
     */
    public void setActualModel(DefaultTableModel newModel) {
        if (_actualModel != null)
            _actualModel.removeTableModelListener(tableListener);
        _actualModel = newModel;
        if (_actualModel != null)
            _actualModel.addTableModelListener(tableListener);
    }
    public void modifyCell(int row,int modelColumn,Object value)
    {
    	modifyData.put(StringUtil.compose(getRealRow(row),modelColumn),value);
    }
    /**
     * 
     * @param key the string generated by method StringUtil.compose(int row,int column).
     * @param value --the new value of cell.
     */
    public void modifyCell(String key,Object value)
    {
    	modifyData.put(key,value);
    }
    public Set<Integer> getShouldDeletedRows() {
    	return deletedRows;
    }
    /**
     * Retrieve a data structure of modified data which is represented by a map key of which is row index ,
     * value of which is cell data structure list.
     */
    public Map<Integer, List<SortModelModifyStruct>> getModifiedDataStructure() {
		Map<Integer, List<SortModelModifyStruct>> modifyStructMap = new HashMap<Integer, List<SortModelModifyStruct>>();
		Iterator<String> it = modifyData.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			int[] cellInfo = StringUtil.resolveString(key);
			if (cellInfo == null)
				continue;

			if (isShouldDelete((cellInfo[0]))) { //ignore the row should be deleted.
				continue;
			}
			List<SortModelModifyStruct> rowStruct = modifyStructMap
					.get(cellInfo[0]);
			if (rowStruct == null) {
				rowStruct = new ArrayList<SortModelModifyStruct>();
				modifyStructMap.put(cellInfo[0], rowStruct);
			}
			rowStruct.add(new SortModelModifyStruct(cellInfo[1], modifyData
					.get(key)));
		}
		return modifyStructMap;
	}
    /**
     * To mark the row as deleted.
     * @param row the row should be deleted.
     */
    public void markDeletedRow(Integer row) {
    	deletedRows.add(row);
    }
    public void markDeletedRow(Integer[] rows) {
    	if (rows == null || rows.length == 0) {
    		return;
    	}
    	for (Integer row : rows) {
    		deletedRows.add(row);
    	}
    }
    /**
     * Cancel the row has been marked as deleted to normal status.
     */
    public void cancelDeletedRow(Integer row) {
    	deletedRows.remove(row);
    }
    public void cancelDeletedRow(Integer[] rows) {
    	if (rows == null || rows.length == 0) {
    		return;
    	}
    	for (Integer row : rows) {
    		deletedRows.remove(row);
    	}
    }
    /**
     * Return true if the specified row should be deleted.
     */
    public boolean isShouldDelete(Integer row) {
    	return deletedRows.contains(row);
    }
    public class SortModelModifyStruct implements Serializable
    {
		private static final long serialVersionUID = 1L;
		
		private int columnIndex;//column index in the model
    	private Object value;
    	public SortModelModifyStruct(int columnIndex,Object value)
    	{
    		this.columnIndex=columnIndex;
    		this.value=value;
    	}
		/**
		 * @return the columnIndex
		 */
		public int getColumnIndex() {
			return this.columnIndex;
		}
		/**
		 * @param columnIndex the columnIndex to set
		 */
		public void setColumnIndex(int columnIndex) {
			this.columnIndex = columnIndex;
		}
		/**
		 * @return the value
		 */
		public Object getValue() {
			return this.value;
		}
		/**
		 * @param value the value to set
		 */
		public void setValue(Object value) {
			this.value = value;
		}
    }
    /**
     * Return the row index in model corresponding to the index in view.
     * @param row the row index in view.
     */
    public int getRealRow(int row) {
        return _indexes[row].intValue();
    }
    /**
     * Get the row index in view corresponding to model row index.
     * @param modelRow the row index in model.
     */
    public int getViewRow(int modelRow)
    {
    	for(int i=0;i<_indexes.length;i++)
    	{
    		if(_indexes[i]==modelRow)
    			return i;
    	}
    	return -1;
    }
    /*
     * ���� Javadoc��
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return _actualModel == null ? 0 : _actualModel.getRowCount();
    }

    /*
     * ���� Javadoc��
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return _actualModel == null ? 0 : _actualModel.getColumnCount();
    }

    /*
     * ���� Javadoc��
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        //System.out.println("row="+row+",col="+col+",value="+o+",index["+row+"]="+_indexes[row].intValue()+"");
        return _actualModel.getValueAt(_indexes[row].intValue(), col);
    }

    /**
     * ���� Javadoc��
     * 
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object value, int row, int col) {
        _actualModel.setValueAt(value, _indexes[row].intValue(), col);
    }

    /**
     * ���ʵ�ʵ��к��л�ȡ������
     * 
     * @param row
     * @param col
     * @return
     */
    public Object getValueInRealModel(int row, int col) {
        return _actualModel.getValueAt(row, col);
    }

    /**
     * Set the value of specified cell, but not fire a notification to all table model listener
     */
    public void setValueInRealModel(Object ob, int row, int col) {
//        _actualModel.setValueAt(ob, row, col);
    	@SuppressWarnings("unchecked")
    	Vector<Object> rowVector = (Vector<Object>)_actualModel.getDataVector().elementAt(row);
        rowVector.setElementAt(ob, col);
    }
    /**
     * 
     * @param row --row index in the view
     * @param column --column index in the model
     * @return
     */
    public Object getDisplayValue(int row,int column)
    {
    	String key=StringUtil.compose(getRealRow(row), column);
    	if(this.modifyData.containsKey(key))
    	{
    		return this.modifyData.get(key);
    	}else
    	{
    		return getValueAt(row,column);
    	}
    }
    /**
     * ��ȡ����
     */
    public String getColumnName(int col) {
        return _actualModel.getColumnName(col);
    }

    public Class<?> getColumnClass(int col) {
        return _actualModel.getColumnClass(col);
    }

    /**
     * Delete specified row.
     * 
     * @param modelRow the row index in model.
     */
    public void deleteRow(int modelRow) {
        _actualModel.removeRow(modelRow);
        fireTableChanged(new TableModelEvent(this, modelRow, modelRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    /**
     * Delete multiple rows specified by user.
     * @param rows the row index in model.
     */
    public void deleteRows(int rows[]) {
    	Set<Integer> rowsSet = new HashSet<Integer>(rows.length);
    	for (Integer i : rows) {
    		rowsSet.add(i);
    	}
    	List<Integer> rowsList = new ArrayList<Integer>(rowsSet.size());
    	rowsList.addAll(rowsSet);
    	Collections.sort(rowsList, intSorterDesc);
        for (int i = 0; i < rowsList.size(); i++) {
            deleteRow(rowsList.get(i));
        }
    }

    private final static Comparator<Integer> intSorterDesc = new Comparator<Integer>() {

		public int compare(Integer o1, Integer o2) {
			return o2 - o1;
		}
    	
    };
    /**
     * ����һ�����
     * 
     * @param values
     */
    public void insertRow(Object values[]) {

        int startRow = _actualModel.getRowCount();
        _actualModel.addRow(values);
        fireTableChanged(new TableModelEvent(this, startRow, startRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    /**
     * ����һ�����
     * 
     * @param values
     */
    public void insertRow(Vector<?> values) {
        int startRow = _actualModel.getRowCount();
        _actualModel.addRow(values);
        fireTableChanged(new TableModelEvent(this, startRow, startRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    /**
     * �������
     * 
     * @param data
     */
    public void insertRows(Vector<?> data) {
        if (data == null)
            return;
        int startRow = _actualModel.getRowCount();
        Iterator<?> it = data.iterator();
        while (it.hasNext()) {
            Vector<?> v = (Vector<?>) it.next();
            _actualModel.addRow(v);
        }
        fireTableChanged(new TableModelEvent(this, startRow, startRow
                + data.size(), TableModelEvent.ALL_COLUMNS,
                TableModelEvent.INSERT));
    }

    public boolean isCellEditable(int row, int col) {
        return _actualModel.isCellEditable(_indexes[row].intValue(), col);
    }

    /**
     * ���ָ�����У���ʵ�ʵı�ģ�ͽ�������
     * 
     * @param column
     * @return
     */
    public boolean sortByColumn(int column) {
        boolean b = true;
        if (column == _iColumn)
            b = !_bAscending;
        sortByColumn(column, b);
        return b;
    }

    /**
     * Sort the column by specified order.
     * @param column The column index in model.
     * @param ascending the mode of ordering. True means sorting by ascending.
     */
    public void sortByColumn(int column, boolean ascending) {
        _iColumn = column;
        _bAscending = ascending;
        comparator.set_iColumn(column);
        comparator.set_iAscending(ascending);
        Arrays.sort(_indexes, comparator);
        fireTableDataChanged();
        fireSortModeChanged(PROPERTYNAME_ORDER, null, null);
    }

    /**
     * ��ͷ��Ϣ����
     * 
     * @param ob
     */
    public void setColumnIdentifiers(Object ob[]) {
        _actualModel.setColumnIdentifiers(ob);
    }

    /**
     * ��ͷ��Ϣ����
     * 
     * @param ob
     */
    public void setColumnIdentifiers(Vector<?> ob) {
        _actualModel.setColumnIdentifiers(ob);
    }

    /**
     * ��ȡ��������
     * 
     * @return
     */
    public Vector<String> getColumns() {
        Vector<String> v = new Vector<String>(this.getColumnCount());
        for (int i = 0; i < this.getColumnCount(); i++) {
            v.add(this.getColumnName(i));
        }
        return v;
    }

    /**
     * ���ȱʡ��ģ�͵���ݸ�ֵ����
     */
    public void setDataVector(Vector<?> data, Vector<?> columnName) {
        _actualModel.setDataVector(data, columnName);
    }

    /**
     * Ϊʵ�ʵ�tablemodel��Ӽ����¼�
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
        _actualModel.addTableModelListener(l);
    }

    /**
     * Ϊʵ�ʵ�tablemodelɾ������¼�
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {
        _actualModel.removeTableModelListener(l);
    }

    /**
     * Ϊ��ǰ��tablemodel��Ӽ����¼�
     */
    public void addSortModelListener(PropertyChangeListener l) {
        pcs.add(l);
    }

    /**
     * Ϊ��ǰ��tablemodelɾ������¼�
     * 
     * @param l
     */
    public void removeSortModelListener(PropertyChangeListener l) {
        pcs.remove(l);
    }

    /**
     * ����������������Ըı���Ϣ
     */
    public void fireSortModeChanged(String name, Object oldOb, Object newOb) {
        PropertyChangeEvent evt = new PropertyChangeEvent(this, name, oldOb,
                newOb);
        for (int i = 0; i < pcs.size(); i++) {
            PropertyChangeListener l = (PropertyChangeListener) pcs.get(i);
            l.propertyChange(evt);
        }
    }

    /**
     * �����Ⱦ״̬
     *  
     */
    public void clearRenderState() {
        clearModify();

        _iColumn = -1;
        _indexes = new Integer[0];
        _bAscending = true;
    }
    public Object removeModify(int modelRow,int modelColumn)
    {
    	return this.modifyData.remove(StringUtil.compose(modelRow,modelColumn));
    }
    public Object removeModify(String key)
    {
    	return this.modifyData.remove(key);
    }
    /**
     * ���޸ĵ���ݶ���գ�׼����һ�εı������޸�
     *  
     */
    public void clearModify() {
        this.modifyData.clear();
    }
    /**
     * ����Ϣ����(��ݵ�ǰ�����֯)
     *
     */
    public void rowInfoReset(int rows) {
        _indexes = new Integer[rows];
        for (int i = 0; i < _indexes.length; i++)
            _indexes[i] = new Integer(i);
    }

    protected class MyTableModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent evt) {
            if (!(evt.getType() == TableModelEvent.UPDATE)) {
                rowInfoReset(getRowCount());
            }

        }

        protected MyTableModelListener() {
        }
    }

    protected class TableModelComparator implements Comparator<Integer> {

        public int compare(Integer i1, Integer i2) {
            Object data1;
            Object data2;
            data1 = modifyData.get(StringUtil.compose(i1.intValue(), _iColumn));
            data2 = modifyData.get(StringUtil.compose(i2.intValue(), _iColumn));
            if (data1 == null)
                data1 = _actualModel.getValueAt(i1.intValue(), _iColumn);
            if (data2 == null)
                data2 = _actualModel.getValueAt(i2.intValue(), _iColumn);
            if (data1 == null && data2 == null)
                return 0;
            if (data1 == null)
                return 1 * _iAscending;
            if (data2 == null)
                return -1 * _iAscending;
            try {
                Comparable<Object> c1 = (Comparable<Object>) data1;

                return c1.compareTo((Comparable<Object>)data2) * _iAscending;
            } catch (ClassCastException ex) {
                return data1.toString().compareTo(data2.toString())
                        * _iAscending;
            }
        }

        private int _iColumn;

        private int _iAscending;

        public TableModelComparator()
        {
            this(-1);
        }
        public TableModelComparator(int iColumn) {
            this(iColumn, true);
        }

        public TableModelComparator(int iColumn, boolean ascending) {
            _iColumn = iColumn;
            if (ascending)
                _iAscending = 1;
            else
                _iAscending = -1;
        }
        public void set_iAscending(boolean ascending) {
            if (ascending)
                _iAscending = 1;
            else
                _iAscending = -1;
        }
        public void set_iColumn(int column) {
            _iColumn = column;
        }
    }
    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param	l		the TableModelListener
     */
    public void addTableModelModifyListener(TableModelModifyListener l) {
    	listenerList.add(TableModelModifyListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param	l		the TableModelListener
     */
    public void removeTableModelModifyListener(TableModelModifyListener l) {
		listenerList.remove(TableModelModifyListener.class, l);
    }
    public void fireDataChanged(TableModelModifyEvent e) {
    	// Guaranteed to return a non-null array
    	Object[] listeners = listenerList.getListenerList();
    	// Process the listeners last to first, notifying
    	// those that are interested in this event
    	for (int i = listeners.length-2; i>=0; i-=2) {
    	    if (listeners[i]==TableModelModifyListener.class) {
    		((TableModelModifyListener)listeners[i+1]).dataChanged(e);
    	    }
    	}
   }
}
