package com.cattsoft.coolsql.sql.model;

import java.sql.SQLException;

import com.cattsoft.coolsql.pub.exception.UnifyException;
/**
 * Table entity definition.
 * @author liu_xlin
 */
public interface Table
    extends Entity
{

    public Integer getSize();

    public void deleteAllRows()
        throws SQLException, UnifyException;
    
    /**
     * get primary key of current entity.
     * @return
     * @throws SQLException
     */
    public PrimaryKey getPrimaryKey() throws SQLException,UnifyException;
}
