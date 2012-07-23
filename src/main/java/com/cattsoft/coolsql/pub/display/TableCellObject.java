/*
 * �������� 2006-8-20
 */
package com.cattsoft.coolsql.pub.display;

import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author liu_xlin
 *���Ԫ�ص�value�����ڱ����Ⱦ���ܹ���ʾͼ�꣩
 */
public class TableCellObject implements Comparable,Serializable{

	private static final long serialVersionUID = -7845145524115346055L;
	/**
     * ��Ⱦͼ��
     */
    private transient Icon icon=null;
    /**
     * ���Ԫ����ʾ������
     */
    private String value=null;
    
    //used to display on ui
    private String displayValue=null;
    public TableCellObject()
    {
        this("",null);
    }
    public TableCellObject(String value)
    {
        this(value,null);
    }
    public TableCellObject(String value,Icon icon)
    {
        this(value,value,icon);
    }
    public TableCellObject(String value,String displayValue,Icon icon)
    {
        this.value=value;
        this.displayValue=displayValue;
        this.icon=icon;
    }
    /**
     * ������Ԫ���Ƿ����ͼ���չʾ
     * @return
     */
    public boolean hasIcon()
    {
        return icon!=null;
    }
    /**
     * ��дtoString()����
     */
    public String toString()
    {
        return getDisplayValue();
    }
    /**
     * ��дequals()����
     */
    public boolean equals(Object ob)
    {
        if(ob==null)
            return false;
        if(!(ob instanceof TableCellObject))
            return false;
        TableCellObject tmp=(TableCellObject)ob;
        if(tmp.icon.toString().equals(this.icon.toString())&&tmp.value.equals(value))
            return true;
        else
            return false;
    }
    /* ���� Javadoc��
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {
        if(arg0==null)
          return 1;
        if(!(arg0 instanceof TableCellObject))
            return -1;
        TableCellObject tmp=(TableCellObject)arg0;
        return value.compareTo(tmp.value);
    }
    /**
     * @return ���� icon��
     */
    public Icon getIcon() {
        return icon;
    }
    /**
     * @param icon Ҫ���õ� icon��
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    /**
     * @return ���� value��
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value Ҫ���õ� value��
     */
    public void setValue(String value) {
        this.value = value;
    }
	/**
	 * @return the displayValue
	 */
	public String getDisplayValue() {
		return this.displayValue;
	}
}
