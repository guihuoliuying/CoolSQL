

package com.cattsoft.coolsql.sql.model;

/**
 * Foreign key definition.
 * @author liu_xlin
 */
public interface ForeignKey
{
    
    public  String getPkName();

    public String getFkName();
        
    public  String getLocalEntityName();

    public  String getLocalEntityCatalog();
    
    public  String getLocalEntitySchema();

    public  String getLocalEntityQualifiedName();

    public  String getForeignEntityName();

    public  String getForeignEntityCatalog();
    
    public  String getForeignEntitySchema();

    public  String getForeignEntityQualifiedName();

    public  int getNumberOfColumns();

    public  String getLocalColumnName(int i);

    public  String getForeignColumnName(int i);

    public  int getDeleteRule();
    
    public  int getUpdateRule();
}
