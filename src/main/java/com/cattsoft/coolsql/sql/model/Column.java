

package com.cattsoft.coolsql.sql.model;
/**
 * Column interface. This interface defines some methods which return all information related to column object.
 * @author liu_xlin
 */
public interface Column
{

	/**
	 * 
	 * @return the size of column
	 */
    public  long getSize();

    /**
     * 
     * @return return primary key order if primary keys includes this column.
     */
    public  int getPrimaryKeyOrder();

    /**
     * 
     * @return Indicates whether this column is a primary key.
     */
    public  boolean isPrimaryKey();

    /**
     * 
     * @return return column name.
     */
    public  String getName();

    /**
     * @return the number of fractional digits of this column.
     */
    public  int getNumberOfFractionalDigits();

    /**
     * 
     * @return the type name of column.
     */
    public  String getTypeName();

    /**
     * @return Indicate whether the type of column is real.
     */
    public  boolean isReal();

    /**
     * @return Indicate whether column is nullable.
     */
    public  boolean isNullable();

    /**
     * @return return whether the type of column is numeric.
     */
    public  boolean isNumeric();

    /**
     * @return return the type of column.
     */
    public  int getType();

    public  Entity getParentEntity();

    public  String getQualifiedTableName();

    public  int getPosition();
    public  String getRemarks();
    
    public int getRadix();
    public int getOctetLength();
    public int getNullAllowed();
    public String getDefaultValue();
    
}
