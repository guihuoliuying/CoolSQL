/*
 * �������� 2006-9-9
 */
package com.cattsoft.coolsql.sql.model;

import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;

/**
 * Schema definition.
 * @author liu_xlin
 */
public class Schema implements Comparable<Schema>,IDatabaseMode {

	private String catalog; //����
	
    private String name;

    private String displayName;
    /**
     * �Ƿ���ȱʡ��ģʽ������ģʽ��Ϊ������ݿ���û�����ô��ֵӦ��Ϊtrue,����Ϊfalse;
     */
    private boolean isDefault;

    private boolean exists;
    public Schema() {
        exists = true;
    }

    public Schema(String catalog,String name, boolean isDefault) {
    	this(catalog,name,name,isDefault);
    }
    public Schema(String catalog,String name, String displayName, boolean isDefault) {
        exists = true;
        this.name = name;
        this.displayName = displayName;
        this.isDefault = isDefault;
        this.catalog=catalog;
    }

    public Schema(String catalog,String name) {
        this(catalog,name, name, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(Schema that) {
        if (that.isDefault() == isDefault())
            return getDisplayName().compareTo(that.getDisplayName());
        else
            return that.isDefault() ? 1 : -1;
    }

    public boolean equals(Object obj) {
    	if(obj==null)
    		return false;
        if (getClass() != obj.getClass())
            return false;
        Schema that = (Schema) obj;
        if (catalog == null && that.catalog != null)
            return false;
        if (catalog != null && !catalog.equals(that.catalog))
            return false;
        if (name == null && that.name != null)
            return false;
        if (name != null && !name.equals(that.name))
            return false;
        if (displayName == null && that.displayName != null)
            return false;
        return displayName != null || displayName.equals(that.displayName);
    }

    public int hashCode() {
        int hashCode = 51;
        if(catalog!=null)
        	hashCode ^= catalog.hashCode();
        if (name != null)
            hashCode ^= name.hashCode();
        if (displayName != null)
            hashCode ^= displayName.hashCode();
        return hashCode;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean exists() {
        return exists;
    }

    void setExists(boolean exists) {
        this.exists = exists;
    }

    public String toString() {
        return displayName != null ? displayName : name;
    }

	/**
	 * @return the catalog
	 */
	public String getCatalog() {
		return this.catalog;
	}

	/**
	 * @param catalog the catalog to set
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IDatabaseMode#getType()
	 */
	public int getType() {
		return BookMarkPubInfo.NODE_SCHEMA;
	}

}