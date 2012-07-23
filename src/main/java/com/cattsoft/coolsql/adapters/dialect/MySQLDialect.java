package com.cattsoft.coolsql.adapters.dialect;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.Table;

/**
 * An extension to the standard Hibernate MySQL dialect
 */

public class MySQLDialect extends org.hibernate.dialect.MySQLDialect 
                          implements HibernateDialect {
    
    public MySQLDialect() {
        super();
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.BINARY, 255, "binary($l)");
        registerColumnType(Types.BINARY, 65532, "blob");
        registerColumnType(Types.BINARY, "longblob");
        registerColumnType(Types.BIT, "bit");
        registerColumnType(Types.BLOB, 65532, "blob");
        registerColumnType(Types.BLOB, "longblob");
        registerColumnType(Types.BOOLEAN, "bool");
        registerColumnType(Types.CHAR, 255, "char($l)");
        registerColumnType(Types.CHAR, 65532, "text");
        registerColumnType(Types.CHAR, "longtext");
        registerColumnType(Types.CLOB, "longtext");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.DECIMAL, "decimal($p,$s)");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.FLOAT, "float($p)");
        registerColumnType(Types.INTEGER, "int");        
        registerColumnType(Types.LONGVARBINARY, "longblob");
        registerColumnType(Types.LONGVARCHAR, "longtext");
        registerColumnType(Types.NUMERIC, "numeric($p,$s)");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.VARBINARY, 255, "varbinary($l)");
        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.VARCHAR, "text");        
    }    
    
    
    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#supportsSchemasInTableDefinition()
     */
    public boolean supportsSchemasInTableDefinition() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getLengthFunction()
     */
    public String getLengthFunction(int dataType) {
        return "length";
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getMaxFunction()
     */
    public String getMaxFunction() {
        return "max";
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getMaxPrecision(int)
     */
    public int getMaxPrecision(int dataType) {
        if (dataType == Types.FLOAT) {
            return 53;
        } else {
            return 38;
        }
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getMaxScale(int)
     */
    public int getMaxScale(int dataType) {
        return getMaxPrecision(dataType);
    }
    
    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getPrecisionDigits(int, int)
     */
    public int getPrecisionDigits(int columnSize, int dataType) {
        return columnSize;
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getColumnLength(int, int)
     */
    public int getColumnLength(int columnSize, int dataType) {
        return columnSize;
    }
   
    /**
     * The string which identifies this dialect in the dialect chooser.
     * 
     * @return a descriptive name that tells the user what database this dialect
     *         is design to work with.
     */
    public String getDisplayName() {
        return "MySQL";
    }    

    /**
     * Returns boolean value indicating whether or not this dialect supports the
     * specified database product/version.
     * 
     * @param databaseProductName the name of the database as reported by 
     * 							  DatabaseMetaData.getDatabaseProductName()
     * @param databaseProductVersion the version of the database as reported by
     *                              DatabaseMetaData.getDatabaseProductVersion()
     * @return true if this dialect can be used for the specified product name
     *              and version; false otherwise.
     */
    public boolean supportsProduct(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().toLowerCase().startsWith("mysql")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}    
    
    /**
     * Returns the SQL statement to use to add a column to the specified table
     * using the information about the new column specified by info.
     * @param info information about the new column such as type, name, etc.
     * 
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         adding columns after a table has already been created.
     */
    public String[] getColumnAddSQL(Column info) throws UnsupportedOperationException {
        ArrayList<String> returnVal = new ArrayList<String>();
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(info.getParentEntity().getQualifiedName());
        result.append(" ADD COLUMN ");
        result.append(info.getName());
        result.append(" ");
        result.append(DialectUtils.getTypeName(info, this));
        result.append(" ");
        DialectUtils.appendDefaultClause(info, result);
        if (info.getRemarks() != null && !"".equals(info.getRemarks())) {
            result.append(" COMMENT ");
            result.append("'");
            result.append(info.getRemarks());
            result.append("'");
        }
        returnVal.add(result.toString());
        if (!info.isNullable()) {
            String setNullSQL = 
                getModifyColumnNullabilitySQL(info.getParentEntity().getQualifiedName(), info, false);
            returnVal.add(setNullSQL);
        } 
        // Sometimes, MySQL omits the change for COMMENT, so explicitly add
        // it in a separate alter statement as well
        if (info.getRemarks() != null && !"".equals(info.getRemarks())) 
        {
            returnVal.add(getColumnCommentAlterSQL(info));
        }        
        // Sometimes, MySQL omits the change for DEFAULT, so explicitly add
        // it in a separate alter statement as well
        //returnVal.add()
        if (info.getDefaultValue() != null 
                && !"".equals(info.getDefaultValue()))
        {
            returnVal.add(getColumnDefaultAlterSQL(info));
        }   
        
        return returnVal.toArray(new String[returnVal.size()]);
    }

    public String getModifyColumnNullabilitySQL(String tableName, 
                                                Column info,
                                                boolean nullable) 
    {
        StringBuilder result = new StringBuilder();
        result.append(" ALTER TABLE ");
        result.append(tableName);
        result.append(" MODIFY ");
        result.append(info.getName());
        result.append(" ");
        result.append(DialectUtils.getTypeName(info, this));
        if (nullable) {
            result.append(" NULL ");
        } else {
            result.append(" NOT NULL ");
        }
        return result.toString();
    }
    
    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports changing a column's default value.
     * 
     * @return true if the database supports modifying column defaults; false 
     *         otherwise
     */
    public boolean supportsAlterColumnDefault() {
        return true;
    }
    
    /**
     * Returns SQL statement used to add the default value of the specified 
     * column.
     * 
     * @param info
     * @return
     * @throws UnsupportedOperationException
     */
    public String getColumnDefaultAlterSQL(Column info)
        throws UnsupportedOperationException 
    {   
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(info.getParentEntity().getQualifiedName());
        result.append(" MODIFY ");
        result.append(info.getName());
        result.append(" ");
        result.append(DialectUtils.getTypeName(info, this));
        DialectUtils.appendDefaultClause(info, result);
        return result.toString();
    }
    
    /**
     * Returns a boolean value indicating whether or not this dialect supports
     * adding comments to columns.
     * 
     * @return true if column comments are supported; false otherwise.
     */
    public boolean supportsColumnComment() {
        return true;
    }
    
    /**
     * Returns the SQL statement to use to add a comment to the specified 
     * column of the specified table.
     * @param info information about the column such as type, name, etc.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         annotating columns with a comment.
     */
    public String getColumnCommentAlterSQL(Column info) 
        throws UnsupportedOperationException 
    {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(info.getParentEntity().getQualifiedName());
        result.append(" MODIFY ");
        result.append(info.getName());
        result.append(" ");
        result.append(DialectUtils.getTypeName(info, this));
        result.append(" COMMENT '");
        result.append(info.getRemarks());
        result.append("'");
        return result.toString();
    }
    
    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports dropping columns from tables.
     * 
     * @return true if the database supports dropping columns; false otherwise.
     */
    public boolean supportsDropColumn() {
        return true;
    }

    /**
     * Returns the SQL that forms the command to drop the specified colum in the
     * specified table.
     * 
     * @param tableName the name of the table that has the column
     * @param columnName the name of the column to drop.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         dropping columns. 
     */
    public String getColumnDropSQL(String tableName, String columnName) {        
        return DialectUtils.getColumnDropSQL(tableName, columnName);
    }
    
    /**
     * Returns the SQL that forms the command to drop the specified table.  If
     * cascade contraints is supported by the dialect and cascadeConstraints is
     * true, then a drop statement with cascade constraints clause will be 
     * formed.
     * 
     * @param Table the table to drop
     * @param cascadeConstraints whether or not to drop any FKs that may 
     * reference the specified table.
     * @return the drop SQL command.
     */
    public List<String> getTableDropSQL(Table Table, boolean cascadeConstraints, boolean isMaterializedView){
        return DialectUtils.getTableDropSQL(Table, true, cascadeConstraints, false, DialectUtils.CASCADE_CLAUSE, false);
    }
    
    /**
     * Returns the SQL that forms the command to add a primary key to the 
     * specified table composed of the given column names.
     * @param tableName the table to add a Primary Key to.
     * @param columnNames the columns that form the key
     * 
     * @return
     */
    public String[] getAddPrimaryKeySQL(String pkName, 
                                        Column[] colInfos, Table ti) 
    {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(ti.getQualifiedName());
        result.append(" ADD CONSTRAINT ");
        result.append(pkName);
        result.append(" PRIMARY KEY (");
        for (int i = 0; i < colInfos.length; i++) {
            result.append(colInfos[i].getName());
            if (i + 1 < colInfos.length) {
                result.append(", ");
            }
        }
        result.append(")");
        return new String[] { result.toString() };
    }
    
    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports changing a column from null to not-null and vice versa.
     * 
     * @return true if the database supports dropping columns; false otherwise.
     */    
    public boolean supportsAlterColumnNull() {
        return true;
    }
        
    /**
     * Returns the SQL used to alter the specified column to not allow null 
     * values
     * 
     * ALTER TABLE testdate MODIFY mydate date NOT NULL;
     * 
     * @param info the column to modify
     * @return the SQL to execute
     */
    public String getColumnNullableAlterSQL(Column info) {
        String alterClause = DialectUtils.MODIFY_COLUMN_CLAUSE;
        return DialectUtils.getColumnNullableAlterSQL(info, 
                                                      this, 
                                                      alterClause, 
                                                      true); 
    }

    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports renaming columns.
     * 
     * @return true if the database supports changing the name of columns;  
     *         false otherwise.
     */
    public boolean supportsRenameColumn() {
        return true;
    }
    
    /**
     * Returns the SQL that is used to change the column name.
     * 
     * ALTER TABLE t1 CHANGE a b INTEGER;
     * 
     * @param from the Column as it is
     * @param to the Column as it wants to be
     * 
     * @return the SQL to make the change
     */
    public String getColumnNameAlterSQL(Column from, Column to) {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(from.getParentEntity().getQualifiedName());
        result.append(" CHANGE ");
        result.append(from.getName());
        result.append(" ");
        result.append(to.getName());
        result.append(" ");
        result.append(DialectUtils.getTypeName(from, this));
        return result.toString();
    }
    
    /**
     * Returns a boolean value indicating whether or not this dialect supports 
     * modifying a columns type.
     * 
     * @return true if supported; false otherwise
     */
    public boolean supportsAlterColumnType() {
        return true;
    }
    
    /**
     * Returns the SQL that is used to change the column type.
     * 
     * ALTER TABLE t1 CHANGE b b BIGINT NOT NULL;
     * 
     * @param from the Column as it is
     * @param to the Column as it wants to be
     * 
     * @return the SQL to make the change
     * @throw UnsupportedOperationException if the database doesn't support 
     *         modifying column types. 
     */
    public List<String> getColumnTypeAlterSQL(Column from, 
                                        Column to)
        throws UnsupportedOperationException
    {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(from.getParentEntity().getQualifiedName());
        result.append(" CHANGE ");
        // Always use "to" column name since name changes happen first
        result.append(to.getName());
        result.append(" ");
        result.append(to.getName());
        result.append(" ");
        result.append(DialectUtils.getTypeName(to, this));
        ArrayList<String> list = new ArrayList<String>();
        list.add(result.toString());
        return list;
    }
    
    /**
     * Returns the SQL command to drop the specified table's primary key.
     * 
     * @param pkName the name of the primary key that should be dropped
     * @param tableName the name of the table whose primary key should be 
     *                  dropped
     * @return
     */
    public String getDropPrimaryKeySQL(String pkName, String tableName) {
        return DialectUtils.getDropPrimaryKeySQL(pkName, tableName, false, false);
    }
    
    /**
     * Returns the SQL command to drop the specified table's foreign key 
     * constraint.
     * 
     * @param fkName the name of the foreign key that should be dropped
     * @param tableName the name of the table whose foreign key should be 
     *                  dropped
     * @return
     */
    public String getDropForeignKeySQL(String fkName, String tableName) {
        StringBuilder tmp = new StringBuilder();
        tmp.append("ALTER TABLE ");
        tmp.append(tableName);
        tmp.append(" DROP FOREIGN KEY ");
        tmp.append(fkName);
        return tmp.toString();
    }
    
    /**
     * Returns the SQL command to create the specified table.
     * 
     * @param tables the tables to get create statements for
     * @param md the metadata from the ISession
     * @param prefs preferences about how the resultant SQL commands should be 
     *              formed.
     * @param isJdbcOdbc whether or not the connection is via JDBC-ODBC bridge.
     *  
     * @return the SQL that is used to create the specified table
     * @throws UnifyException 
     */
    public List<String> getCreateTableSQL(List<Table> tables, 
                                          ISQLDatabaseMetaData md,
                                          CreateScriptPreferences prefs,
                                          boolean isJdbcOdbc)
        throws SQLException, UnifyException
    {
        return DialectUtils.getCreateTableSQL(tables, md, this, prefs, isJdbcOdbc);
    }
    
}
