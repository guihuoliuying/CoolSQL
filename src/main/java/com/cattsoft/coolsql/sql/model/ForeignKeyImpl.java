package com.cattsoft.coolsql.sql.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.util.SqlUtil;

/**
 * A implement to foreign key interface. 
 * @author liu_xlin 
 */
public class ForeignKeyImpl implements ForeignKey {
    private String pkName; //primary key name

    private String localEntityName;

    private String localEntityCatalog;
    private String localEntitySchema;

    private String fkName;  //foreign key name
    private String foreignEntityCatalog;
    private String foreignEntityName;

    private String foreignEntitySchema;

    private List foreignColumns;

    private List localColumns;

    private int deleteRule;
    private int updateRule;

    private Bookmark bookmark;
    public ForeignKeyImpl() {
        foreignColumns = Collections.synchronizedList(new ArrayList());
        localColumns = Collections.synchronizedList(new ArrayList());
    }
    public ForeignKeyImpl(String pkName,String localEntityCatalogName,String localEntitySchema,String localEntityName,
    		String fkName,String foreignEntityCatalogName,String foreignEntitySchema,String foreignEntityName,
    		List localColumns,List foreignColumns,int deleteRule,int updateRule,Bookmark bookmark) {
    	this.pkName=pkName;
    	this.localEntityCatalog=localEntityCatalogName;
    	this.localEntitySchema=localEntitySchema;
    	this.localEntityName=localEntityName;
    	
    	this.fkName=fkName;
    	this.foreignEntityCatalog=foreignEntityCatalogName;
    	this.foreignEntitySchema=foreignEntitySchema;
    	this.foreignEntityName=foreignEntityName;
    	
    	this.localColumns=localColumns;
    	this.foreignColumns=foreignColumns;
    	this.deleteRule=deleteRule;
    	this.updateRule=updateRule;
    	this.bookmark=bookmark;
    }
    public void addColumns(String localColumnName, String foreignColumnName) {
        foreignColumns.add(foreignColumnName);
        localColumns.add(localColumnName);
    }

    public int getDeleteRule() {
        return deleteRule;
    }

    public void setDeleteRule(int deleteRule) {
        this.deleteRule = deleteRule;
    }

    public String getForeignEntityName() {
        return foreignEntityName;
    }

    public void setForeignEntityName(String foreignEntityName) {
        this.foreignEntityName = foreignEntityName;
    }

    public String getForeignEntitySchema() {
        return foreignEntitySchema;
    }

    public void setForeignEntitySchema(String foreignEntitySchema) {
        this.foreignEntitySchema = foreignEntitySchema;
    }

    public String getLocalEntityName() {
        return localEntityName;
    }

    public void setLocalEntityName(String localEntityName) {
        this.localEntityName = localEntityName;
    }

    public String getLocalEntitySchema() {
        return localEntitySchema;
    }

    public void setLocalEntitySchema(String localEntitySchema) {
        this.localEntitySchema = localEntitySchema;
    }

    public int getNumberOfColumns() {
        return localColumns.size();
    }

