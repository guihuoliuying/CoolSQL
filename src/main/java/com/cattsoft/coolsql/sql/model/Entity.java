/*
 * �������� 2006-9-9
 */
package com.cattsoft.coolsql.sql.model;

import java.sql.SQLException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;


/**
 * Entity interface.
 * @author liu_xlin
 */
public interface Entity extends Comparable<Entity>{
    /**
     * @return Entity name.
     */
    public  String getName();

    /**
     * 
     * @return Catalog name.
     */
    public String getCatalog();
    /**
     * @return Schema name.
     */
    public  String getSchema();

    /**
     * 
     * @return Entity type ��TABLE ��VIEW ,SEQUENCE
     */
    public  String getType();

    /**
     * @return return true if it's synonym, otherwise return false.
     */
    public  boolean isSynonym();

    /**
     * 
     * @return return all columns which this entity contains .
     */
    public  Column[] getColumns()
        throws UnifyException, SQLException;

    public  Index[] getIndexes()
        throws UnifyException, SQLException;

    public  Column getColumn(String s)
        throws UnifyException, SQLException;

    /**
     * @return Retrieve the qualified name of this entity.
     */
    public  String getQualifiedName();


    /**
     * @return return quoted table name if this entity is  table.
     */
    public  String getQuotedTableName();

    /**
     * @return  Retrieve exported foreign keys of this entity.
     */
    public  ForeignKey[] getExportedKeys()
        throws UnifyException, SQLException;

    /**
     * @return Retrieve imported foreign keys of this entity.
     */
    public  ForeignKey[] getImportedKeys()
        throws UnifyException, SQLException;

    /**
     * 
     * @return Retrieve foreign keys of this entity , including imported and exported foreign keys.
     */
    public  ForeignKey[] getReferences()
        throws UnifyException, SQLException;

    /**
     * 
     * @return the remark of entity.
     */
    public String getRemark();
    /**
     * 
     * @return return the bookmark which this entity belongs to.
     */
    public  Bookmark getBookmark();
    
    /**
     * Refresh all information related to this entity. 
     *
     */
    public  void refresh() throws UnifyException, SQLException;;
    /**
     * Entity type definition.
     */
    //TABLE TYPE
    public static final String TABLE_TYPE = "TABLE";
    //VIEW TYPE
    public static final String VIEW_TYPE = "VIEW";
    //SEQUENCE TYPE
    public static final String SEQUENCE_TYPE = "SEQUENCE";
}
