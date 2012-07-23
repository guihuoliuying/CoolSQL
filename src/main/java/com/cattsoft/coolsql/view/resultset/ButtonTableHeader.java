/*
 * �������� 2006-6-25
 *
 */
package com.cattsoft.coolsql.view.resultset;

/**
 * @author liu_xlin ��ͷ����Ⱦ�����ԶԱ���ݽ�������
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.cattsoft.coolsql.pub.component.BasicTableHeader;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.SQLResultSetResults;

public class ButtonTableHeader extends BasicTableHeader {
	private static final long serialVersionUID = 1L;

	private Color[] headerColor;

    public ButtonTableHeader(JTable table) {
        super();
        _dataListener = new TableDataListener();
        _currentlySortedColumnIdx = -1;
        _pressed = false;
        _dragged = false;
        _pressedColumnIdx = -1;
        setDefaultRenderer(new ButtonTableRenderer(getFont()));
        HeaderListener hl = new HeaderListener();
        addMouseListener(hl);
        addMouseMotionListener(hl);

        s_descIcon = PublicResource.getIcon("table.header.sort.desicon");
        s_ascIcon = PublicResource.getIcon("table.header.sort.ascicon");

        headerColor=new Color[getColumnModel().getColumnCount()];
        table.addPropertyChangeListener(new PropertyChangeListener()
        {
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("columnModel"))
				{
					headerColor=new Color[getColumnModel().getColumnCount()];
				}
			}
        	
        }
        );
        getColumnModel().addColumnModelListener(new TableColumnModelListener()
        {

			public void columnAdded(TableColumnModelEvent e) {
				headerColor=new Color[getColumnModel().getColumnCount()];
			}

			public void columnMarginChanged(ChangeEvent e) {
			}

			public void columnMoved(TableColumnModelEvent e) {
			}

			public void columnRemoved(TableColumnModelEvent e) {
				headerColor=new Color[getColumnModel().getColumnCount()];				
			}

			public void columnSelectionChanged(ListSelectionEvent e) {
			}
        	
        }
        );
    }
    public void setHeaderColor(int index,Color color)
    {
    	int realIndex=getTable().convertColumnIndexToModel(index);
    	headerColor[realIndex]=color;
    }
	protected class ButtonTableRenderer implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (value == null)
                value = " ";
            
            int modelColumn=table.convertColumnIndexToModel(column);
            if (_pressedColumnIdx == column && _pressed) {
                _buttonLowered.setText(value.toString());
                if (column == _currentlySortedColumnIdx
                        && _currentSortedColumnIcon != null)
                    _buttonLowered.setIcon(_currentSortedColumnIcon);
                else
                    _buttonLowered.setIcon(null);
                
                if(headerColor[modelColumn]!=null)
                	_buttonLowered.setForeground(headerColor[modelColumn]);
                else
                	_buttonLowered.setForeground(ButtonTableHeader.this.getForeground());
                return _buttonLowered;
            }
            _buttonRaised.setText(value.toString());
            if (_currentSortedColumnIcon != null
                    && column == _currentlySortedColumnIdx)
                _buttonRaised.setIcon(_currentSortedColumnIcon);
            else
                _buttonRaised.setIcon(null);
            
            if(headerColor[modelColumn]!=null)
            	_buttonRaised.setForeground(headerColor[modelColumn]);
            else
            	_buttonRaised.setForeground(ButtonTableHeader.this.getForeground());
            return _buttonRaised;
        }

        protected RenderButton _buttonRaised;

        protected RenderButton _buttonLowered;

        ButtonTableRenderer(Font font) {
            
            _buttonRaised = new RenderButton();
            _buttonRaised.setMargin(new Insets(0, 0, 0, 0));
            _buttonRaised.setFont(font);

            _buttonLowered = new RenderButton();
            _buttonLowered.setMargin(new Insets(0, 0, 0, 0));
            _buttonLowered.setFont(font);
//                        _buttonLowered.getModel().setArmed(true);
//            _buttonLowered.getModel().setPressed(true);
            _buttonLowered.setMinimumSize(new Dimension(50, 25));
            _buttonRaised.setMinimumSize(new Dimension(50, 25));
            
//            fm=_buttonRaised.getFontMetrics(_buttonRaised.getFont());
        }
    }

    class HeaderListener extends MouseAdapter implements MouseMotionListener {

        public void mousePressed(MouseEvent e) {
            if(!isSortable()||!SwingUtilities.isLeftMouseButton(e))  //�������������߲������ĵ����ֱ�ӷ���
                return ;
            _pressed = true;
            _pressedColumnIdx = columnAtPoint(e.getPoint());
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            if(!isSortable())  //�����������ֱ�ӷ���
                return ;
            _pressed = false;
            if (!_dragged&&SwingUtilities.isLeftMouseButton(e)) {
                _currentSortedColumnIcon = null;
                int column = getTable().convertColumnIndexToModel(
                        _pressedColumnIdx);
                TableModel tm = getTable().getModel();
                if (column > -1 && column < getTable().getColumnCount()
                        && (tm instanceof SortableTableModel)) {
                    ((SortableTableModel) tm).sortByColumn(column);
                    if (((SortableTableModel) tm)._bAscending)
                        _currentSortedColumnIcon = ButtonTableHeader.s_ascIcon;
                    else
                        _currentSortedColumnIcon = ButtonTableHeader.s_descIcon;
                    _currentlySortedColumnIdx = _pressedColumnIdx;
                }
                repaint();
            }
            _dragged = false;
        }

        public void mouseDragged(MouseEvent e) {
            _dragged = true;
            if (_pressed) {
                _currentSortedColumnIcon = null;
                _currentlySortedColumnIdx = -1;
                _pressed = false;
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
            _dragged = false;
        }

        HeaderListener() {
        }
    }

    /**
     * 
     * @author liu_xlin ��������ؼ���ģ�ͷ���仯ʱ������ѡ����е�����ͼ�����
     */
    private final class TableDataListener implements TableModelListener {

        public void tableChanged(TableModelEvent evt) {
            _currentSortedColumnIcon = null;
            _currentlySortedColumnIdx = -1;
        }

        private TableDataListener() {
        }

    }
    public void clearHeaderColor()
    {
    	headerColor=new Color[getColumnModel().getColumnCount()];
    	repaint();
    }
    /**
     * ���ñ�ͷ�������״̬
     *
     */
    public void clearState()
    {
        _currentlySortedColumnIdx = -1;
        _pressed = false;
        _dragged = false;
        _pressedColumnIdx = -1;
        headerColor=new Color[getColumnModel().getColumnCount()];
    }
    public void setTable(JTable table) {

        JTable oldTable = getTable();
        if (oldTable != null) {
            Object obj = oldTable.getModel();
            if (obj instanceof SortableTableModel) {
                SortableTableModel model = (SortableTableModel) obj;
                model.removeTableModelListener(_dataListener);
            }
        }
        super.setTable(table);
        if (table != null) {

            Object obj = table.getModel();
            if (obj instanceof SortableTableModel) {
                SortableTableModel model = (SortableTableModel) obj;
                model.addTableModelListener(_dataListener);
            }
        }
        _currentSortedColumnIcon = null;
        _currentlySortedColumnIdx = -1;
    }

    public String getToolTipText(MouseEvent e) {
        int col = columnAtPoint(e.getPoint());
        String retStr = null;
        if (col >= 0) {
            TableColumn tcol = getColumnModel().getColumn(col);
            Object value = tcol.getHeaderValue();
            if (value != null) //��ͷ�ж���Ϊ��
            {

                if (value instanceof SQLResultSetResults.Column) //������Ϊ�����ж��󣬲�������ʾ��Ϣ
                {
                    SQLResultSetResults.Column columnInfo = (SQLResultSetResults.Column) value;
                    retStr = getHtmlText(columnInfo.getName(), columnInfo
                            .getType(), columnInfo.getSize(),columnInfo.isAutoIncrement());
                }
            }
        }
        return retStr;
    }

    /**
     * �����ʾ��Ϣ����html����չʾ
     * 
     * @param type
     * @param size
     * @return
     */
    protected String getHtmlText(String name, String type, int size,boolean isAutoIncrement) {
        String toolTip = "<html><body><table border=0>"
                + "<tr><td>name:</td><td>" + name + "</td></tr>"
                + "<tr><td>type:</td><td>" + type + "</td></tr>"
                + "<tr><td>size:</td><td>" + size + "</td></tr>"
                + "<tr><td>isAutoIncrement:</td><td>" + isAutoIncrement + "</td></tr>"
                + "</table></body></html>";
        return toolTip;
    }

    /**
     * �����Ƿ���������
     * @param isSortable
     */
    public void setSortable(boolean isSortable)
    {
        if(!isSortable) //��������ȥ���ͷ��ť��ͼ��
        {
//            ButtonTableRenderer render=(ButtonTableRenderer)this.getDefaultRenderer();
            _currentSortedColumnIcon=null;
            this.repaint();
        }
        this.isSortable=isSortable;
        
    }
    public boolean isSortable()
    {
        return isSortable;
    }
    /**
     * ����ͼ��
     */
    private static Icon s_ascIcon;

    /**
     * ����ͼ��
     */
    private static Icon s_descIcon;

    /**
     * ��ؼ�ģ�͸ı����
     */
    private TableDataListener _dataListener;

    /**
     * �Ƿ��±�־
     */
    private boolean _pressed;

    /**
     * ��ק��־
     */
    private boolean _dragged;

    /**
     * ��ǰ��ѡ�е��е��������������
     */
    private int _pressedColumnIdx;

    /**
     * ��ǰ��������չʾ��ͼ�꣨����ͼ��ͽ���ͼ�꣩
     */
    private Icon _currentSortedColumnIcon;

    /**
     * ��ǰ�������е�����
     */
    private int _currentlySortedColumnIdx;
    
    /**
     * ��ǰ��ͷ�Ƿ���������
     */
    private boolean isSortable;

}
