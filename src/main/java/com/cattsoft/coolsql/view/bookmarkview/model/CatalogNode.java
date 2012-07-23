/**
 * 
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.Database;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.model.Catalog;
import com.cattsoft.coolsql.sql.model.Schema;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author ��Т��
 * 
 * 2008-1-1 create
 */
public class CatalogNode extends Identifier {
	private static final long serialVersionUID = 1L;

	/**
	 * �������
	 */
	private Catalog catalog = null;
	public CatalogNode(String content, Bookmark bookmark) {
		super(BookMarkPubInfo.NODE_CATALOG, content, bookmark);
		catalog=new Catalog(content);
	}
	public CatalogNode(String content, Bookmark bookmark, boolean isHasChild) {
		super(BookMarkPubInfo.NODE_CATALOG, content, bookmark, isHasChild);
		catalog=new Catalog(content);
	}
	public CatalogNode(String content, Bookmark bookmark, Catalog catalog) {
		super(BookMarkPubInfo.NODE_CATALOG, content, bookmark);
		this.catalog = catalog;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
	 */
	public void expand(DefaultTreeNode parent,INodeFilter filter) throws SQLException, UnifyException {
		Bookmark bookmark = getBookmark();
		Database db = bookmark.getDbInfoProvider();
		ISQLDatabaseMetaData dbmd = db.getDatabaseMetaData();

		List<Identifier> list=new ArrayList<Identifier>();
		if (dbmd.supportsSchemas()) {
			Schema[] schemas = db.getSchemas(getContent()); // ��ȡ��ݿ������ģʽ��
			/**
			 * �����ڵ����ݱ�ʾ����
			 */
			for (int i = 0; i < schemas.length; i++) {
				String schemaName = schemas[i].getName();
				SchemaNode node= new SchemaNode(schemaName, bookmark, schemas[i]);
				if(filter==null||filter.filter(node))
					list.add(node);
			}
			
		} else {  //��֧��ģʽ(schema)
			String[] types=bookmark.getDbInfoProvider().getDatabaseMetaData().getTableTypes();
			for (int i = 0; i < types.length; i++) {
				Identifier id = null;
				EntityGroup eg = new EntityGroup(new Catalog(catalog.getName(), catalog.getName()), "");
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
				if(filter==null||filter.filter(id))
					list.add(id);

			}
		}
		parent.addChildren(list.toArray(new Identifier[list.size()]));
		
	}
	public void refresh(DefaultTreeNode node,INodeFilter filter) throws SQLException, UnifyException {
		if(!node.isExpanded())
            return ;
		
		boolean changed = false; // �Ƿ��б仯
		HashMap<Object,Object> temp = new HashMap<Object,Object>();
		int childCount = node.getChildCount();
		for (int i = 0; i < childCount; i++) {
			DefaultTreeNode tmp = (DefaultTreeNode) node.getChildAt(i);
			if (tmp.getUserObject() instanceof SchemaNode) // ���ýڵ�Ϊģʽ�ڵ㣬�����ִ��sql��Ͻڵ����
				temp.put(((Identifier) tmp.getUserObject()).getContent(), tmp);
		}

		Bookmark bookmark = getBookmark();
		Database db = bookmark.getDbInfoProvider();
		ISQLDatabaseMetaData dbmd = db.getDatabaseMetaData();

		if (dbmd.supportsSchemas()) {  //�����ݿ�֧��ģʽ����ģʽ����ˢ��
			Schema[] schemas = db.getSchemas(null); // ��ȡ��ݿ������ģʽ��
			for (int i = 0; i < schemas.length; i++) {
				DefaultTreeNode tmp = (DefaultTreeNode) (temp.remove(schemas[i].getName()));
				if (tmp == null)// ��ģʽΪ������
				{
					String s = schemas[i].getName();
					SchemaNode sh = new SchemaNode(s, getBookmark(),schemas[i]);
					if(filter!=null&&!filter.filter(sh))
	                	continue;
					
					node.addChild(sh, 1); // ���˵�recentsql�ڵ�
					changed = true;
				}else
				{
					if(filter!=null&&!filter.filter(tmp.getUserObject()))//The node will be removed
					{
						temp.put(schemas[i].getName(), tmp);
					}
				}
			}

			for (Iterator<Object> it = temp.values().iterator(); it.hasNext();) // ɾ����ڵ�ģʽ
			{
				node.remove((DefaultTreeNode) it.next());
				changed = true;
			}
		}else //���֧��ģʽ����ô���ӽڵ�Ϊʵ�����ͽڵ�
		{
	        for(int i=0;i<node.getChildCount();i++)  //�ݹ�ˢ��
	        {
	            DefaultTreeNode tmpNode=(DefaultTreeNode)node.getChildAt(i);
	            Identifier id=(Identifier)tmpNode.getUserObject();
	            id.refresh(tmpNode,tmpNode.getNodeFilter());
	        }
		}
		if (changed)
			BookmarkTreeUtil.getInstance().refreshBookmarkTree(node); // ˢ�½ڵ���ģ��

		for (int i = 0; i < node.getChildCount(); i++) // �ݹ�ˢ��
		{
			DefaultTreeNode tmpNode = (DefaultTreeNode) node.getChildAt(i);
			Identifier id = (Identifier) tmpNode.getUserObject();
			id.refresh(tmpNode,tmpNode.getNodeFilter());
		}
	}
	/**
	 * ��дObjectHolder�ķ���
	 */
	public Object getDataObject() {
		return catalog;
	}

}
