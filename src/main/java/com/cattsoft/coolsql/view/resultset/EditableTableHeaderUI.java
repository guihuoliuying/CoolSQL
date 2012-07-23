/*
 * �������� 2006-11-6
 */
package com.cattsoft.coolsql.view.resultset;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableColumn;

import com.cattsoft.coolsql.pub.display.BaseTable;

/**
 * @author liu_xlin ��ı�ͷUI����Ҫ�������Ʊ��е��ƶ�
 */
public class EditableTableHeaderUI extends BasicTableHeaderUI {

    public EditableTableHeaderUI() {
        super();
    }

    protected MouseInputListener createMouseInputListener() {
        return new EditableMouseInputHandler();
    }

    public class EditableMouseInputHandler extends MouseInputHandler {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point p = e.getPoint();

                if(!SwingUtilities.isLeftMouseButton(e))//����ǵ�����
                    return;
                // Firstly find which header cell was hit
                int index = header.columnAtPoint(p);
                TableColumn resizingColumn = getResizingColumn(p, index);
                if (resizingColumn != null) { //�����괦�ڿɵ����ȵ�λ�ã�����˫�������п�ĵ���
                    BaseTable table = (BaseTable) header.getTable();
                    try {
                        table.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        table.columnsToFitWidth(resizingColumn.getModelIndex());
                    } finally {
                        table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }

        /**
         * ��ݸ�����㣬��ָ�����з�Χ�ڣ���ȡ����ߵ��ж���
         * 
         * @param p
         *            --�������
         * @param column
         *            --������
         * @return --�������p�ڿɵ����п�ȵ�λ�ã���������е��ж��󣬷��򷵻�null
         */
        private TableColumn getResizingColumn(Point p, int column) {
            if (column == -1) {
                return null;
            }
            Rectangle r = header.getHeaderRect(column);
            r.grow(-3, 0);
            if (r.contains(p)) {
                return null;
            }
            int midPoint = r.x + r.width / 2;
            int columnIndex;
            if (header.getComponentOrientation().isLeftToRight()) {
                columnIndex = (p.x < midPoint) ? column - 1 : column;
            } else {
                columnIndex = (p.x < midPoint) ? column : column - 1;
            }
            if (columnIndex == -1) {
                return null;
            }
            return header.getColumnModel().getColumn(columnIndex);
        }
    }
}
