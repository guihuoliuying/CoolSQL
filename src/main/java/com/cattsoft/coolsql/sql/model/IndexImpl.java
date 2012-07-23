package com.cattsoft.coolsql.sql.model;

import java.util.*;

/**
 * 
 * @author liu_xlin
 * ����bean��
 */
class IndexImpl
    implements Index
{

    private String name;
    private List columnNames;
    private Entity entity;
    private List ascending;
    private List<Integer> positionList;
    private boolean isNonUnique;
    IndexImpl(Entity entity, String name,boolean isNonUnique)
    {
        columnNames = Collections.synchronizedList(new ArrayList());
        ascending = Collections.synchronizedList(new ArrayList());
        positionList=Collections.synchronizedList(new ArrayList<Integer>());
        this.name = name;
        this.entity = entity;
        this.isNonUnique=isNonUnique;
    }

    void addColumn(String columnName, Boolean ascending,int position)
    {
        columnNames.add(columnName);
        this.ascending.add(ascending);
        positionList.add(position);
    }

    public String getName()
    {
        return name;
    }

    public int getNumberOfColumns()
    {
        return columnNames.size();
    }

    public String getColumnName(int ordinalPosition)
    {
        return (String)columnNames.get(ordinalPosition);
    }

    public Entity getParentEntity()
    {
        return entity;
    }

    public boolean isAscending(int ordinalPosition)
    {
        Boolean ascending = (Boolean)this.ascending.get(ordinalPosition);
        return Boolean.TRUE.equals(ascending);
    }

    public boolean isDescending(int ordinalPosition)
    {
        Boolean ascending = (Boolean)this.ascending.get(ordinalPosition);
        return Boolean.FALSE.equals(ascending);
    }
    public int getOrdinalPosition(int i)
    {
    	return positionList.get(i);
    }
    public boolean isNonUnique()
    {
    	return isNonUnique;
    }
}
