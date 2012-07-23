/**
 * 
 */
package com.cattsoft.coolsql.sql.model;

import java.util.List;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-1-12 create
 */
public interface IPrimaryKey {

    /**
     * get catalog of primary key
     * @return
     */
    public String getCatalog();
    /**
     * get schema of primary key
     * @return
     */
    public String getSchema();
    
    public String getTable();
    
    public String getPkName();
    
    public int getNumberOfColumns();
    //primary key may include more than one column;
    public String getColumn(int i);
    
    public short getKeySequence(int i);
    
    public void addColumn(String column,short keySequence);
}
