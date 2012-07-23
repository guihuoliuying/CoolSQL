/*
 * Created on 2007-1-26
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.cattsoft.coolsql.pub.display.ClipboardUtil;

/**
 * @author liu_xlin �Զ���ɱ༭��ģ��
 */
public class EditorTableModel extends AbstractTableModel {

    /**
     * �����
     */
    private List columnList = null;

    //����ݼ��϶���
    private List dataList = null;

    /**
     * ���µ�Ԫ�����ʱ������ݸ�ʽ����ȷ�Խ���У��
     * true:��Ҫ����
     * false:������У�鴦��
     */
    private boolean isValueCheck=true;

    public EditorTableModel()
    {
        this(new Object[0],0);
    }
    public EditorTableModel(List columnNames, int rowCount) {
        setDataList(newList(rowCount), columnNames);
    }
    public EditorTableModel(Object[] columnNames, int rowCount) {
        setDataList(newList(rowCount), convertToList(columnNames));
    }
    public EditorTableModel(List dataList, List columnList) {
        setDataList(dataList, columnList);
    }

    public EditorTableModel(Object[][] dataList, Object[] columnList) {
        setDataList(convertToList(dataList), convertToList(columnList));
    }

    private static List nonNullList(List v) {
        return (v != null) ? v : new ArrayList();
    }

    private List newList(int rows)
    {
        return convertToList(new Object[rows]);
    }
    /**
     * ����ݶ���ת���ɼ��ϣ�List������
     * 
     * @param object
     *            --��Ҫ��ת�����������
     * @return --������ת������List����
     */
    private List convertToList(Object[] object) {
        if (object == null)
            return null;
        List list = Arrays.asList(object);
        return list;
    }

    /**
     * ����ά����ת��ΪList����
     * 
     * @param object
     *            --��Ҫ��ת���Ķ�ά����
     * @return
     */
    private List convertToList(Object[][] object) {
        if (object == null)
            return null;

        List list = new ArrayList();
        for (int i = 0; i < object.length; i++) {
            list.add(convertToList(object[i]));
        }
        return list;
    }

