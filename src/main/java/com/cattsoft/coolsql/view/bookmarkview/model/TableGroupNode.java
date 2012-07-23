/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.model.IDatabaseMode;
import com.cattsoft.coolsql.sql.model.Schema;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ����Ͻڵ���
 */
public class TableGroupNode extends Identifier {
	private static final long serialVersionUID = 1L;
	/**
     * ��������/ģʽ
     */
    private IDatabaseMode dm = null;

    public TableGroupNode() {
        super();
        super.setType(BookMarkPubInfo.NODE_TABLES);
    }

    public TableGroupNode(String content, Bookmark bookmark) {
        super(BookMarkPubInfo.NODE_TABLES, content, bookmark);
    }

    public TableGroupNode(String content, Bookmark bookmark, boolean isHasChild) {
        super(BookMarkPubInfo.NODE_TABLES, content, bookmark, isHasChild);
    }

    public TableGroupNode(String content, Bookmark bookmark, IDatabaseMode dm) {
        super(BookMarkPubInfo.NODE_TABLES, content, bookmark);
        this.dm = dm;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent,INodeFilter filter) throws SQLException, UnifyException {
        //���Ȼ�ȡģʽ�µ����б�ʵ��
        Entity[] entitys = getEntities();
        if (entitys == null)
            return;

        //��ɽڵ��ʶ����
        List<Identifier> list=new ArrayList<Identifier>();
        for (int i = 0; i < entitys.length; i++) {
        	Identifier id = new TableNode(entitys[i].getName(), getBookmark(),
                    entitys[i]);
        	if(filter==null||filter.filter(id))
        		list.add(id);
        }

        //��ӽڵ�
        parent.addChildren(list.toArray(new Identifier[list.size()]));
    }
    private Entity[] getEntities() {
    	IDatabaseMode parentObj = ((EntityGroup) dm).getParentObject();
    	Schema schema = null;
    	if (parentObj.getType() == BookMarkPubInfo.NODE_CATALOG) {
    		schema = new Schema(parentObj.getName(), null);
    	} else if (parentObj.getType() == BookMarkPubInfo.NODE_SCHEMA) {
    		schema = (Schema) parentObj;
    	} else {
    		return null;
    	}
    	 Entity[] entities = null;
		try {
			entities = this.getBookmark().getDbInfoProvider().getEntities(
			         getBookmark(), schema, new String[]{"TABLE"});
		} catch (Exception e) {
			LogProxy.errorReport("Retrieving entities failed: " + e.getMessage(), e);
		}
    	 
    	 return entities;
    }
    /**
     * �ڵ��ˢ�´���ʵ��
     *  ���� Javadoc��
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#refresh(com.coolsql.view.bookmarkview.model.DefaultTreeNode)
     */
    public void refresh(DefaultTreeNode parent,INodeFilter filter) throws SQLException,UnifyException
    {
        if(!parent.isExpanded())
            return;
        boolean changed=false; //�Ƿ��б仯
        Map<String, DefaultTreeNode> temp=new HashMap<String, DefaultTreeNode>();
        int childCount=parent.getChildCount();
        for(int i=0;i<childCount;i++)
        {
            DefaultTreeNode tmp=(DefaultTreeNode)parent.getChildAt(i);       
            temp.put(((Identifier)tmp.getUserObject()).getContent(),tmp);
        }
        
        Entity[] entitys = getEntities();
        for(int i=0;i<entitys.length;i++)
        {
            DefaultTreeNode node=(DefaultTreeNode)temp.remove(entitys[i].getName());
            if(node==null)
            {
                if(parent.getChildCount()<1)
                {
                    Identifier id=(Identifier)parent.getUserObject();
                    id.setHasChildren(true);
                }
                TableNode tmpNode=new TableNode(entitys[i].getName(),this.getBookmark(),entitys[i]);
                if(filter!=null&&!filter.filter(tmpNode))
                	continue;
                
                parent.addChild(tmpNode);
                
                changed=true;
            }else
            {
            	if(filter!=null&&!filter.filter(node.getUserObject()))
            	{
            		temp.put(entitys[i].getName(), node);
            	}
            }
        }

        for(Iterator<DefaultTreeNode> it = temp.values().iterator(); it.hasNext();) //ɾ����ڵ�ģʽ
        {
            parent.remove((DefaultTreeNode)it.next());
            changed = true;
        }
        if(changed)
        	BookmarkTreeUtil.getInstance().refreshBookmarkTree(parent); //ˢ�½ڵ���ģ��  
        
        for(int i=0;i<parent.getChildCount();i++)  //�ݹ�ˢ��
        {
            DefaultTreeNode tmpNode=(DefaultTreeNode)parent.getChildAt(i);
            Identifier id=(Identifier)tmpNode.getUserObject();
            id.refresh(tmpNode,tmpNode.getNodeFilter());
        }
    }
    /**
     * ��дIdentifier�ķ���
     */
    public Object getDataObject() {
        return dm;
    }
}
