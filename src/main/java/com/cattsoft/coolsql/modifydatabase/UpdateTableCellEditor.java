/*
 * Created on 2007-1-16
 */
package com.cattsoft.coolsql.modifydatabase;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;

import com.cattsoft.coolsql.modifydatabase.model.BaseTableCell;
import com.cattsoft.coolsql.modifydatabase.model.NewValueTableCell;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.sql.model.Column;

/**
 * @author liu_xlin ��������ݱ�ؼ���Ԫ�ر༭��Ⱦ��
 */
public class UpdateTableCellEditor extends DefaultCellEditor {
    /**
     * �༭�����ӳ�䱣�� key:�༭������� value:�༭�������
     */
    private Map editor = null;

    /**
     * ��ǰ�༭�������
     */
    private String currentEditorType;

    /**
     * ��ǰ���ڱ༭�ı��λ��
     */
    private int currentRow; //��

    private int currentColumn; //��

    /**
     * ��ǰ���ڱ༭����ԭֵ
     */
    private Object oldValue;

    public UpdateTableCellEditor() {
        super(new TextEditor());
        editor = new HashMap();
        currentEditorType = null;
        currentRow = -1;
        currentColumn = -1;
        oldValue = null;
    }

    /**
     * ��д���෽��
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        oldValue = value; //���浱ǰ���ڱ༭����ԭֵ

        TableCell cellValue = null;
        if (value != null) {
            cellValue = (TableCell) value;
            ((BaseTableCell)cellValue).setIsNullValue(false);
            int columnIndex = table.getColumnModel().getColumnIndex(
                    UpdateRowTable.UpdateConstant.COLUMN_NAME);
            TableCell columnNameCell = (TableCell) table.getValueAt(row,
                    columnIndex);
            Column sqlField = (Column) columnNameCell.getValue();

            currentEditorType = cellValue.getEditorType();
            JComponent com = (JComponent) editor.get(currentEditorType);
            if (com != null) //����Ѿ������˸ñ༭���ͣ������ø�ǰ�༭�������
            {
                editorComponent = com;
                if (editorComponent instanceof EditorLimiter) {  //�������ñ༭���ĳ�������
                    ((EditorLimiter) editorComponent).setSize((int) sqlField
                            .getSize());
                    ((EditorLimiter) editorComponent).setDigits(sqlField
                            .getNumberOfFractionalDigits());
                }
            } else //���������
            {
                editorComponent = EditorFactory.getEditorComponent(
                        currentEditorType, (int) sqlField.getSize(), sqlField
                                .getNumberOfFractionalDigits());
                editor.put(currentEditorType, editorComponent); //�����´����ı༭���
            }

        }
        //        delegate.setValue(cellValue == null ? "" : cellValue.toString());
        if (editorComponent instanceof DataHolder) {
            ((DataHolder) editorComponent).setValue(cellValue);
        }

        currentRow = row;
        currentColumn = column;
        return editorComponent;
    }

    /**
     * ��дԪ�ر༭����ı༭ֵ�ķ���
     */
    public Object getCellEditorValue() {
        Object tmpValue = oldValue;

        if (editorComponent instanceof DataHolder) {
            DataHolder holder = (DataHolder) editorComponent;

            if (oldValue instanceof NewValueTableCell) {
                ((NewValueTableCell) tmpValue).setValue(holder.getHolderValue()
                        .toString());
            }
        }

        oldValue = null; //��ȥ��ԭֵ������
        return tmpValue;
    }

    /**
     * ����ڴ��еı༭�������
     *  
     */
    public void disposeEditor() {
        editor.clear();
        editor = null;
        editorComponent = null;
    }

    /**
     * @return Returns the currentColumn.
     */
    public int getCurrentColumn() {
        return currentColumn;
    }

    /**
     * @param currentColumn
     *            The currentColumn to set.
     */
    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }

    /**
     * @return Returns the currentRow.
     */
    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * @param currentRow
     *            The currentRow to set.
     */
    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }
}
