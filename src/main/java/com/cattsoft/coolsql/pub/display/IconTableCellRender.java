/*
 * �������� 2006-8-20
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * @author liu_xlin
 *��Ԫ���а���ͼ���չʾ��Ⱦ��
 */
public class IconTableCellRender extends BaseTableCellRender  {
    /**
     * ��д���Ԫ����Ⱦ����
     */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);
        if(value instanceof TableCellObject)
        {
            TableCellObject tmp=(TableCellObject)value;
            if(tmp.hasIcon())  //�����ͼ�꣬����ǩ����ͼ��
                label.setIcon(tmp.getIcon());
        }
        return label;
    }
}
