/*
 * Created on 2007-1-30
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import com.cattsoft.coolsql.pub.component.BasePanel;
import com.cattsoft.coolsql.pub.component.DisplayPanel;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;

/**
 * @author liu_xlin
 *  
 */
public class EditeTableCellRender extends DefaultTableCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6421563801604030241L;
	private Color invalidateColor;  //���Ԫ��У�鲻�Ϸ���ʱ�����и�����ʾ
    
    public EditeTableCellRender()
    {
        invalidateColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_EDITORTABLE_ERRORCELL_COLOR, Color.RED);
        
    }
    /**
     * 
     * Returns the default table cell renderer.
     * 
     * @param table
     *            the <code>JTable</code>
     * @param value
     *            the value to assign to the cell at <code>[row, column]</code>
     * @param isSelected
     *            true if cell is selected
     * @param hasFocus
     *            true if cell has focus
     * @param row
     *            the row of the cell to render
     * @param column
     *            the column of the cell to render
     * @return the default table cell renderer
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            super.setForeground(table.getForeground());
            if(!table.isRowSelected(row))
                super.setBackground(table.getBackground());
            else
                super.setBackground(DisplayPanel.getSelectionColor().brighter());
        }
        setFont(table.getFont());
        
        EditorTable t=(EditorTable)table;
        if(t.isCheckValue()&&value!=null)  //�����ҪУ�鵥Ԫ�����
        {
            CellValidation validate=t.getCellValidate();
            if(validate!=null)
            {
                if(!validate.checkValidation(value,row,column)) //���У�鲻ͨ��õ�Ԫ�����ݸ�����ʾ
                {
                    super.setForeground(invalidateColor);
                }
            }
        }
        
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            
        } else {
            setBorder(noFocusBorder);
        }

        if(value instanceof EditeTableCell)
        {
            EditeTableCell cell=(EditeTableCell)value;
            
            value=cell.isNull()?"NAN":cell.getDisplayLabel();
            if(cell.isNull())
                super.setForeground(getNullColor());
        }
        setValue(value);

        return this;
    }
    protected Color getNullColor()
    {
        return Color.BLUE;
    }
    /**
     * @return Returns the invalidateColor.
     */
    public Color getInvalidateColor() {
        return invalidateColor;
    }
    /**
     * @param invalidateColor The invalidateColor to set.
     */
    public void setInvalidateColor(Color invalidateColor) {
        this.invalidateColor = invalidateColor;
    }
    protected Color getRowSelectColor(JTable table)
    {
        return BasePanel.getThemeColor().brighter();
//        return table.getSelectionBackground().brighter();
    }
}
