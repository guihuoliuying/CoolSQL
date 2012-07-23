/*
 * Created on 2007-1-15
 */
package com.cattsoft.coolsql.modifydatabase;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.cattsoft.coolsql.modifydatabase.model.BaseTableCell;
import com.cattsoft.coolsql.modifydatabase.model.CheckBean;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.model.Column;

/**
 * @author liu_xlin �����������ı�ؼ�
 */
public class UpdateRowTable extends JTable {

    /**
     * ���Ԫ�صı�����ɫ��Ĭ������Ϊǳ��ɫ
     */
    protected static Color cellBackground = new Color(155, 236, 193, 200);

    public UpdateRowTable() {
        super();
        /**
         * ��дȱʡ��ģ�͵ķ�����ʹ��һ��Ϊ��ѡ�����
         */
        DefaultTableModel model = new DefaultTableModel() {
            public Class getColumnClass(int col) {
                if (col == 0)
                    return java.lang.Boolean.class;
                else
                    return Object.class;
            }
        };
        setModel(model);
        JTableHeader header = this.getTableHeader();
        header.setReorderingAllowed(false); //���������
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setAutoCreateColumnsFromModel(false);

        setColumnModel(createColumns());

        //        setRowSelectionAllowed(false);
        //        setColumnSelectionAllowed(true);
        //        setCellSelectionEnabled(true);
        setRowHeight(34);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//ֻ����ѡ��һ��
    }

    /**
     * ������ģ��: 0:ѡ��״̬ 1������� 2��ԭֵ 3����ֵ
     * 
     * @return --��ؼ�����ģ��
     */
    protected DefaultTableColumnModel createColumns() {
        DefaultTableColumnModel columnModel = (DefaultTableColumnModel) this
                .getColumnModel();

        TableColumn col = new TableColumn(0, 55, new IBooleanRenderer(),
                new IBooleanEditor());
        col.setHeaderValue(UpdateConstant.COLUMN_STATE);
        columnModel.addColumn(col);

        UpdateTableCellRender cellRender = new UpdateTableCellRender();

        col = new TableColumn(1, 100, cellRender, null); //�ֶ������
        col.setHeaderValue(UpdateConstant.COLUMN_NAME);
        columnModel.addColumn(col);

        col = new TableColumn(2, 120, cellRender, null); //ԭֵ��
        col.setHeaderValue(UpdateConstant.COLUMN_OLDVALUE);
        columnModel.addColumn(col);

        UpdateTableCellEditor cellEditor = new UpdateTableCellEditor();
        col = new TableColumn(3, 180, cellRender, cellEditor); //��ֵ��
        col.setHeaderValue(UpdateConstant.COLUMN_NEWVALUE);
        columnModel.addColumn(col);

        return columnModel;
    }

    /**
     * ���¶����ؼ�����ʾ��Ϣ
     */
    public String getToolTipText(MouseEvent event) {
        Point p = event.getPoint();
        int column = columnAtPoint(p);
        if (column == 0)
            return null;
        int row = rowAtPoint(p);
        TableCell tableCell = (TableCell) getValueAt(row, 1); //��ȡ��ǰ�����Ӧ����
        Column colInfo = (Column) tableCell.getValue();

        return getToolText(colInfo);
    }

    /**
     * ����ж���,��ȡ���ж���ļ�Ҫ����,�������Ծ�̬html��ʽչʾ
     * 
     * @param col
     *            --��Ҫ���������ж���
     * @return --���ж���ļ�Ҫ����.
     */
    private String getToolText(Column col) {
        return "<html><body><table><tr><th align=right>name:</th><td>"
                + col.getName() + "</td></tr>"
                + "<tr><th align=right>size:</th><td>" + col.getSize()
                + "</td></tr>" + "<tr><th align=right>type:</th><td>"
                + col.getTypeName() + "</td></tr>"
                + "<tr><th align=right>isPrimarykey:</th><td>"
                + (col.isPrimaryKey() ? "yes" : "no") + "</td></tr>"
                + "</table></body></htm>";
    }

