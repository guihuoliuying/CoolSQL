/*
 * Created date: 2006-9-16
 */
package com.cattsoft.coolsql.search;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.action.common.InsertToEntityCommand;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.display.CommonDataTable;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.sql.commonoperator.EntityPropertyOperator;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.model.TableImpl;
import com.cattsoft.coolsql.sql.model.ViewImpl;
import com.cattsoft.coolsql.system.menubuild.IconResource;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.TableNode;
import com.cattsoft.coolsql.view.bookmarkview.model.ViewNode;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin �������ͼ�������չʾ����
 */
public class SearchOfEntityFrame extends SearchResultFrame {

	private static final long serialVersionUID = 1L;
	
    public SearchOfEntityFrame(JFrame con, Bookmark bookmark) {
        super(con, bookmark);
        this.setTitle(" result of entity querying!");
        super.setPrompt(PublicResource
                .getSQLString("searchinfo.query.result.bookmark")
                + bookmark.getAliasName());
    }

    public SearchOfEntityFrame(JDialog con, Bookmark bookmark) {
        super(con, bookmark);
        this.setTitle(" result of entity querying!");
        super.setPrompt(PublicResource
                .getSQLString("searchinfo.query.result.bookmark")
                + bookmark.getAliasName());
        Action addDataAction=new AbstractAction()
        {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				CommonDataTable table=getTable();
				int row = table.getSelectedRow();
		        String name = table.getValueAt(row, 0).toString(); //��ȡʵ����
		        String catalog=SqlUtil.validateSqlParam((String)table.getValueAt(row, 1));//��ȡ����
		        String schema = SqlUtil.validateSqlParam((String) table.getValueAt(row, 2)); //��ȡģʽ��
		        String type = (String)table.getValueAt(row, 3); //��ȡģʽ��
		        Entity entity=null;
		        if(type==SqlUtil.VIEW)
		        {
		            entity=new ViewImpl(getBookmark(),catalog,schema,name, "", false);
		        }else 
		        {
		            entity=new TableImpl(getBookmark(),catalog,schema ,name, "", false); //������ģ��
		        }
		        ActionCommand command = new InsertToEntityCommand(entity);
				try {
					command.exectue();
				} catch (Exception e1) {
					LogProxy.errorReport("Initing frame used to add data errors:"+e1.getMessage(), e1);
				}
			}
        	
        };
        getTable().getPopMenuManage().addMenuItem(PublicResource
                .getString("bookmarkView.popup.adddata"), addDataAction , IconResource
                .getIcon("system.icon.addrows"), false);
        
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.view.bookmarkview.SearchResultFrame#initContent()
     */
    protected CommonDataTable initContent() {

        Vector<String> header = new Vector<String>();
        for (int i = 0; i < 4; i++) {
            header.add(PublicResource
                    .getSQLString("searchinfo.query.entityresult.table" + i));
        }

        CommonDataTable table = new CommonDataTable(null, header,new int[]{0});        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); //�Զ�������
        table.setEnableToolTip(false);//��������ʾ
        return table;
    }

    /**
     * ���һ�����
     * 
     * @param row
     */
    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) this.getTable()
                .getModel();
        model.addRow(row);
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.view.bookmarkview.SearchResultFrame#detailInfo()
     */
    public void detailInfo() throws UnifyException, SQLException {
        int row = getTable().getSelectedRow();
        String name = this.getTable().getValueAt(row, 0).toString(); //��ȡʵ����
        String catalog=SqlUtil.validateSqlParam((String)this.getTable().getValueAt(row, 1));//��ȡ����
        String schema = SqlUtil.validateSqlParam((String) this.getTable().getValueAt(row, 2)); //��ȡģʽ��
        String type = (String) this.getTable().getValueAt(row, 3); //��ȡģʽ��
        Entity entity=null;
        Identifier id =null;
        if(type == SqlUtil.VIEW)
        {
            entity=new ViewImpl(this.getBookmark(),catalog,schema,name, "", false);
            id=new ViewNode(name, this.getBookmark(), entity);
        }else 
        {
            entity=new TableImpl(this.getBookmark(),catalog,schema ,name, "", false); //������ģ��
            id=new TableNode(name, this.getBookmark(), entity);
        }

        
        Operatable operator;
        try {
            operator = OperatorFactory
                    .getOperator(EntityPropertyOperator.class);
        } catch (ClassNotFoundException e) {
            LogProxy.errorReport(e);
            return;
        } catch (InstantiationException e) {
            
            LogProxy.errorReport(new UnifyException("internal error"));
            return;
        } catch (IllegalAccessException e) {
            LogProxy.errorReport(new UnifyException("internal error"));
            return;
        }
        List<Object> list=new ArrayList<Object>();
        list.add(id);
        list.add(GUIUtil.getMainFrame());
        operator.operate(list);
    }

    /* ���� Javadoc��
     * @see com.coolsql.querydbinfo.SearchResultFrame#getSelectObject()
     */
    protected Identifier getSelectObject() throws UnifyException {
        if(this.getBookmark()==null)  //��ǩ����Ϊ��
            throw new UnifyException("no bookmark information");
        
        int row = getTable().getSelectedRow();
        String name = this.getTable().getValueAt(row, 0).toString(); //��ȡʵ����
        String catalog=SqlUtil.validateSqlParam((String)this.getTable().getValueAt(row, 1));//��ȡ����
        String schema = SqlUtil.validateSqlParam((String) this.getTable().getValueAt(row, 2)); //��ȡģʽ��
        String type = (String) this.getTable().getValueAt(row, 3); //��ȡģʽ��
        Entity entity=null;
        if(type.equals(SqlUtil.VIEW))
        {
            entity=new ViewImpl(this.getBookmark(),catalog,schema,name, "", false);
            return new ViewNode(name,this.getBookmark(),entity);
        }else 
        {
            entity=new TableImpl(this.getBookmark(),catalog,schema ,name,type, "", false); //������ģ��
            return new TableNode(name,this.getBookmark(),entity);
        }
    }

    /* (non-Javadoc)
     * @see com.coolsql.querydbinfo.SearchResultFrame#adjustGUI()
     */
    public void adjustGUI() {
        getTable().adjustPerfectWidth();
        
    }
}
