/*
 * Created on 2007-3-9
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import com.cattsoft.coolsql.pub.component.DisplayPanel;

/**
 * @author liu_xlin
 *BaseTable��ȱʡ��Ԫ����Ⱦ��
 */
public class BaseTableCellRender extends DefaultTableCellRenderer {

    public BaseTableCellRender()
    {
        super();
    }
    /**
     * ��д��ȡ���Ԫ��Ⱦ�ķ���
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
    	if (isSelected) {
    		   super.setForeground(table.getSelectionForeground());
    		   super.setBackground(table.getSelectionBackground());
    		}
    		else {
    		    super.setForeground(table.getForeground());
    		    if(!table.isRowSelected(row))  //���Ԫ�������б�ѡ�У�ʹ������ѡ����ɫ��Ⱦ���еĵ�Ԫ��
    		        super.setBackground(table.getBackground());
    		    else
    		        super.setBackground(DisplayPanel.getSelectionColor().brighter());
    		}
    		
    		setFont(table.getFont());

    		if (hasFocus) {
    		    setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
//    		    if (table.isCellEditable(row, column)) {
//    		        super.setForeground( UIManager.getColor("Table.focusCellForeground") );
//    		        super.setBackground( UIManager.getColor("Table.focusCellBackground") );
//    		    }
    		} else {
    		    setBorder(noFocusBorder);
    		}
    		BaseTable bt=(BaseTable)table;
    		if(bt.isDisplayRowBorder())
    		{
    			if(row==bt.getMouseOverRow())
    			{
    				if(column==0)
    					setBorder(TableCellHighlightBorder.createLeftCellBorder());
    				else if(column==table.getColumnCount()-1)
    					setBorder(TableCellHighlightBorder.createRightCellBorder());
    				else
    					setBorder(TableCellHighlightBorder.createCenterCellBorder());
    			}
    		}
    	        setValue(value); 

    		return this;
        
    }
}
