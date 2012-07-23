/*
 * �������� 2006-9-11
 */
package com.cattsoft.coolsql.gui.property;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author liu_xlin ���Խڵ�����bean
 */
public class NodeKey {
    private String name; //�ڵ�����

    private Icon icon; //�ڵ�ͼ��

    private String displayName;
    
    private NodeKey parent; //�ϼ��ڵ�
    public NodeKey(String name,String iconFilePath,NodeKey parent)
    {
        this.name=name;
        displayName=name;
        this.icon=new ImageIcon(iconFilePath);
        this.parent=parent;
    }
    public NodeKey(String name,Icon icon,NodeKey parent)
    {
        this.name=name;
        displayName=name;
        this.icon=icon;
        this.parent=parent;
    }
    /**
     * @return ���� icon��
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon
     *            Ҫ���õ� icon��
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * @return ���� name��
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            Ҫ���õ� name��
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return ���� displayName��
     */
    public String getDisplayName() {
        return displayName;
    }
    /**
     * @param displayName Ҫ���õ� displayName��
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String toString()
    {
        return name;
    }
    public boolean equals(Object ob)
    {
        if(ob==null)
            return false;
        if(!(ob instanceof NodeKey))
            return false;
        NodeKey key=(NodeKey)ob;
        
        if(parent!=key.getParent())
        	return false;
        
        if(key.getName()==null&&this.name!=null)
            return false;
        if(key.getName()!=null&&!key.getName().equals(name))
        	return false;
        
        if(key.getDisplayName()==null&&this.displayName!=null)
            return false;
        if(key.getDisplayName()!=null&&!key.getDisplayName().equals(displayName))
        	return false;
        
        return true;
    }
    /**
     * @return ���� parent��
     */
    public NodeKey getParent() {
        return parent;
    }
    /**
     * @param parent Ҫ���õ� parent��
     */
    public void setParent(NodeKey parent) {
        this.parent = parent;
    }
}
