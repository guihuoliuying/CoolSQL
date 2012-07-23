/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.model.Schema;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author liu_xlin
 *  ģʽ�ڵ���
 */
public class SchemaNode extends Identifier implements NodeExpandable {
	private static final long serialVersionUID = 1L;
	private Schema schema=null;
	public SchemaNode()
	{
        super();
        super.setType(BookMarkPubInfo.NODE_SCHEMA);
	}
	public SchemaNode(String content,Bookmark bookmark)
	{
        super(BookMarkPubInfo.NODE_SCHEMA,content,bookmark);
	}  
	public SchemaNode(String content,Bookmark bookmark,boolean isHasChild)
	{
        super(BookMarkPubInfo.NODE_SCHEMA,content,bookmark,isHasChild);
	}  
	public SchemaNode(String content,Bookmark bookmark,Schema schema)
	{
        super(BookMarkPubInfo.NODE_SCHEMA,content,bookmark);
        this.schema=schema;
	}  
    public String getContent() {
    	if(schema.isDefault())
    		return "";
        return super.getContent();
    }
    /* ���� Javadoc��
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent,INodeFilter filter) throws UnifyException, SQLException {
    	
		String[] types=getBookmark().getDbInfoProvider().getDatabaseMetaData().getTableTypes();
		List<Identifier> list=new ArrayList<Identifier>();
		for (int i = 0; i < types.length; i++) {
			Identifier id = null;
			EntityGroup eg = new EntityGroup(schema, "");
			if (types[i].equals(TYPE_VIEW)) {
				eg.setType(BookMarkPubInfo.NODE_VIEWS);
				id = new ViewGroupNode(groupView, getBookmark(), eg);
			} else if (types[i].equals(TYPE_TABLE)) {
				eg.setType(BookMarkPubInfo.NODE_TABLES);
				id = new TableGroupNode(groupTable, getBookmark(), eg);
			} else {
				eg.setType(BookMarkPubInfo.NODE_SYSTEM_TABLES);
				id = new TableTypeNode(types[i], getBookmark(), eg);
			}
			if(filter!=null&&!filter.filter(id))//invalid node
				continue;
			
			list.add(id);
		}
		parent.addChildren(list.toArray(new Identifier[list.size()]));
    }
    /**
     * ˢ����ǩ�ڵ�,���»�ȡģʽ�ڵ���Ϣ
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#refresh(com.coolsql.view.bookmarkview.model.DefaultTreeNode)
     */
    public void refresh(DefaultTreeNode node) throws SQLException,UnifyException
    {
        if(!node.isExpanded())
            return ;
        for(int i=0;i<node.getChildCount();i++)  //�ݹ�ˢ��
        {
            DefaultTreeNode tmpNode=(DefaultTreeNode)node.getChildAt(i);
            Identifier id=(Identifier)tmpNode.getUserObject();
            id.refresh(tmpNode,tmpNode.getNodeFilter());
        }
    }
    /**
     * ��дObjectHolder�ķ���
     */
    public Object getDataObject()
    {
        return schema;
    }
}
