/*
 * �������� 2006-7-1
 *
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.SQLException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author liu_xlin ��ʶ�ڵ�����
 */
public abstract class Identifier implements TreeNodeEditable, Serializable,
        Comparable<Identifier>, NodeExpandable,ObjectHolder{
	private static final long serialVersionUID = 1L;
	
	public static final String groupTable="Tables";
    public static final String groupView="Views";
	public static final String TYPE_VIEW="VIEW";
	public static final String TYPE_TABLE="TABLE";
    /**
     * �Ƿ����ӽڵ㣬Ĭ��Ϊ���ӽڵ�
     */
    private boolean hasChildren = true;

    /**
     * ������ǩ
     */
    private Bookmark bookmark = null;

    /**
     * ���ͣ���ʶ��ͬ�ڵ������
     */
    private int type;

    /**
     * �ڵ�ֵ
     */
    private String content;

    /**
     * �ڵ���ʾ��ֵ��
     */
    private String displayLabel;

    protected PropertyChangeSupport pcs = null;

    private boolean isSelected=false;
    public Identifier() {
        this(-1,"",null);
    }

    public Identifier(int type, String content, Bookmark bookmark) {
        this(type,content,bookmark,true);
    }
    public Identifier(int type, String content,String displayLabel, Bookmark bookmark) {
        this(type,content,displayLabel,bookmark,true);
    }
    public Identifier(int type, String content, Bookmark bookmark,
            boolean isHasChild) {
        this(type,content,content,bookmark,isHasChild);
    }
    public Identifier(int type, String content,String displayLabel, Bookmark bookmark,
            boolean isHasChild) {
        this.type = type;
        this.content = content;
        this.displayLabel = displayLabel;
        this.bookmark = bookmark;
        pcs = new PropertyChangeSupport(this);
        this.hasChildren = isHasChild;
    }
    /**
     * @return ���� content��
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            Ҫ���õ� content��
     */
    public void setContent(String content) {
        String oldvalue = this.content;
        this.content = content;
        if (oldvalue == displayLabel)
            displayLabel = content;
        pcs.firePropertyChange("Content", oldvalue, content);
    }

    /**
     * @return ���� type��
     */
    public int getType() {
        return type;
    }

    /**
     * @param type
     *            Ҫ���õ� type��
     */
    public void setType(int type) {
        this.type = type;
    }

    public String toString() {
        return content;
    }

    public void copy() {
        StringSelection ss = new StringSelection(content);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.view.bookmarkview.model.TreeNodeEditable#property()
     */
    public void property() throws UnifyException, SQLException {

    }
    /*
     * �ڵ��ˢ�´���ʵ��
     *  ���� Javadoc��
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#refresh(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void refresh(DefaultTreeNode parent,INodeFilter filter) throws SQLException,UnifyException
    {       
    }
    /**
     * @return ���� bookmark��
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    /**
     * @param bookmark
     *            Ҫ���õ� bookmark��
     */
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    /*
     * ����ģʽ�ڵ㡢��ڵ㡢��ͼ�ڵ㡢��ǩ�ڵ������
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Identifier arg0) {
        if (arg0 == null)
            return 1;
        Identifier id = arg0;

        return content.toLowerCase().compareTo(id.getContent());
    }

    public void addPropertyListener(PropertyChangeListener pl) {
        pcs.addPropertyChangeListener(pl);
    }
    public void addPropertyListener(String name, PropertyChangeListener pl) {
        pcs.addPropertyChangeListener(name, pl);
    }

    public void removePropertyListener(PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(pl);
    }
    
    public boolean isSelected()
    {
    	return isSelected;
    }
    public void setSelected(boolean isSelected)
    {
    	this.isSelected=isSelected;
    }

    public boolean equals(Object ob) {
        if (ob == null)
            return false;
        if (!(ob instanceof Identifier))
            return false;
        Identifier tmp = (Identifier) ob;
        if(bookmark==null)
        {
            if(tmp.bookmark==null)
                return true;
            else
                return false;
        }else
        {
            if(tmp.bookmark==null)
                return false;
        }
        if(content==null)
        {
        	if(tmp.content!=null)
        		return false;
        }else
        {
        	if(!content.equals(tmp.content))
        		return false;
        }
        if (bookmark.getAliasName().equals(tmp.getBookmark().getAliasName())
                && type == tmp.type )
            return true;
        else
            return false;
    }

    /**
     * @return ���� displayLabel��
     */
    public String getDisplayLabel() {
        return displayLabel;
    }

    /**
     * @param displayLabel
     *            Ҫ���õ� displayLabel��
     */
    public void setDisplayLabel(String displayLabel) {
        String oldValue = this.displayLabel;
        this.displayLabel = displayLabel;
        pcs.firePropertyChange("displayLabel", oldValue, displayLabel);
    }

    /**
     * @return ���� hasChildren��
     */
    public boolean isHasChildren() {
        return hasChildren;
    }

    /**
     * @param hasChildren
     *            Ҫ���õ� hasChildren��
     */
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    /**
     * ʵ���˽ӿ�ObjectHolder�ķ���
     */
    public Object getDataObject()
    {
        return null;
    }
}
