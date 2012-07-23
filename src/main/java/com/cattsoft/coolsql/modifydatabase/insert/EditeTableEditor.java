/*
 * Created on 2007-3-6
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

import com.cattsoft.coolsql.pub.component.StringEditor;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.sql.model.Column;

/**
 * @author liu_xlin �༭��ؼ��ı༭���������д��ؼ���ȱʡ�༭�����Ա༭�����������ϵĿ��ƣ����������󳤶ȣ���ʽ��
 */
public class EditeTableEditor extends DefaultCellEditor {

    /**
     * ��ǰ���ڱ༭��ֵ
     */
    private Object currentValue;
    public EditeTableEditor() {
        super(new TextEditor());
        currentValue=null;
    }
    
    /**
     * ���ñ༭����ֵ
     * @param ob
     */
    public void setValue(Object ob) {
        if (ob instanceof EditeTableCell) {
            EditeTableCell cell = (EditeTableCell) ob;
            delegate.setValue(cell.getDisplayLabel());
        } else
            delegate.setValue(ob);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        currentValue=value;
//        control(table,column);
        setValue(value);       
        return editorComponent;
    }
    /**
     *����ж��壬 ���Ʊ༭������󳤶�
     * @param t  --�༭�����Ӧ�ı�ؼ�
     * @param columnIndex  --��ǰ�༭���Ӧ��������
     */
    protected void control(JTable t,int columnIndex)
    {
        if(t instanceof EditorTable)
        {
            EditorTable table=(EditorTable)t;
            Object tmp=table.getColumnModel().getColumn(columnIndex).getHeaderValue();
            if(tmp instanceof TableHeaderCell)
            {
                Object headerValue=((TableHeaderCell)tmp).getHeaderValue();
                if(headerValue instanceof Column)
                {
                    Column column=(Column)headerValue;
                    ((StringEditor)editorComponent).setMaxLength((int)column.getSize());
                }
            }
        }
    }
    /**
     * Forwards the message from the <code>CellEditor</code> to
     * 
     */
    public Object getCellEditorValue() {
        Object ob=super.getCellEditorValue();
        if(currentValue instanceof EditeTableCell)
        {
            EditeTableCell cell=(EditeTableCell)currentValue;
            cell.setValue(ob);
        }else
            currentValue=ob;
        return currentValue;
    }
}
