

package com.cattsoft.coolsql.sql.model;

import com.cattsoft.coolsql.sql.util.TypesHelper;
/**
 * 
 * @author liu_xlin
 *
 */
public class ColumnImpl
    implements Column, Comparable
{

    private long size;
    private boolean nullable;
    private int primaryKeyOrder=-1;
    private String name;
    private Entity entity;
    private int numberOfFractionalDigits;
    private String typeName;
    private int type;
    private int position;
    private String remarks;
    
    private int radix;
    private int octetLength;
    private String defaultValue;
    private int nullAllowed;
    
    public ColumnImpl()
    {
    	
    }
    public ColumnImpl(Entity entity, String name, String typeName, int type, long size, int numberOfFractionalDigits, 
            boolean nullable, int position, String remarks,int radix,int octetLength,String defaultValue,int nullAllowed)
    {
        this.entity = entity;
        this.name = name;
        this.typeName = typeName;
        this.type = type;
        this.size = size;
        this.numberOfFractionalDigits = numberOfFractionalDigits;
        this.nullable = nullable;
        this.position = position;
        this.remarks = remarks;
        
        this.radix=radix;
        this.octetLength=octetLength;
        this.defaultValue=defaultValue;
        this.nullAllowed=nullAllowed;
    }

    public int getPrimaryKeyOrder()
    {
        return primaryKeyOrder;
    }

    public boolean isPrimaryKey()
    {
        return getPrimaryKeyOrder() > -1;
    }

    public String getName()
    {
        return name;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public boolean isReal()
    {
        return false;
    }

    public boolean isNullable()
    {
        return nullable;
    }

    public boolean isNumeric()
    {
        return TypesHelper.isNumberic(type);
    }

    public int getType()
    {
        return type;
    }

    public Entity getParentEntity()
    {
        return entity;
    }

    public String getQualifiedTableName()
    {
        return entity.getQualifiedName();
    }

    public void setPrimaryKeyOrder(int i)
    {
        primaryKeyOrder = i;
    }

    public int compareTo(Object o)
    {
        ColumnImpl that = (ColumnImpl)o;
        if(isPrimaryKey() && that.isPrimaryKey())
            return primaryKeyOrder - that.primaryKeyOrder;
        if(isPrimaryKey())
            return -1;
        if(that.isPrimaryKey())
            return 1;
        else
            return position - that.position;
    }

    public long getSize()
    {
        return size;
    }

    public int getNumberOfFractionalDigits()
    {
        return numberOfFractionalDigits;
    }

    public String getRemarks()
    {
        return remarks != null ? remarks : "";
    }

    public String toString()
    {
        return name;
    }
    /**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * @return the nullAllowed
	 */
	public int getNullAllowed() {
		return this.nullAllowed;
	}

	/**
	 * @return the octetLength
	 */
	public int getOctetLength() {
		return this.octetLength;
	}

	/**
	 * @return the radix
	 */
	public int getRadix() {
		return this.radix;
	}

	/* (non-Javadoc)
     * @see com.coolsql.sql.model.Column#getPosition()
     */
    public int getPosition() {
        return position;
    }
	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}
	/**
	 * @param nullable the nullable to set
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param numberOfFractionalDigits the numberOfFractionalDigits to set
	 */
	public void setNumberOfFractionalDigits(int numberOfFractionalDigits) {
		this.numberOfFractionalDigits = numberOfFractionalDigits;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @param radix the radix to set
	 */
	public void setRadix(int radix) {
		this.radix = radix;
	}
	/**
	 * @param octetLength the octetLength to set
	 */
	public void setOctetLength(int octetLength) {
		this.octetLength = octetLength;
	}
	/**
	 * @param nullAllowed the nullAllowed to set
	 */
	public void setNullAllowed(int nullAllowed) {
		this.nullAllowed = nullAllowed;
	}
}
