/*
 * �������� 2006-9-10
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.commonoperator.ColumnPropertyOperator;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *
 */
public class ColumnNode extends Identifier {
	private static final long serialVersionUID = 1L;
	/**
     * ��������Ϣ��
     */
    private Column column;
	public ColumnNode()
	{
        super();
        super.setType(BookMarkPubInfo.NODE_COLUMN);
        super.setHasChildren(false);
	}
	public ColumnNode(String content,String displayLabel,Bookmark bookmark)
	{
        this(content,displayLabel,bookmark,null);
	}   	
	public ColumnNode(String content,String displayLabel,Bookmark bookmark,Column column)
	{
        super(BookMarkPubInfo.NODE_COLUMN,content,displayLabel,bookmark);
        super.setHasChildren(false);
        setType(BookMarkPubInfo.NODE_COLUMN);
        this.column=column;
        if(column!=null&&column.isPrimaryKey())  //�ж��Ƿ�������
            setType(BookMarkPubInfo.NODE_KEYCOLUMN);
	}  
    /* ���� Javadoc��
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent,INodeFilter filter) throws SQLException, UnifyException {

    }
    /**
     * ��дObjectHolder�ķ���
     */
    public Object getDataObject()
    {
        return column;
    }
    /**
     * ��д���Բ鿴����
     * @throws SQLException
     * @throws UnifyException
     */
    public void property() throws UnifyException, SQLException
    {
        //��ʼ�����
        List<Object> list=new ArrayList<Object>();
        list.add(this.getBookmark());
        Entity entity=column.getParentEntity();
        list.add(entity.getCatalog());
        list.add(entity.getSchema());
        list.add(entity.getName());
        list.add(column.getName());
        list.add(GUIUtil.findLikelyOwnerWindow());
        
        Operatable operator;
        try {
            operator = OperatorFactory.getOperator(ColumnPropertyOperator.class);
        } catch (ClassNotFoundException e) {
            LogProxy.errorReport(e);
            return;
        } catch (InstantiationException e) {
            LogProxy.internalError(e);
            return;
        } catch (IllegalAccessException e) {
            LogProxy.internalError(e);
            return;
        }
        operator.operate(list);
    }
}
