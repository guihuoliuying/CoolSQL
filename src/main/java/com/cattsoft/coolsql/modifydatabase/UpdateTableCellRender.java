/*
 * Created on 2007-1-15
 */
package com.cattsoft.coolsql.modifydatabase;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author liu_xlin �����ݿ�sql�ı�ؼ��ı����Ⱦ��
 */
public class UpdateTableCellRender extends DefaultTableCellRenderer {
    private static ILineBorder leftLineBorder = null;

    private static ILineBorder doubleLineBorder = null;

    private static ILineBorder rightLineBorder = null;

    public UpdateTableCellRender() {
        super();
    }

    private ILineBorder getLeftLineBorder(JTable table) {
        if (leftLineBorder == null)
            leftLineBorder = new ILineBorder(table.getBackground(),
                    ILineBorder.LeftLineBorder);
        return leftLineBorder;
    }

    private ILineBorder getDoubleLineBorder(JTable table) {
        if (doubleLineBorder == null)
            doubleLineBorder = new ILineBorder(table.getBackground(),
                    ILineBorder.DoubleLineBorder);
        return leftLineBorder;
    }

    private ILineBorder getRightLineBorder(JTable table) {
        if (rightLineBorder == null)
            rightLineBorder = new ILineBorder(table.getBackground(),
                    ILineBorder.RightLineBorder);
        return leftLineBorder;
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

        TableCell cellValue = (TableCell) value;

        super.setBackground(cellValue.getRenderOfBackground());

        setFont(table.getFont());
        if (column == 3 && isSelected) {

            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else
            setBorder(null);
        //        }

        setValue(cellValue.isNullValue()?"NAN":cellValue.getStyleString());

        return this;
    }

    /**
     * 
     * @author liu_xlin �Զ���߿���,����������δ��ڵı߿�,���ڸ�������ݱ�ؼ���ѡ����ʾ
     */
    protected class ILineBorder extends LineBorder { //___
        public final static int LeftLineBorder = -1; // ���ϡ��»��ϱ߿�

        public final static int DoubleLineBorder = 0; //�ϡ��±߿�

        public final static int RightLineBorder = 1; //�ϡ��¡��ұ߿�

        /**
         * �߿�����
         */
        private int lineType;

        public ILineBorder(Color color, int lineType) {
            super(color);
            this.lineType = lineType;
        }

        /**
         * @param color
         */
        public ILineBorder(Color color, int thickness, int lineType) {
            super(color, thickness < 2 ? 2 : thickness);
            this.lineType = lineType;
        }

        public void paintBorder(Component c, Graphics g, int x, int y,
                int width, int height) {
            Color oldColor = g.getColor();
            int i;

            g.setColor(lineColor);
            for (i = 0; i < thickness; i++) {

                g.drawLine(x + i, y + i, x + width - i - 2, y + i);//   ---(top)
                g.drawLine(x + width - i - 1, y + height - i - 1, x + i + 1, y
                        + height - i - 1); //   --(bottom)

                if (lineType == RightLineBorder)
                    g.drawLine(x + width - i - 1, y + i, x + width - i - 1, y
                            + height - i - 2); //      |(right)

                if (lineType == LeftLineBorder)
                    g.drawLine(x + i, y + height - i - 1, x + i, y + i + 1); //|(left)
                //    	    if(!roundedCorners)
                //                    g.drawRect(x+i, y+i, width-i-i-1, height-i-i-1);
                //    	    else
                //                    g.drawRoundRect(x+i, y+i, width-i-i-1, height-i-i-1,
                // thickness, thickness);
            }
            g.setColor(oldColor);
        }
    }
}
