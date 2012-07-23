package com.cattsoft.coolsql.sql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.cattsoft.coolsql.sql.model.Schema;

/**
 * An interface that describes public API of SQLDatabaseMetaData.  
 * 
 * @author manningr
 */
public interface ISQLDatabaseMetaData {

    /**
     * Return the name of the current user. Cached on first call.
     *
     * @return  the current user name.
     */
    String getUserName() throws SQLException;

    /**
     * Return the database product name for this connection. Cached on first
     * call.
     *
     * @return  the database product name for this connection.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    String getDatabaseProductName() throws SQLException;

    /**
     * Return the database product version for this connection. Cached on first
     * call.
     *
     * @return  database product version
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    String getDatabaseProductVersion() throws SQLException;

    /**
     * Return the database major version for this connection. Cached on first
     * call.
     *
     * @return  database major version
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */    
    int getDatabaseMajorVersion() throws SQLException;
    
    /**
     * Return the JDBC driver name for this connection. Cached on first call.
     *
     * @return  the JDBC driver name for this connection.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    String getDriverName() throws SQLException;

    /**
     * Return the JDBC version of this driver. Cached on first call.
     *
     * @return  the JDBC version of the driver.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    int getJDBCVersion() throws SQLException;

    /**
     * Return the string used to quote characters in this DBMS. Cached on first
     * call.
     *
     * @return  quote string.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    String getIdentifierQuoteString() throws SQLException;

    /**
     * Returns the "cascade" constraints clause which is supported by some 
     * databases when performing a delete to removed child records in dependent
     * tables which would otherwise be orphaned and make the delete fail.
     *  
     * @return the "cascade" clause.
     * 
     * @throws SQLException
     */
//    String getCascadeClause() throws SQLException;

    /**
     * Return a string array containing the names of all the schemas in the
     * database. Cached on first call.
     *
     * @return  String[] of the names of the schemas in the database.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    Schema[] getSchemas() throws SQLException;

    /**
     * Retrieves whether this database supports schemas at all.
     *
     * @return  <TT>true</TT> if database supports schemas.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsSchemas() throws SQLException;

    /**
     * Retrieves whether a schema name can be used in a data manipulation
     * statement. Cached on first call.
     *
     * @return  <TT>true</TT> if a schema name can be used in a data
     *          manipulation statement.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsSchemasInDataManipulation() throws SQLException;

    /**
     * Retrieves whether a schema name can be used in a table definition
     * statement. Cached on first call.
     *
     * @return  <TT>true</TT> if a schema name can be used in a table
     *          definition statement.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsSchemasInTableDefinitions() throws SQLException;

    /**
     * Retrieves whether this DBMS supports stored procedures. Cached on first
     * call.
     *
     * @return  <TT>true</TT> if DBMS supports stored procedures.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsStoredProcedures() throws SQLException;

    /**
     * Retrieves whether this DBMS supports save points. Cached on first
     * call.
     *
     * @return  <TT>true</TT> if DBMS supports save points.
     * 
     * @throws SQLException if an SQL error occurs.
     */
    boolean supportsSavepoints() throws SQLException;

    /**
     * Retrieves whether this DBMS supports result sets of the specified type. 
     * Cached on first call.
     *
     * @param type the type of the ResultSet.  There are constants defined in 
     *             the ResultSet class that define the different types.
     *             
     * @return  <TT>true</TT> if DBMS supports this type of ResultSet.
     * 
     * @throws SQLException if an SQL error occurs.
     */
    boolean supportsResultSetType(int type) throws SQLException;

    /**
     * Return a string array containing the names of all the catalogs in the
     * database. Cached on first call.
     *
     * @return  String[] of the names of the catalogs in the database.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    String[] getCatalogs() throws SQLException;

    /**
     * Retrieves the URL for this DBMS.
     * 
     * @return  the URL for this DBMS or null if it cannot be generated
     * 
     * @throws SQLException if a database access error occurs
     */
    String getURL() throws SQLException;

    /**
     * Retrieves the database vendor's preferred term for "catalog".
     * 
     * @return the vendor term for "catalog"
     * 
     * @throws SQLException if a database access error occurs
     */
    String getCatalogTerm() throws SQLException;

    /**
     * Retrieves the database vendor's preferred term for "schema".
     * 
     * @return  the vendor term for "schema"
     * 
     * @throws SQLException if a database access error occurs
     */
    String getSchemaTerm() throws SQLException;

    /**
     * Retrieves the database vendor's preferred term for "procedure".
     * 
     * @return the vendor term for "procedure"
     * 
     * @throws SQLException if a database access error occurs
     */
    String getProcedureTerm() throws SQLException;

