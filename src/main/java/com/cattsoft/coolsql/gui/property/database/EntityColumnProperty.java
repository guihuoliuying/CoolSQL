/*
 * �������� 2006-9-14
 */
package com.cattsoft.coolsql.gui.property.database;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.gui.property.PropertyPane;
import com.cattsoft.coolsql.pub.display.CommonDataTable;
import com.cattsoft.coolsql.pub.display.TableCellObject;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *���������
 */
public class EntityColumnProperty extends PropertyPane {
	private static final long serialVersionUID = 1L;
	JPanel content;
    public EntityColumnProperty()
    {
        super();
    }
    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyPane#initContent()
     */
    public JPanel initContent() {
        content=new JPanel();
        
        return content;
    }

    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#set()
     */
    public boolean set() {
        return true;
    }

    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#quit()
     */
    public void cancel() {

    }

    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if(ob==null)
            return ;
        
        Identifier node=(Identifier)ob;
        Entity entity=(Entity)node.getDataObject();
        Bookmark bookmark=node.getBookmark();
        
        if(bookmark.isConnected())  //����״̬����ʼ�����������塣
        {
            content.removeAll();
            if(entity.getType().equals(SqlUtil.SEQUENCE))
                return;
            content.setLayout(new BorderLayout());
            try {
               Column[] cols=entity.getColumns(); //��ȡ�����������Ϣ
                
                //��ʼ����ͷ��Ϣ
                String[] header=new String[8];
                for(int i=0;i<header.length;i++)
                {
                    header[i]=PublicResource.getSQLString("sql.propertyset.entity.column"+i);
                }
                
                TableCellObject[][] data=new TableCellObject[cols.length][header.length];
                for(int i=0;i<cols.length;i++)
                {
                     int type=cols[i].isPrimaryKey()?BookMarkPubInfo.NODE_KEYCOLUMN:BookMarkPubInfo.NODE_COLUMN;
                     
                     data[i][0]=new TableCellObject(cols[i].getName(),BookMarkPubInfo.getIconList()[type]);
                     data[i][1]=new TableCellObject(cols[i].getTypeName());
                     data[i][2]=new TableCellObject(String.valueOf(cols[i].getSize()));
                     data[i][3]=new TableCellObject(String.valueOf(cols[i].getNumberOfFractionalDigits()));
                     data[i][4]=new TableCellObject(cols[i].isPrimaryKey()?"yes":"no");
                     data[i][5]=new TableCellObject(cols[i].isNullable()?"yes":"no");
                     data[i][6]=new TableCellObject(cols[i].getDefaultValue());
                     data[i][7]=new TableCellObject(cols[i].getRemarks());
                }
                
                int[] render={0};
                CommonDataTable table=new CommonDataTable(data,header,render)
                {
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int row, int column)
                    {
                        return false;
                    }
                }
                ;
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.adjustPerfectWidth();
                content.add(new TableScrollPane(table),BorderLayout.CENTER);
                
            } catch (UnifyException e) {
                LogProxy.errorReport(e);
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(e);
            }
        }else  //���δ������ݿ⣬ֻ��ʾ��ʾ��Ϣ
        {
            content.removeAll();
            content.setLayout(new FlowLayout());
            content.add(new JLabel(PublicResource.getSQLString("sql.propertyset.notconnect")));
        }
    }

    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#apply()
     */
    public void apply() {

    }
    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
     */
    public boolean isNeedApply() {
        return false;
    }

}
