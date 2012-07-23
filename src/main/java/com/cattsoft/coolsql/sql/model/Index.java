package com.cattsoft.coolsql.sql.model;
/**
 * Indices definition
 * @author liu_xlin
 */
public interface Index
{

    public  String getName();

    public  int getNumberOfColumns();

    public  String getColumnName(int i);

    public  Entity getParentEntity();

    public  boolean isAscending(int i);

    public  boolean isDescending(int i);
    
    public int getOrdinalPosition(int i);
    
    public boolean isNonUnique();
}