    /**
     * Retrieves the String that this database uses as the separator between a
     * catalog and table name. Cached on first call.
     *
     * @return  The separator character.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    String getCatalogSeparator() throws SQLException;

    /**
     * Retrieves whether this database supports catalogs at all.
     *
     * @return  <TT>true</TT> fi database supports catalogs.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsCatalogs() throws SQLException;

    /**
     * Retrieves whether a catalog name can be used in a table definition
     * statement. Cached on first call.
     *
     * @return  <TT>true</TT> if a catalog name can be used in a table
     *          definition statement.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsCatalogsInTableDefinitions() throws SQLException;

    /**
     * Retrieves whether a catalog name can be used in a data manipulation
     * statement. Cached on first call.
     *
     * @return  <TT>true</TT> if a catalog name can be used in a data
     *          manipulation statement.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsCatalogsInDataManipulation() throws SQLException;

    /**
     * Retrieves whether a catalog name can be used in a procedure call. Cached
     * on first call.
     *
     * @return  <TT>true</TT> if a catalog name can be used in a procedure
     *          call.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsCatalogsInProcedureCalls() throws SQLException;

    /**
     * Return the <TT>DatabaseMetaData</TT> object for this connection.
     *
     * @return  The <TT>DatabaseMetaData</TT> object for this connection.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    DatabaseMetaData getJDBCMetaData() throws SQLException;

    /**
     * TODO:���Խ���ȡ�洢���
     * NOTE: This method should only be used by SchemaInfo since this class should not and does not cache.
     *
     * Retrieve information about the procedures in the system.
     */
//    IProcedureInfo[] getProcedures(String catalog, String schemaPattern,
//            String procedureNamePattern, ProgressCallBack progressCallBack)
//            throws SQLException;

    /**
     * Return a string array containing the different types of tables in this
     * database. E.G. <TT>"TABLE", "VIEW", "SYSTEM TABLE"</TT>. Cached on first
     * call.
     *
     * @return  table type names.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    String[] getTableTypes() throws SQLException;

    /**
     * TODO:���Խ��
     * Retrieve information about the UDTs in the system.
     *
     * @param   catalog     The name of the catalog to retrieve UDTs
     *                      for. An empty string will return those without a
     *                      catalog. <TT>null</TT> means that the catalog
     *                      will not be used to narrow the search.
     * @param   schemaPattern   The name of the schema to retrieve UDTs
     *                      for. An empty string will return those without a
     *                      schema. <TT>null</TT> means that the schema
     *                      will not be used to narrow the search.
     * @param   typeNamepattern     A type name pattern; must match the
     *                              type name as it is stored in the
     *                              database.
     * @param   types       List of user-defined types (JAVA_OBJECT, STRUCT, or
     *                      DISTINCT) to include; null returns all types
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
//    IUDTInfo[] getUDTs(String catalog, String schemaPattern,
//            String typeNamePattern, int[] types) throws SQLException;

    /**
     * Retrieve the names of the Numeric Functions that this DBMS supports.
     * Cached on first call.
     *
     * @return  String[] of function names.
     */
    String[] getNumericFunctions() throws SQLException;

    /**
     * Retrieve the names of the String Functions that this DBMS supports.
     * Cached on first call.
     *
     * @return  String[] of function names.
     */
    String[] getStringFunctions() throws SQLException;

    /**
     * Retrieve the names of the System Functions that this DBMS supports.
     * Cached on first call.
     *
     * @return  String[] of function names.
     */
    String[] getSystemFunctions() throws SQLException;

    /**
     * Retrieve the names of the Date/Time Functions that this DBMS supports.
     * Cached on first call.
     *
     * @return  String[] of function names.
     */
    String[] getTimeDateFunctions() throws SQLException;

    /**
     * Retrieve the names of the non-standard keywords that this DBMS supports.
     * Cached on first call.
     *
     * @return  String[] of keywords.
     */
    String[] getSQLKeywords() throws SQLException;
    
 
    /**
     * Retrieve whether this driver correctly handles Statement.setMaxRows(int).
     * Some drivers such as version 5.02 of the Opta2000 driver use setMaxRows
     * for UPDATEs, DELETEs etc. instead of just SELECTs. If this method returns
     * <TT>false</TT> then setMaxRows should only be applied to statements
     * that are running SELECTs.
     *
     * @return  <TT>true</TT> if this driver correctly implements setMaxRows().
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean correctlySupportsSetMaxRows() throws SQLException;

    /**
     * Retrieve whether this driver supports multiple result sets. Cached on
     * first call.
     *
     * @return  <tt>true</tt> if driver supports multiple result sets
     *          else <tt>false</tt>.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean supportsMultipleResultSets() throws SQLException;

    /**
     * Retrieves whether this database treats mixed case unquoted SQL
     * identifiers as case insensitive and stores them in upper case.
     * Cached on first call.
     *
     * @return  <tt>true</tt> if driver stores upper case identifiers
     *          else <tt>false</tt>.
     *
     * @throws  SQLException    Thrown if an SQL error occurs.
     */
    boolean storesUpperCaseIdentifiers() throws SQLException;

    boolean storesLowerCaseIdentifiers()throws SQLException;
    /**
     * Clear cache of commonly accessed metadata properties.
     */
    void clearCache();

   
}