    public boolean equals(Object object) {
        if (getClass() != object.getClass())
            return false;
        ForeignKeyImpl that = (ForeignKeyImpl) object;
        if (pkName == null && that.pkName != null)
            return false;
        if (pkName != null && !pkName.equals(that.pkName))
            return false;
        if (fkName == null && that.fkName != null)
            return false;
        if (fkName != null && !fkName.equals(that.fkName))
            return false;
        if (foreignEntitySchema == null && that.foreignEntitySchema != null)
            return false;
        if (foreignEntitySchema != null
                && !foreignEntitySchema.equals(that.foreignEntitySchema))
            return false;
        if (foreignEntityName == null && that.foreignEntityName != null)
            return false;
        if (foreignEntityName != null
                && !foreignEntityName.equals(that.foreignEntityName))
            return false;
        if (localEntitySchema == null && that.localEntitySchema != null)
            return false;
        if (localEntitySchema != null
                && !localEntitySchema.equals(that.foreignEntitySchema))
            return false;
        if (localEntityName == null && that.localEntityName != null)
            return false;
        if (localEntityName != null
                && !localEntityName.equals(that.localEntityName))
            return false;
        if (deleteRule != that.deleteRule)
            return false;
        if (updateRule != that.updateRule)
            return false;
        if (localColumns.size() != that.localColumns.size()
                || foreignColumns.size() != that.foreignColumns.size())
            return false;
        boolean result = true;
        int i = 0;
        for (int length = localColumns.size(); i < length; i++) {
            Object localColumn = localColumns.get(i);
            result &= localColumn != null
                    && localColumn.equals(that.localColumns.get(i));
            Object foreignColumn = foreignColumns.get(i);
            result &= foreignColumn != null
                    && foreignColumn.equals(that.foreignColumns.get(i));
        }

        return result;
    }

    public int hashCode() {
        int hashCode = 57;
        if (pkName != null)
            hashCode ^= pkName.hashCode();
        if (fkName != null)
            hashCode ^= fkName.hashCode();
        if (foreignEntitySchema != null)
            hashCode ^= foreignEntitySchema.hashCode();
        if (foreignEntityName != null)
            hashCode ^= foreignEntityName.hashCode();
        if (localEntitySchema != null)
            hashCode ^= localEntitySchema.hashCode();
        if (localEntityName != null)
            hashCode ^= localEntityName.hashCode();
        hashCode ^= deleteRule;
        int i = 0;
        for (int length = localColumns.size(); i < length; i++) {
            hashCode ^= localColumns.get(i).hashCode();
            hashCode ^= foreignColumns.get(i).hashCode();
        }

        return hashCode;
    }

    public String getLocalColumnName(int index) {
        return (String) localColumns.get(index);
    }

    public String getForeignColumnName(int index) {
        return (String) foreignColumns.get(index);
    }

    public String getLocalEntityQualifiedName() {
		try {
			return SqlUtil.generateQualifiedName(getLocalEntityCatalog(),
					getLocalEntitySchema(), getLocalEntityName(), bookmark);
		} catch (UnifyException e) {
			return getLocalEntityName();
		}
	}

    public String getForeignEntityQualifiedName() {                
        try {
        	return SqlUtil.generateQualifiedName(getForeignEntityCatalog(), getForeignEntitySchema(), getForeignEntityName(), bookmark);
        } catch (UnifyException e) {
        	return getForeignEntityName();
        }
    }

	/**
	 * @return the localEntityCatalogName
	 */
	public String getLocalEntityCatalog() {
		return this.localEntityCatalog;
	}

	/**
	 * @param localEntityCatalogName the localEntityCatalogName to set
	 */
	public void setLocalEntityCatalog(String localEntityCatalog) {
		this.localEntityCatalog = localEntityCatalog;
	}
	public String getForeignEntityCatalog()
	{
		return foreignEntityCatalog;
	}
	public void setForeignEntityCatalog(String foreignEntityCatalog)
	{
		this.foreignEntityCatalog=foreignEntityCatalog;
	}
	/**
	 * @return the fkName
	 */
	public String getFkName() {
		return this.fkName;
	}
	/**
	 * @param fkName the fkName to set
	 */
	public void setFkName(String fkName) {
		this.fkName = fkName;
	}
	/**
	 * @return the pkname
	 */
	public String getPkName() {
		return this.pkName;
	}
	/**
	 * @param pkname the pkname to set
	 */
	public void setPkName(String pkname) {
		this.pkName = pkname;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.ForeignKey#getUpdateRule()
	 */
	public int getUpdateRule() {
		
		return updateRule;
	}
	public void setUpdateRule(short updateRule)
	{
		this.updateRule=updateRule;
	}
}