    /**
     * ���±�ṹ����ݣ���������ݺ�����Ϣ
     * 
     * @param dataList
     *            --����ݼ��϶���
     * @param columnList
     *            --����Ϣ���϶���
     */
    public void setDataList(List dataList, List columnList) {
        this.dataList = nonNullList(dataList);
        this.columnList = nonNullList(columnList);
        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    //************************************************
    public void addRow(List rowList) {
        insertRow(getRowCount(), rowList);
    }

    public void addRow(Object[] rowList) {
        insertRow(getRowCount(), convertToList(rowList));
    }
    public void addRows(Object[][] rowList) {
        insertRows(getRowCount(), convertToList(rowList));
    }
    public void addRows(List rowList) {
        insertRows(getRowCount(), rowList);
    }
    /**
     * Removes the row at <code>row</code> from the model. Notification of the
     * row being removed will be sent to all the listeners.
     * 
     * @param row
     *            the row index of the row to be removed
     * @exception ArrayIndexOutOfBoundsException
     *                if the row was invalid
     */
    public void removeRow(int row) {
        dataList.remove(row);
        fireTableRowsDeleted(row, row);
    }
    /**
     * �����е�����
     * 
     * @param columnCount
     *            --�µ�����
     */
    public void setColumnCount(int columnCount) {
        int old = getColumnCount();
        if (old == columnCount)
            return;

        if (old >= columnCount) {
            columnList = columnList.subList(0, columnCount);
        } else
            columnList.addAll(Arrays.asList(new Object[columnCount - old]));

        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    /**
     * ���ģ�����һ��
     * 
     * @param columnName
     *            --�������ж���
     * @param columnData
     *            --���������Ӧ�����
     */
    public void addColumn(Object columnName, List columnData) {
        columnList.add(columnName);
        if (columnData != null) {
            int columnSize = columnData.size();
            if (columnSize > getRowCount()) { //��������ݵ���������峤�ȣ�������β���ɾ��
                columnData = columnData.subList(0, getRowCount());
            }
            justifyRows(0, getRowCount());
            int newColumn = getColumnCount() - 1;
            for (int i = 0; i < columnData.size(); i++) {
                List row = (List) dataList.get(i);
                row.set(newColumn, columnData.get(i));
            }
        } else {
            justifyRows(0, getRowCount());
        }

        fireTableStructureChanged();
    }

    public void addColumn(Object columnName, Object[] columnData) {
        addColumn(columnName, convertToList(columnData));
    }
    public void addColumn(Object columnName) {
        addColumn(columnName, (List)null);
    }
    /**
     * ��ȡָ���������Ӧ������
     */
    public String getColumnName(int column) {
        Object id = null;
        // This test is to cover the case when
        // getColumnCount has been subclassed by mistake ...
        if (column < columnList.size()) {
            id = columnList.get(column);
        }
        return (id == null) ? super.getColumnName(column) : id.toString();
    }

    public boolean isCellEditable(int row, int column) {
        return true;
    }

    /**
     * �����м���
     * @param columnList  --�µ�����Ϣ����
     */
    public void setColumnIdentifiers(List columnList) {
        setDataList(dataList, columnList);
    }
    public void setColumnIdentifiers(Object[] newIdentifiers) {
        setColumnIdentifiers(convertToList(newIdentifiers));
    }
    //*************************************************

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return dataList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        List rowList = (List) dataList.get(rowIndex);
        return rowList.get(columnIndex);
    }

    /**
     * ���ò���ָ������е�Ԫ�ض���ֵ aValue --��񱻸��µĶ���ֵ row --����Ӧ��������ֵ column --����Ӧ��������ֵ
     */
    public void setValueAt(Object aValue, int row, int column) {
        List rowList = (List) dataList.get(row);
        rowList.set(column, aValue);
        fireTableCellUpdated(row, column);
    }

    /**
     * ������м����ݵ������ԣ��������ݵ������붨�������ȣ������ʵ��ĵ���
     * 
     * @param from
     *            --��ʼ��������ֵ
     * @param to
     *            --������������ֵ
     */
    private void justifyRows(int from, int to) {

        for (int i = from; i < to; i++) {
            List rowList = (List) dataList.get(i);
            if (rowList == null) {
                rowList = Arrays.asList(new Object[getColumnCount()]);
                dataList.set(i, rowList);
            }

            int differCount = rowList.size() - getColumnCount();
            if (differCount > 0) //�������ݵ�Ԫ�ظ�����˶������ֵ
            {
                rowList = rowList.subList(0, getColumnCount());
                dataList.set(i, rowList);
            } else if (differCount < 0) {
                differCount = -differCount;
                rowList.addAll(Arrays.asList(new Object[differCount]));
            }

        }
    }

    /**
     * Inserts a row at <code>row</code> in the model. The new row will
     * contain <code>null</code> values unless <code>rowData</code> is
     * specified. Notification of the row being added will be generated.
     * 
     * @param row
     *            the row index of the row to be inserted
     * @param rowData
     *            optional data of the row being added
     * @exception ArrayIndexOutOfBoundsException
     *                if the row was invalid
     */
    public void insertRow(int row, List rowData) {
        dataList.add(row, rowData);
        justifyRows(row, row + 1);
        fireTableRowsInserted(row, row);
    }
    public void insertRow(int row, Object[] rowData) {
        insertRow(row,convertToList(rowData));
    }
    public void insertRows(int row, Object[][] rowData) {
        insertRows(row,convertToList(rowData));
    }
    public void insertRows(int row, List rowData) {
        dataList.addAll(row, rowData);
        justifyRows(row, row + rowData.size());
        fireTableRowsInserted(row, row+rowData.size()-1);
    }
    /**
     * ����ؼ����������������Ϊ������rowCount���� ���ǰ�д��ڸ��У���ɾ��β�����������ݣ�
     * ���ǰ��С�ڸ��У���β�������Ӧ���У�����ӵ������Ϊnull��
     * 
     * @param rowCount
     *            --��ģ���µ�����
     */
    public void setRowCount(int rowCount) {
        int old = getRowCount();
        if (old == rowCount) {
            return;
        }

        if (rowCount <= old) {
            dataList = dataList.subList(0, rowCount);
            fireTableRowsDeleted(rowCount, old - 1);
        } else {
            dataList.addAll(Arrays.asList(new Object[rowCount - old]));

            justifyRows(old, rowCount);
            fireTableRowsInserted(old, rowCount - 1);
        }
    }
    /**
     * ���������ָ������Ϣճ���ڱ�ؼ��С�
     * @param startRow  --ճ�����ʼ��
     * @param startColumn  --ճ�����ʼ��
     */
    public void batchPaste(int startRow,int startColumn,JTable table)
    {        
        if(startRow<0||startRow>getRowCount()||startColumn<0||startColumn>getColumnCount())
            return;
        
        batchPaste(ClipboardUtil.getStringContent(),startRow,startColumn,table);
        
    }
    /**
     * ���޸ĵ�Ԫ��ֵʱ���Ƿ������ֵ��У��
     * @return true:У��  false:��У��
     */
    public boolean isCheckValue()
    {
        return isValueCheck;
    }
    public void setCheckValue(boolean isValueCheck)
    {
        this.isValueCheck=isValueCheck;
    }
    /**
     * ��ָ������Ϣճ���ڱ�ؼ��С�
     * 1���в�������
     * 2���п��Ը��ճ��������������Ӧ������
     * 3�������ݣ�content���Է�š�\t\n\r\f�����зָ��ȡԪ��ֵ������Ԫ��ֵ���µ���Ӧ�ı��Ԫ����
     * @param content  --��ճ�������
     * @param startRow   --ճ�����ʼ��
     * @param startColumn  --ճ�����ʼ��
     */
    protected void batchPaste(String content,int startRow,int startColumn,JTable table)
    {
	    if(content!=null)
	    {
	        
	        StringTokenizer stn=new StringTokenizer(content,"\t\n\r\f",true);
	        boolean flag=false;  //�Ƿ�ոջ�����
	        int rowPointer=startRow;
	        int columnPointer=startColumn;
	        while(stn.hasMoreTokens())
	        {
	            String tmp=stn.nextToken();
	            char firstChar=tmp.charAt(0);
	            switch(firstChar)
	            {
	              case '\n': 
	                  rowPointer++;
	                  flag=true;
	                  columnPointer=startColumn;
	                  break;
	              case '\t':
	                  columnPointer++;
	                  break;
	              case '\r':
	                  break;
	              case '\f':
	                  break;
	              default:
	                  if(columnPointer>=getColumnCount())  //�����ָ�볬��������ж���
	                  {
	                      continue;
	                  }else
	                  {
	                      if(flag)
	                      {
	                          flag=false;
	    	                  if(rowPointer>getRowCount()-1)
	    	                      dataList.add(EditorTableModel.getEmptyCell(getColumnCount())); //��չһ��
	                      }
	                      setValueNoNotify(tmp,rowPointer,table.convertColumnIndexToView(columnPointer));
	                  }
	            }
	        }
	        fireTableStructureChanged();
	    }
    }
    /**
     * ����ָ������Ԫ�ض���ֵ������֪ͨ�����?
     * @param aValue  --�¶���ֵ
     * @param row  --ָ��������
     * @param column  --ָ��������
     */
    void setValueNoNotify(Object aValue, int row, int column) {
        List rowList = (List) dataList.get(row);
        int size=rowList.size();
        EditeTableCell cell=(EditeTableCell)rowList.get(column);
        cell.setValue(aValue);
        rowList.set(column, cell);
    }
	/**
	 * ��ȡ�������
	 * @param columnSize  --����
	 * @return  --�������
	 */
	protected static List getEmptyCell(int columnSize)
	{
	    List list=new ArrayList();
	    for(int i=0;i<columnSize;i++)
	        list.add(new InsertTableCell("",false));
	    
	    return list;
	}
	/**
	 * ��ȡ���п����
	 * @param rowSize  --����
	 * @param columnSize  --����
	 * @return  --���п���ݶ���
	 */
	protected static List getEmptyCell(int rowSize,int columnSize)
	{
	    List list=new ArrayList();
	    
	    for(int i=0;i<rowSize;i++)
	        list.add(EditorTableModel.getEmptyCell(columnSize));
	    
	    return list;
	}
}
