/*
 * �������� 2006-12-15
 */
package com.cattsoft.coolsql.view.resultset;

import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.SQLResultSetResults;

/**
 * @author liu_xlin
 *��ͷ������Ҽ���˵�����
 */
public class TableHeaderMenuManage extends BaseMenuManage {

    /**
     * ���汾�β˵��������ڵ�λ��
     */
    private Point point=null;
    /**
     * ��������˵���
     */
    private JMenuItem copyColumnName=null;
    
    /**
     * @param com
     */
    public TableHeaderMenuManage(JTableHeader com) {
        super(com);

    }

    protected void createPopMenu()
    {
        if(popMenu==null)
        {
            popMenu=new BasePopupMenu();
            ActionListener copyNameAction=new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
                    JTableHeader tableHeader=(JTableHeader)getComponent();
                    int col = tableHeader.columnAtPoint(point);
                    String retStr = null;
                    if (col >= 0) {
                        TableColumn tcol = tableHeader.getColumnModel().getColumn(col);
                        int colWidth = tcol.getWidth();
                        TableCellRenderer h = tcol.getHeaderRenderer();
                        if (h == null)
                            h = tableHeader.getDefaultRenderer();
                        Component c = h.getTableCellRendererComponent(tableHeader.getTable(), tcol
                                .getHeaderValue(), false, false, -1, col);
                        Object value = tcol.getHeaderValue();
                        if (value != null) //��ͷ�ж���Ϊ��
                        {

                            if (value instanceof SQLResultSetResults.Column) //������Ϊ�����ж��󣬲�������ʾ��Ϣ
                            {
                                SQLResultSetResults.Column columnInfo = (SQLResultSetResults.Column) value;
                                StringSelection ss = new StringSelection(StringUtil.trim(columnInfo.getName()));
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
                            }
                        }
                    }
                }
                
            };
            copyColumnName = createMenuItem(PublicResource
                    .getString("resultView.tableheader.popmenu.copyname"),null, copyNameAction);
            popMenu.add(copyColumnName);
        }
    }
    /* ���� Javadoc��
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        if(popMenu==null)
            createPopMenu();
        return popMenu;
    }

    /* ���� Javadoc��
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        return itemCheck();
    }

    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
}
