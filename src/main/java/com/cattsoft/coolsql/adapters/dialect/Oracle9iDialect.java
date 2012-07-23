package com.cattsoft.coolsql.adapters.dialect;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.dialect.Oracle9Dialect;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.Table;
import com.cattsoft.coolsql.sql.util.TypesHelper;

/**
 * A description of this class goes here...
 */

public class Oracle9iDialect extends Oracle9Dialect 
                             implements HibernateDialect {

    public Oracle9iDialect() {
        super();
        registerColumnType(Types.BIGINT, "number($p)");
        registerColumnType(Types.BINARY, 2000, "raw($l)");
        registerColumnType(Types.BINARY, "blob");
        registerColumnType(Types.BIT, "smallint");
        registerColumnType(Types.BLOB, "blob");
        registerColumnType(Types.BOOLEAN, "smallint");
        registerColumnType(Types.CHAR, 2000, "char($l)");
        registerColumnType(Types.CHAR, 4000, "varchar2($l)");
        registerColumnType(Types.CHAR, "clob");
        registerColumnType(Types.CLOB, "clob");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.DECIMAL, "decimal($p)");
        registerColumnType(Types.DOUBLE, "float($p)");
        registerColumnType(Types.FLOAT, "float($p)");
        registerColumnType(Types.INTEGER, "int");        
        registerColumnType(Types.LONGVARBINARY, "blob");
        registerColumnType(Types.LONGVARCHAR, 4000, "varchar2($l)");
        registerColumnType(Types.LONGVARCHAR, "clob");
        registerColumnType(Types.NUMERIC, "number($p)");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.TIME, "date");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.TINYINT, "smallint");
        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.VARCHAR, 4000, "varchar2($l)");
        registerColumnType(Types.VARCHAR, "clob");
        // Total Hack!  Type OTHER(1111) can be other types as well?
        registerColumnType(Types.OTHER, 4000, "varchar2(4000)");
        registerColumnType(Types.OTHER, "clob");
        
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
        if (dataType == Types.DOUBLE
                || dataType == Types.FLOAT)
        {
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
        return "Oracle";
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
    	if (databaseProductName.trim().toLowerCase().startsWith("oracle")) {
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
        if (info.getRemarks() != null && !"".equals(info.getRemarks())) {
            return new String[] {
                DialectUtils.getColumnAddSQL(info, this, true, true, true),
                DialectUtils.getColumnCommentAlterSQL(info)
            };            
        } else {
            return new String[] {
                DialectUtils.getColumnAddSQL(info, this, true, true, true)
            };            
        }
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
     * @param tableName the name of the table to create the SQL for.
     * 
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         annotating columns with a comment.
     */
    public String getColumnCommentAlterSQL(Column info) 
        throws UnsupportedOperationException 
    {
        return DialectUtils.getColumnCommentAlterSQL(info);
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
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(tableName);
        result.append(" DROP COLUMN ");
        result.append(columnName);
        return result.toString();
    }
        
    /**
     * Returns the SQL that forms the command to drop the specified table.  If
     * cascade contraints is supported by the dialect and cascadeConstraints is
     * true, then a drop statement with cascade constraints clause will be 
     * formed.
     * @param cascadeConstraints whether or not to drop any FKs that may 
     * reference the specified table.
     * @param ti the table to drop
     * 
     * @return the drop SQL command.
     */
    public List<String> getTableDropSQL(Table ti, 
                                  boolean cascadeConstraints, 
                                  boolean isMaterializedView)
    {
        String cascadeClause = "";
        if (!isMaterializedView) {
            cascadeClause = DialectUtils.CASCADE_CONSTRAINTS_CLAUSE;            
        }

        return DialectUtils.getTableDropSQL(ti,  
                                            true, 
                                            cascadeConstraints, 
                                            true, 
                                            cascadeClause, 
                                            isMaterializedView);
    }
    
    /**
     * Returns the SQL that forms the command to add a primary key to the 
     * specified table composed of the given column names.
     * @param columns the columns that form the key
     * @return
     */
    public String[] getAddPrimaryKeySQL(String pkName,
                                        Column[] columns, Table ti) {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(ti.getQualifiedName());
        result.append(" ADD CONSTRAINT ");
        result.append(pkName);
        result.append(" PRIMARY KEY (");
        for (int i = 0; i < columns.length; i++) {
            result.append(columns[i].getName());
            if (i + 1 < columns.length) {
                result.append(", ");
            }
        }
        result.append(")");
        return new String[] { result.toString() };
    }
    
    /**
     * Returns the SQL used to alter the specified column to allow/disallow null 
     * values, based on the value of isNullable.
     * 
     * alter table test modify mycol not null
     * 
     * @param info the column to modify
     * @return the SQL to execute
     */
    public String getColumnNullableAlterSQL(Column info) {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(info.getParentEntity().getQualifiedName());
        result.append(" MODIFY ");
        result.append(info.getName());
        if (info.isNullable()) {
            result.append(" NULL");
        } else {
            result.append(" NOT NULL");
        }
        return result.toString();
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
     * @param from the Column as it is
     * @param to the Column as it wants to be
     * 
     * @return the SQL to make the change
     */
    public String getColumnNameAlterSQL(Column from, 
                                        Column to) {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(from.getParentEntity().getQualifiedName());
        result.append(" RENAME COLUMN ");
        result.append(from.getName());
        result.append(" TO ");
        result.append(to.getName());
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
     * alter table test modify (mycol varchar(100))
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
        ArrayList<String> result = new ArrayList<String>();
        
        // Oracle won't allow in-place conversion between CLOB and VARCHAR 
        if ( (from.getType() == Types.VARCHAR && to.getType() == Types.CLOB)
                || (from.getType() == Types.CLOB && to.getType() == Types.VARCHAR) ) 
        {
            // add <columnName>_2 null as CLOB
            Column newInfo = 
                DialectUtils.getRenamedColumn(to, to.getName()+"_2");
            
            String[] addSQL = this.getColumnAddSQL(newInfo);
            for (int i = 0; i < addSQL.length; i++) {
                result.add(addSQL[i]);
            }
            
            // update table set <columnName>_2 = <columnName>
            StringBuilder updateSQL = new StringBuilder();
            updateSQL.append("update ");
            updateSQL.append(from.getParentEntity().getQualifiedName());
            updateSQL.append(" set ");
            updateSQL.append(newInfo.getName());
            updateSQL.append(" = ");
            updateSQL.append(from.getName());
            result.add(updateSQL.toString());
            
            // drop <columnName>
            String dropSQL = 
                getColumnDropSQL(from.getParentEntity().getQualifiedName(), from.getName());
            result.add(dropSQL);
            
            // rename <columnName>_2 to <columnName>
            String renameSQL = this.getColumnNameAlterSQL(newInfo, to);
            result.add(renameSQL);
        } 
        else 
        {
            StringBuffer tmp = new StringBuffer();
            tmp.append("ALTER TABLE ");
            tmp.append(from.getParentEntity().getQualifiedName());
            tmp.append(" MODIFY (");
            tmp.append(from.getName());
            tmp.append(" ");
            tmp.append(DialectUtils.getTypeName(to, this));
            tmp.append(")");
            result.add(tmp.toString());
        }
        return result;
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
     * Returns the SQL command to change the specified column's default value
     *
     * alter table test modify mychar default 'foo'
     * 
     * alter table test modify nullint default 0   
     *   
     * @param info the column to modify and it's default value.
     * @return SQL to make the change
     */
    public String getColumnDefaultAlterSQL(Column info) {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(info.getParentEntity().getQualifiedName());
        result.append(" MODIFY ");
        result.append(info.getName());
        result.append(" DEFAULT ");
        if (TypesHelper.isNumberic(info.getType())) {
            result.append(info.getDefaultValue());
        } else {
            result.append("'");
            result.append(info.getDefaultValue());
            result.append("'");
        }
        return result.toString();
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
        return DialectUtils.getDropForeignKeySQL(fkName, tableName);
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