    /**
     * ��ȡ��ͷ��ʶ�����������
     * 
     * @return --��ͷ��ʾ
     */
    protected Vector getHeaderData() {
        Vector v = new Vector();
        v.add(UpdateConstant.COLUMN_STATE);
        v.add(UpdateConstant.COLUMN_NAME);
        v.add(UpdateConstant.COLUMN_OLDVALUE);
        v.add(UpdateConstant.COLUMN_NEWVALUE);
        return v;
    }

    /**
     * ����ݽ��и����滻
     * 
     * @param data
     *            --���������
     */
    public void replaceData(Vector data) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setDataVector(data, getHeaderData());
    }

    /**
     * ��д�༭������ֻ�����һ�У�ѡ��״̬���͵����У���ֵ�����б༭
     */
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            CheckBean bean = (CheckBean) getValueAt(row, column);
            return bean.isEnable();
        } else {
            TableCell cell = (TableCell) getValueAt(row, column);

            if (!cell.isEditable() && cell.isNullValue() && column == 3) {
                int result = JOptionPane.showConfirmDialog(this, PublicResource
                        .getSQLString("rowupdate.table.ismodifynull"),
                        "confirm", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    ((BaseTableCell) cell).setEditable(true);
                }
            }
            return cell.isEditable();
        }
    }

    /**
     * ����ָ�����е���ر��ֵ�ñ�����ɫ
     * 
     * @param row
     *            --ָ����
     * @param color
     *            --��Ҫ�趨����ɫ
     */
    protected void setRowBackgroundColor(int row, Color color) {
        BaseTableCell cell = (BaseTableCell) UpdateRowTable.this.getValueAt(
                row, 1);
        cell.setRenderOfBackground(color);
        cell = (BaseTableCell) UpdateRowTable.this.getValueAt(row, 2);
        cell.setRenderOfBackground(color);
        Object ob = UpdateRowTable.this.getValueAt(row, 3);
        cell = (BaseTableCell) UpdateRowTable.this.getValueAt(row, 3);
        cell.setRenderOfBackground(color);
    }

    /**
     * 
     * @author liu_xlin ��ѡ�����Ⱦ�࣬Ӧ�ø��±��ĵ�һ��
     */
    protected class IBooleanRenderer extends JCheckBox implements
            TableCellRenderer {
        public IBooleanRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            setBackground(null); //ʹ��һ���ޱ���ɫ
            if (value != null && value instanceof CheckBean) {
                CheckBean bean = (CheckBean) value;
                setSelected(bean.getSelectValue().booleanValue());
            } else
                setSelected(false);
            return this;
        }
    }

    /**
     * 
     * @author liu_xlin ��ѡ��༭Ԫ������ض���,�����˱༭��������Ե��ж�
     */
    protected class IBooleanEditor extends DefaultCellEditor {
        private CheckBean currentBean = null;

        public IBooleanEditor() {
            super(new JCheckBox());

            JCheckBox checkBox = (JCheckBox) getComponent();
            checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        }

        /** Implements the <code>TableCellEditor</code> interface. */
        public Component getTableCellEditorComponent(JTable table,
                Object value, boolean isSelected, int row, int column) {
            CheckBean bean = (CheckBean) value;
            currentBean = bean;

            Component com = super.getTableCellEditorComponent(table, bean
                    .getSelectValue(), isSelected, row, column);
            GUIUtil.setComponentEnabled(bean.isEnable(), (JComponent) com);
            return com;
        }

        public Object getCellEditorValue() {
            if (currentBean != null)
                currentBean
                        .setSelectValue((Boolean) super.getCellEditorValue());
            return currentBean;
        }
    }

    public interface UpdateConstant {
        public static final String COLUMN_STATE = PublicResource
                .getSQLString("rowupdate.table.column.state"); //ѡ��״̬��

        public static final String COLUMN_NAME = PublicResource
                .getSQLString("rowupdate.table.column.columname"); //����

        public static final String COLUMN_OLDVALUE = PublicResource
                .getSQLString("rowupdate.table.column.oldvalue"); //ԭֵ

        public static final String COLUMN_NEWVALUE = PublicResource
                .getSQLString("rowupdate.table.column.newvalue"); //��ֵ
    }

}
