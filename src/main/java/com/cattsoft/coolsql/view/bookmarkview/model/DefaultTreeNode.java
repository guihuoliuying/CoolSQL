/*
 * �������� 2006-9-7
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��ǩ��ͼ�����Ľڵ��࣬������չ�˽ڵ��࣬�����ڸ�����Ͻ������������
 */
public class DefaultTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = -753350526562735325L;
	private boolean expanded = false;

	private INodeFilter nodeFilter;
    public DefaultTreeNode() {
        super();
    }

    public DefaultTreeNode(Object ob) {
        super(ob);
    }
    public DefaultTreeNode(Object ob,boolean allowChildren)
    {
    	super(ob,allowChildren);
    }
    /**
     * Create a tree node object with id.
     * It's convenient to extend this class by rewriting this method.
     * @param id user object.
     * @return return a object created according to actual type 
     */
    protected DefaultTreeNode createTreeNode(Identifier id) {
    	return new DefaultTreeNode(id);
    }
    /**
     * ������ʽ���в���ڵ�
     * @param id
     */
    public void addChild(Identifier id)
    {
        int count=this.getChildCount();
        if(count<1)
        {
            Identifier tmpId=(Identifier)getUserObject();
            tmpId.setHasChildren(true);
            
            insert(createTreeNode(id),0);
            return;
        }
        for(int i=0;i<count;i++)
        {
            DefaultTreeNode node=(DefaultTreeNode)this.getChildAt(i);
            Identifier itf=(Identifier)node.getUserObject();
            if(id.getContent().compareTo(itf.getContent())<=0)
            {
                insert(createTreeNode(id),i);
                return;
            }
        }
        insert(createTreeNode(id),count);
    }
    /**
     * �Ӹ�������󰴽���ʽ���в���ڵ�
     * @param id
     * @param index  -- �Ӹ�����������ĸ˳������ӽڵ�
     */
    public void addChild(Identifier id,int index)
    {
        int count=this.getChildCount();
        if(index>=count){
            insert(createTreeNode(id),count);
        }
        if(count<1)
        {
            Identifier tmpId=(Identifier)getUserObject();
            tmpId.setHasChildren(true);
            
            insert(createTreeNode(id),0);
            return;
        }
        for(int i=index;i<count;i++)
        {
            DefaultTreeNode node=(DefaultTreeNode)this.getChildAt(i);
            Identifier itf=(Identifier)node.getUserObject();
            if(id.getContent().compareTo(itf.getContent())<=0)
            {
                insert(createTreeNode(id),i);
                return;
            }
        }
        insert(createTreeNode(id),count);
    }
    /**
     * Ϊ���ڵ�����ӽڵ� �Խڵ����������
     */
    public void addChildren(Identifier[] ids) {
    	if(ids==null)
    		return;
        Arrays.sort(ids, new SortIdentifer());
        for (int i = 0; i < ids.length; i++) {
            DefaultTreeNode node = createTreeNode(ids[i]);
            add(node);
        }
    }
    public void addChildren(List<Identifier> ids) {
    	if(ids==null)
    		return;
        Collections.sort(ids, new SortIdentifer());
        for (int i = 0; i < ids.size(); i++) {
        	DefaultTreeNode node = createTreeNode(ids.get(i));
            add(node);
        }
    }
    @Override
    public boolean isLeaf() {
        Object userOb = this.getUserObject();
        if (userOb == null || !(userOb instanceof Identifier)) {
            return super.isLeaf();
        }
        Identifier id = (Identifier) userOb;
        //       if(id.getBookmark()==null)
        //           return super.isLeaf();
        return (!id.isHasChildren());
    }

    /**
     * չ���ڵ�
     *  return boolean ����Ѿ�չ������false�����δչ������true
     */
    public boolean expand() {
        if (!expanded) {
            expanded = true;
            Identifier id = (Identifier) this.getUserObject();
            try {
                id.expand(this,nodeFilter);
            } catch (SQLException e) {              
                LogProxy.SQLErrorReport(e);
            } catch (UnifyException e) {
                LogProxy.errorReport(e);
            } 
            return true;
        }else
            return false;
    }

    public boolean equals(Object ob)
    {
        if(ob==null)
        {
            return false;
        }
        
        if(!(ob instanceof DefaultTreeNode))
        {
            return false;
        }
        
        if(this.getUserObject()==null)
        {
            if(((DefaultTreeNode)ob).getUserObject()==null)
                return true;
            else
                return false;
        }
        
        return this.getUserObject().equals(((DefaultTreeNode)ob).getUserObject());
    }
    /**
     * 
     * @author liu_xlin �����ڵ��������
     */
    protected class SortIdentifer implements Comparator<Identifier> {
        public SortIdentifer() {

        }

        /*
         * ���� Javadoc��
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Identifier id0, Identifier id1) {
            if (id0 == null || id0.getContent() == null)
                return -1;
            if (id1 == null || id1.getContent() == null)
                return 1;
            return id0.getContent().compareTo(id1.getContent());
        }
    }

    /**
     * @return ���� expanded��
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * @param expanded
     *            Ҫ���õ� expanded��
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

	/**
	 * @return the nodeFilter
	 */
	public INodeFilter getNodeFilter() {
		return this.nodeFilter;
	}

	/**
	 * @param nodeFilter the nodeFilter to set
	 */
	public void setNodeFilter(INodeFilter nodeFilter) {
		this.nodeFilter = nodeFilter;
	}
}
