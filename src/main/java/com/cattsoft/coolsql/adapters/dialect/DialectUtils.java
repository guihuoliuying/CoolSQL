
package com.cattsoft.coolsql.adapters.dialect;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.ColumnImpl;
import com.cattsoft.coolsql.sql.model.ForeignKey;
import com.cattsoft.coolsql.sql.model.Index;
import com.cattsoft.coolsql.sql.model.PrimaryKey;
import com.cattsoft.coolsql.sql.model.Table;
import com.cattsoft.coolsql.sql.util.TypesHelper;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * A simple utility class in which to place common code shared amongst the 
 * dialects. Since the dialects all inherit behavior from specific server 
 * dialects, it is not possible to inherit common behavior from a single base 
 * class.  So, this class is where common code is located.
 * 
 * @author manningr
 */
public class DialectUtils {

    /** Logger for this class. */
    private static final Logger log = 
        Logger.getLogger(DialectUtils.class);    
    
    /** Internationalized strings for this class. */
    private static final StringManager s_stringMgr =
        StringManagerFactory.getStringManager(DialectUtils.class);
    
    // alter column clauses
    
    public static final String ALTER_COLUMN_CLAUSE = "ALTER COLUMN";
    
    public static final String MODIFY_COLUMN_CLAUSE = "MODIFY COLUMN";
    
    public static final String MODIFY_CLAUSE = "MODIFY";
    
    public static final String COLUMN_CLAUSE = "COLUMN";
    
    // alter name clauses
    
    public static final String RENAME_COLUMN_CLAUSE = "RENAME COLUMN";
    
    public static final String RENAME_TO_CLAUSE = "RENAME TO";
    
    public static final String TO_CLAUSE = "TO";
    
    // alter default clauses
    
    public static final String DEFAULT_CLAUSE = "DEFAULT";
    
    public static final String SET_DEFAULT_CLAUSE = "SET DEFAULT";
    
    public static final String SET_CLAUSE = "SET";
    
    public static final String ADD_DEFAULT_CLAUSE = "ADD DEFAULT";
    
    public static final String DROP_DEFAULT_CLAUSE = "DROP DEFAULT";
    
    // alter type clauses
    
    public static final String TYPE_CLAUSE = "TYPE";
    
    public static final String SET_DATA_TYPE_CLAUSE = "SET DATA TYPE";
    
    // drop column clauses
    
    public static final String DROP_CLAUSE = "DROP";
    
    public static final String DROP_COLUMN_CLAUSE = "DROP COLUMN";
    
    // cascade constraint clauses
    
    public static final String CASCADE_CLAUSE = "CASCADE";
    
    public static final String CASCADE_CONSTRAINTS_CLAUSE = "CASCADE CONSTRAINTS";
    
    // features
    
    public static final int COLUMN_COMMENT_ALTER_TYPE = 0;
    public static final int COLUMN_DEFAULT_ALTER_TYPE = 1;
    public static final int COLUMN_DROP_TYPE = 2;
    public static final int COLUMN_NAME_ALTER_TYPE = 3;
    public static final int COLUMN_NULL_ALTER_TYPE = 4;
    public static final int COLUMN_TYPE_ALTER_TYPE = 5;
    public static final int ADD_PRIMARY_KEY_TYPE = 6;
    public static final int DROP_PRIMARY_KEY_TYPE = 7;
    
    
    
    /**
     * Returns the SQL statement to use to add a column to the specified table
     * using the information about the new column specified by info.
     * @param info information about the new column such as type, name, etc.
     * @param dialect the HibernateDialect to use to resolve the type
     * @param addDefaultClause whether or not the dialect's SQL supports a 
     *        DEFAULT clause for columns.  
     * @param addNullClause if
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         adding columns after a table has already been created.
     */
    public static String getColumnAddSQL(Column info, 
                                         HibernateDialect dialect,
                                         boolean addDefaultClause,
                                         boolean supportsNullQualifier, 
                                         boolean addNullClause) 
        throws UnsupportedOperationException, HibernateException 
    {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(info.getParentEntity().getQualifiedName());
        result.append(" ");
        result.append(dialect.getAddColumnString().toUpperCase());
        result.append(" ");
        result.append(info.getName());
        result.append(" ");
        result.append(dialect.getTypeName(info.getType(), 
        										(int) info.getSize(), 
                                                (int)info.getSize(), 
                                                info.getNumberOfFractionalDigits()));
        result.append(info.getTypeName());
        
        if (addDefaultClause) {
            appendDefaultClause(info, result);
        }
        if (addNullClause) {
            if (!info.isNullable()) {
                result.append(" NOT NULL ");
            } else {
                if (supportsNullQualifier) {
                    result.append(" NULL ");
                }
            }
        }
        return result.toString();
    }
    
    public static String appendDefaultClause(Column info, 
                                             StringBuilder buffer) {

        if (info.getDefaultValue() != null 
                && !"".equals(info.getDefaultValue())) 
        {
            buffer.append(" DEFAULT ");
            if (TypesHelper.isNumberic(info.getType())) {
                buffer.append(info.getDefaultValue());
            } else {
                buffer.append("'");
                buffer.append(info.getDefaultValue());
                buffer.append("'");                
            }
        }                    
        return buffer.toString();
    }
    
    /**
     * Returns the SQL statement to use to add a comment to the specified 
     * column of the specified table.
     * 
     * @param tableName the name of the table to create the SQL for.
     * @param columnName the name of the column to create the SQL for.
     * @param comment the comment to add.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         annotating columns with a comment.
     */
    public static String getColumnCommentAlterSQL(String tableName, 
                                                  String columnName, 
                                                  String comment) 
    {
        StringBuilder result = new StringBuilder();
        result.append("COMMENT ON COLUMN ");
        result.append(tableName);
        result.append(".");
        result.append(columnName);
        result.append(" IS '");
        if (comment != null && !"".equals(comment)) {
            result.append(comment);
        }
        result.append("'");
        return result.toString();
    }
    
    /**
     * Returns the SQL statement to use to add a comment to the specified 
     * column of the specified table.
     * 
     * @param tableName the name of the table to create the SQL for.
     * @param columnName the name of the column to create the SQL for.
     * @param comment the comment to add.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         annotating columns with a comment.
     */
    public static String getColumnCommentAlterSQL(Column info) {
        return getColumnCommentAlterSQL(info.getParentEntity().getQualifiedName(), 
                                        info.getName(), 
                                        info.getRemarks());
    }
   
    
    /**
     * 
     * @param tableName
     * @param columnName
     * @return
     */
    public static String getColumnDropSQL(String tableName, 
                                          String columnName) {
        return getColumnDropSQL(tableName, columnName, "DROP", false, null);
    }

    /**
     * 
     * @param tableName
     * @param columnName
     * @param addConstraintClause flag indicats whether or not add constraint clause.
     * @param constraintClause --if addConstraintClause is true,this constraintclause will be added to sql.
     * @return
     */
    public static String getColumnDropSQL(String tableName, 
                                          String columnName,
                                          String dropClause, 
                                          boolean addConstraintClause, 
                                          String constraintClause) {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(tableName);
        result.append(" ");
        result.append(dropClause);
        result.append(" ");
        result.append(columnName);
        if (addConstraintClause) {
            result.append(" ");
            result.append(constraintClause);
        }
        return result.toString();
    }
    
    /**
     * Returns the SQL that forms the command to drop the specified table.  If
     * cascade contraints is supported by the dialect and cascadeConstraints is
     * true, then a drop statement with cascade constraints clause will be 
     * formed.
     * 
     * @param Table the table to drop
     * @param supportsCascade whether or not the cascade clause should be added.
     * @param cascadeValue whether or not to drop any FKs that may 
     * reference the specified table.
     * @param supportsMatViews indicat whether or not current database supports materialized view.
     * @param cascadeClause 
     * @param isMatView indicat whether or not current table is materialized view
     * @return the drop SQL command.
     */
    public static List<String> getTableDropSQL(Table Table, 
                                         boolean supportsCascade, 
                                         boolean cascadeValue, 
                                         boolean supportsMatViews, 
                                         String cascadeClause, 
                                         boolean isMatView) 
    {
        StringBuilder result = new StringBuilder();
        if (supportsMatViews && isMatView) {
            result.append("DROP MATERIALIZED VIEW ");
        } else {
            result.append("DROP TABLE ");
        }
        result.append(Table.getQualifiedName());
        if (supportsCascade && cascadeValue) {
            result.append(" ");
            result.append(cascadeClause);
        }
        return Arrays.asList(new String[] { result.toString() });
    }
    
    public static String getTypeName(Column info, 
                                     HibernateDialect dialect) 
    {
        return dialect.getTypeName(info.getType(), 
                                   (int)info.getSize(), 
                                   (int)info.getSize(), 
                                   info.getNumberOfFractionalDigits());
    }
    
    /**
     * Returns the SQL used to alter the specified column to allow/disallow null 
     * values.
     * <br>
     * ALTER TABLE table_name &lt;alterClause&gt; column_name TYPE NULL | NOT NULL
     * <br>
     * ALTER TABLE table_name &lt;alterClause&gt; column_name NULL | NOT NULL
     * 
     * @param info the column to modify
     * @param dialect the HibernateDialect representing the target database.
     * @param alterClause the alter column clause (e.g. ALTER COLUMN )
     * @param specifyType whether or not the column type needs to be specified
     * 
     * @return the SQL to execute
     */
    public static String getColumnNullableAlterSQL(Column info, 
                                                   HibernateDialect dialect,
                                                   String alterClause,
                                                   boolean specifyType) 
    {
        boolean nullable = info.isNullable();
        return getColumnNullableAlterSQL(info, 
                                         nullable, 
                                         dialect, 
                                         alterClause, 
                                         specifyType);
    }
    
    /**
     * Returns the SQL used to alter the specified column to allow/disallow null 
     * values.
     * <br>
     * ALTER TABLE table_name &lt;alterClause&gt; column_name TYPE NULL | NOT NULL
     * <br>
     * ALTER TABLE table_name &lt;alterClause&gt; column_name NULL | NOT NULL
     * 
     * @param info the column to modify
     * @param dialect the HibernateDialect representing the target database.
     * @param alterClause the alter column clause (e.g. ALTER COLUMN )
     * @param specifyType whether or not the column type needs to be specified
     * 
     * @return the SQL to execute
     */
    public static String getColumnNullableAlterSQL(Column info,
                                                   boolean nullable,
                                                   HibernateDialect dialect,
                                                   String alterClause,
                                                   boolean specifyType) 
    {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(info.getParentEntity().getQualifiedName());
        result.append(" ");
        result.append(alterClause);
        result.append(" ");
        result.append(info.getName());
        if (specifyType) {
            result.append(" ");
            result.append(getTypeName(info, dialect));
            result.append(" ");
        }
        if (nullable) { 
            result.append(" NULL");
        } else {
            result.append(" NOT NULL");
        }
        return result.toString();
    }
    
    /**
     * Populates the specified ArrayList with SQL statement(s) required to 
     * convert each of the columns to not null.  This is typically needed in 
     * some databases when adding a primary key (some dbs do this step 
     * automatically)
     * 
     * @param colInfos the columns to be made not null
     * @param dialect 
     * @param result
     */
    public static void getMultiColNotNullSQL(Column[] colInfos,  
                                             HibernateDialect dialect,
                                             String alterClause,
                                             boolean specifyType,
                                             ArrayList<String> result) 
    {
        for (int i = 0; i < colInfos.length; i++) {
            StringBuilder notNullSQL = new StringBuilder();
            notNullSQL.append("ALTER TABLE ");
            notNullSQL.append(colInfos[i].getParentEntity().getQualifiedName());
            notNullSQL.append(" ");
            notNullSQL.append(alterClause);
            notNullSQL.append(" ");
            notNullSQL.append(colInfos[i].getName());
            if (specifyType) {
                notNullSQL.append(" ");
                notNullSQL.append(DialectUtils.getTypeName(colInfos[i], dialect));
            }
            notNullSQL.append(" NOT NULL");
            result.add(notNullSQL.toString());
        }
    }
    
    /**
     * Returns the SQL for creating a primary key consisting of the specified 
     * colInfos.
     *  
     * ALTER TABLE table_name ADD CONSTRAINT pkName PRIMARY KEY (col,...);
     * 
     * or
     * 
     * ALTER TABLE table_name ADD CONSTRAINT PRIMARY KEY (col,...) CONSTRAINT pkName;
     * @param ti --table entity object
     * @param colInfos
     * @param appendConstraintName whether or not the pkName (constraint name) 
     *                             should be placed at the end of the statement.
     *  
     * @return
     */
    public static String getAddPrimaryKeySQL(Table ti, 
                                             String pkName, 
                                             Column[] colInfos, 
                                             boolean appendConstraintName) {
        StringBuilder pkSQL = new StringBuilder();
        pkSQL.append("ALTER TABLE ");
        pkSQL.append(ti.getQualifiedName());
        pkSQL.append(" ADD CONSTRAINT ");
        if (!appendConstraintName) {
            pkSQL.append(pkName);
        }
        pkSQL.append(" PRIMARY KEY ");
        pkSQL.append(getColumnList(colInfos));
        if (appendConstraintName) {
            pkSQL.append(" CONSTRAINT ");
            pkSQL.append(pkName);
        }
        return pkSQL.toString();
    }
    
    /**
     * Returns:
     * 
     *  (column1, column2, ...)
     * 
     * @param colInfos
     * @return
     */
    private static String getColumnList(Column[] colInfos) {
        StringBuilder result = new StringBuilder();
        result.append("(");
        for (int i = 0; i < colInfos.length; i++) {
            result.append(colInfos[i].getName());
            if (i + 1 < colInfos.length) {
                result.append(", ");
            }
        }
        result.append(")");
        return result.toString();
    }
    
    /**
     * Returns the SQL that is used to change the column name.
     * 
     * ALTER TABLE table_name [alterClause] column_name [renameToClause] column_name
     * 
     * @param from the Column as it is
     * @param to the Column as it wants to be
     * 
     * @return the SQL to make the change
     */
    public static String getColumnNameAlterSQL(Column from, 
                                               Column to,
                                               String alterClause,
                                               String renameToClause) 
    {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(from.getParentEntity().getQualifiedName());
        result.append(" ");
        result.append(alterClause);
        result.append(" ");
        result.append(from.getName());
        result.append(" ");
        result.append(renameToClause);
        result.append(" ");
        result.append(to.getName());
        return result.toString();
    }
    
    /**
     * Returns the SQL command to change the specified column's default value
     *   
     * ALTER TABLE table_name ALTER COLUMN column_name [defaultClause] 'defaultVal'  
     * 
     * ALTER TABLE table_name ALTER COLUMN column_name [defaultClause] 1234
     * 
     * @param dialect --database dialect object
     * @param info the column to modify and it's default value.
     * @param specifyType flag indicats whether or not specify the type name of column
     *   
     * @return SQL to make the change
     */
    public static String getColumnDefaultAlterSQL(HibernateDialect dialect,
                                                  Column info,
                                                  String alterClause, 
                                                  boolean specifyType, 
                                                  String defaultClause) {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(info.getParentEntity().getQualifiedName());
        result.append(" ");
        result.append(alterClause);
        result.append(" ");
        result.append(info.getName());
        result.append(" ");
        if (specifyType) {
            result.append(getTypeName(info, dialect));
        }
        result.append(" ");
        result.append(defaultClause);
        result.append(" ");
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
     * Returns the SQL that is used to change the column type.
     * 
     * ALTER TABLE table_name alter_clause column_name [setClause] data_type
     *  
     * ALTER TABLE table_name alter_clause column_name column_name [setClause] data_type
     * 
     * @param from the Column as it is
     * @param to the Column as it wants to be
     * 
     * @return the SQL to make the change
     * @throw UnsupportedOperationException if the database doesn't support 
     *         modifying column types. 
     */
    @SuppressWarnings("unused")
    public static List<String> getColumnTypeAlterSQL(HibernateDialect dialect,
                                                     String alterClause,
                                                     String setClause,
                                                     boolean repeatColumn,
                                                     Column from, 
                                                     Column to)
        throws UnsupportedOperationException
    {
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(to.getParentEntity().getQualifiedName());
        result.append(" ");
        result.append(alterClause);
        result.append(" ");
        if (repeatColumn) {
            result.append(to.getName());
            result.append(" ");
        }
        result.append(to.getName());
        result.append(" ");
        if (setClause != null && !"".equals(setClause)) {
            result.append(setClause);
            result.append(" ");
        }
        result.append(getTypeName(to, dialect));
        list.add(result.toString());
        return list;
    }    
    
    /**
     * Returns the SQL that is used to change the column name.
     * 
     * RENAME COLUMN table_name.column_name TO new_column_name
     * 
     * @param from the Column as it is
     * @param to the Column as it wants to be
     * 
     * @return the SQL to make the change
     */
    public static String getColumnRenameSQL(Column from, 
                                            Column to) {
        StringBuilder result = new StringBuilder();
        result.append("RENAME COLUMN ");
        result.append(from.getParentEntity().getQualifiedName());
        result.append(".");
        result.append(from.getName());
        result.append(" TO ");
        result.append(to.getName());
        return result.toString();
    }
    
    public static String getUnsupportedMessage(HibernateDialect dialect,
                                               int featureId) 
        throws UnsupportedOperationException
    {
        String msg = null;
        switch (featureId) {
            case COLUMN_COMMENT_ALTER_TYPE:
                //i18n[DialectUtils.columnCommentUnsupported={0} doesn''t support
                //column comments]
                msg = s_stringMgr.getString("DialectUtils.columnCommentUnsupported",
                                            dialect.getDisplayName());
                break;
            case COLUMN_DEFAULT_ALTER_TYPE:
                //i18n[DialectUtils.columnDefaultUnsupported={0} doesn''t support
                //altering a column''s default value]
                msg = s_stringMgr.getString("DialectUtils.columnDefaultUnsupported",
                                            dialect.getDisplayName());
                break;                
                
            case COLUMN_DROP_TYPE:
                //i18n[DialectUtils.columnDropUnsupported={0} doesn''t support
                //dropping a column]
                msg = s_stringMgr.getString("DialectUtils.columnDropUnsupported",
                                            dialect.getDisplayName());
                break;                                
            case COLUMN_NAME_ALTER_TYPE:
                //i18n[DialectUtils.columnNameUnsupported={0} doesn''t support 
                //altering a column''s name]
                msg = s_stringMgr.getString("DialectUtils.columnNameUnsupported",
                                            dialect.getDisplayName());
                break;                                
            case COLUMN_NULL_ALTER_TYPE:
                //i18n[DialectUtils.columnNullUnsupported={0} doesn''t support
                //altering a column's nullable attribute]
                msg = s_stringMgr.getString("DialectUtils.columnCommentUnsupported",
                                            dialect.getDisplayName());
                break;
            case COLUMN_TYPE_ALTER_TYPE:
                //i18n[DialectUtils.columnTypeUnsupported={0} doesn''t support
                //altering a column's type attribute]
                msg = s_stringMgr.getString("DialectUtils.columnTypeUnsupported",
                                            dialect.getDisplayName());
                break;
            case ADD_PRIMARY_KEY_TYPE:
                //i18n[DialectUtils.addPrimaryKeyUnsupported={0} doesn''t 
                //support adding primary keys]
                msg = s_stringMgr.getString("DialectUtils.addPrimaryKeyUnsupported",
                                            dialect.getDisplayName());
                break;
            case DROP_PRIMARY_KEY_TYPE:
                //i18n[DialectUtils.dropPrimaryKeyUnsupported={0} doesn''t 
                //support dropping primary keys]
                msg = s_stringMgr.getString("DialectUtils.dropPrimaryKeyUnsupported",
                                            dialect.getDisplayName());
                break;
            default:
                throw new IllegalArgumentException("Unknown featureId: "+featureId);
        }
        return msg;
    }
    
    /**
     * Returns the SQL command to drop the specified table's primary key.
     * 
     * alter table table_name drop primary key
     * 
     * or 
     * 
     * alter table table_name drop constraint [pkName]
     * 
     * @param pkName the name of the primary key that should be dropped
     * @param tableName the name of the table whose primary key should be 
     *                  dropped
     * @param useConstraintName if true, the constraint name is used - like 
     *                          'DROP CONSTRAINT pkName'; otherwise
     *                          a generic 'DROP PRIMARY KEY' is used instead.
     * @param cascadeConstraints whether or not to append 'CASCADE' to the end.
     * @return
     */
    public static String getDropPrimaryKeySQL(String pkName, 
                                              String tableName, 
                                              boolean useConstraintName, 
                                              boolean cascadeConstraints) {
        StringBuilder result = new StringBuilder();
        result.append("ALTER TABLE ");
        result.append(tableName);
        if (useConstraintName) {
            result.append(" DROP CONSTRAINT ");
            result.append(pkName);
        } else {
            result.append(" DROP PRIMARY KEY");
        }
        if (cascadeConstraints) {
            result.append(" CASCADE");
        }
        return result.toString();
    }
    
    /**
     * CREATE UNIQUE INDEX indexName ON tableName (columns);
     * 
     * @param indexName
     * @param tableName
     * @param columns
     * @return
     */
    public static String getAddIndexSQL(String indexName,
                                        boolean unique,
                                        Column[] columns) 
    {
        StringBuilder result = new StringBuilder();
        if (unique) {
            result.append("CREATE UNIQUE INDEX ");
        } else {
            result.append("CREATE INDEX ");
        }
        result.append(indexName);
        result.append(" ON ");
        result.append(columns[0].getParentEntity().getQualifiedName());
        result.append(" ");
        result.append(getColumnList(columns));
        return result.toString();
    }
   
    public static Column getRenamedColumn(Column info,
                                                   String newColumnName) 
    {
    	if(info==null)
    		return null;
        Column result = new ColumnImpl(info.getParentEntity(),
        				newColumnName,info.getTypeName(),
        				info.getType(),info.getSize(),
        				info.getNumberOfFractionalDigits(),
        				info.isNullable(),info.getPosition(),
        				info.getRemarks(),info.getRadix(),info.getOctetLength(),info.getDefaultValue(),info.getNullAllowed());
        return result;
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
    public static String getDropForeignKeySQL(String fkName, String tableName) {
        StringBuilder tmp = new StringBuilder();
        tmp.append("ALTER TABLE ");
        tmp.append(tableName);
        tmp.append(" DROP CONSTRAINT ");
        tmp.append(fkName);
        return tmp.toString();                    
    }
    
    public static List<String> getCreateTableSQL(List<Table> tables,
                                                 ISQLDatabaseMetaData md,
                                                 HibernateDialect dialect,
                                                 CreateScriptPreferences prefs,
                                                 boolean isJdbcOdbc)
        throws SQLException,UnifyException
    {
        List<String> sqls = new ArrayList<String>();
        List<String> allconstraints = new ArrayList<String>();
        
        for (Table ti : tables) {
            StringBuilder result = new StringBuilder();
            String tableName = 
                prefs.isQualifyTableNames()? ti.getQualifiedName() : ti.getName();
            result.append("CREATE TABLE ");
            result.append(tableName);
            result.append("\n(");
            
            PrimaryKey pkInfos = getPrimaryKey(md, ti, isJdbcOdbc);
            List<String> pks = getPKSequenceList(pkInfos);
            Column[] infos = ti.getColumns();
//            	md.getColumnInfo(ti);
            for (Column tcInfo : infos) {
                String columnName = tcInfo.getName();
                int columnSize =(int) tcInfo.getSize();
                int dataType = tcInfo.getType();
                int precision = dialect.getPrecisionDigits(columnSize, dataType);
                String column = dialect.getTypeName(tcInfo.getType(), 
                                                    (int)tcInfo.getSize(),
                                                    precision,
                                                    tcInfo.getNumberOfFractionalDigits()); 
                
                result.append("\n   ");
                result.append(columnName);
                result.append(" ");
                result.append(column);
//                String isNullable = tcInfo.isNullable();
                if (pks.size() == 1 && pks.get(0).equals(columnName))
                {
                   result.append(" PRIMARY KEY");
                }
                if (!tcInfo.isNullable())
                {
                   result.append(" NOT NULL");
                }
                result.append(",");                   
            }
            
            if (pks.size() > 1) {
               result.append("\n   CONSTRAINT ");
               result.append(pkInfos.getPkName());
               result.append(" PRIMARY KEY (");
               for (int i = 0; i < pks.size(); i++)
               {
                  result.append(pks.get(i));
                  result.append(",");
               }
               result.setLength(result.length() - 1);
               result.append("),");
            }
            result.setLength(result.length() - 1);

            result.append("\n)");
            sqls.add(result.toString());
            
            if(isJdbcOdbc) { continue; }

            List<String> constraints = 
                createConstraints(ti, tables, prefs, md);
            addConstraintsSQLs(sqls, allconstraints, constraints, prefs);
            
            List<String> indexes = createIndexes(ti, md, pkInfos);
            addConstraintsSQLs(sqls, allconstraints, indexes, prefs);
        }
        
        if (prefs.isConstraintsAtEnd()) {
            sqls.addAll(allconstraints);
        }
        return sqls;
    }
    
    private static void addConstraintsSQLs(List<String> sqls,
                                           List<String> allconstraints,
                                           List<String> sqlsToAdd,
                                           CreateScriptPreferences prefs) 
    {
        if(sqlsToAdd.size() > 0) {
           if(prefs.isConstraintsAtEnd()) {
               allconstraints.addAll(sqlsToAdd);
           } else {
               sqls.addAll(sqlsToAdd);
           }
        }        
    }
    
    /**
     * Get a list of statements needed to create indexes for the specified table
     * 
     * @param ti
     * @param md
     * @param primaryKeys can be null
     * @return
     */
    public  static List<String> createIndexes(Table ti,
                                              ISQLDatabaseMetaData md,
                                              PrimaryKey primaryKeys) 
    {
        if (ti == null) {
            throw new IllegalArgumentException("ti cannot be null");
        }
        if (md == null) {
            throw new IllegalArgumentException("md cannot be null");
        }
        List<String> result = new ArrayList<String>();
//        if (ti.getDatabaseObjectType() == DatabaseObjectType.VIEW) {
//            return result;
//        }

        List<IndexColInfo> pkCols = new ArrayList<IndexColInfo>();
        if (primaryKeys != null) {
            for (int i=0;i<primaryKeys.getNumberOfColumns();i++) {
               pkCols.add(new IndexColInfo(primaryKeys.getColumn(i)));
            }
            Collections.sort(pkCols, IndexColInfo.NAME_COMPARATOR);
        }
        
//        List<IndexImpl> indexInfos = null;
        Index[] indexInfos=null;
        try {
            indexInfos = ti.getIndexes();
        } catch (SQLException e) {
            String msg = 
                s_stringMgr.getString("DialectUtils.error.getprimarykey", 
                                      ti.getName());
            log.error(msg, e);
            return result;
        } catch (UnifyException e) {
			LogProxy.errorReport(e.getMessage(),log, e);
		}       
        
        // Group all columns by index 
        Hashtable<String, TableIndexInfo> buf = new Hashtable<String, TableIndexInfo>();
        for (Index indexInfo : indexInfos) {
            String indexName = indexInfo.getName();
            if(null == indexName) {
               continue;
            }
//            TableIndexInfo ixi = buf.get(indexName);
//            if(null == ixi)
//            {
               List<IndexColInfo> ixCols = new ArrayList<IndexColInfo>();
               for(int i=0;i<indexInfo.getNumberOfColumns();i++)
            	   ixCols.add(new IndexColInfo(indexInfo.getColumnName(i), 
                                           indexInfo.getOrdinalPosition(i)));
               buf.put(indexName, 
                       new TableIndexInfo(indexInfo.getParentEntity().getName(), 
                                          indexName, 
                                          ixCols,
                                          !indexInfo.isNonUnique()));
//            }
//            else
//            {
//               ixi.cols.add(new IndexColInfo(indexInfo.getColumnName(), 
//                                             indexInfo.getOrdinalPosition()));
//            }           
        }
        
        TableIndexInfo[] ixs = buf.values().toArray(new TableIndexInfo[buf.size()]);
        for (int i = 0; i < ixs.length; i++)
        {
           Collections.sort(ixs[i].cols, IndexColInfo.NAME_COMPARATOR);

           if(pkCols.equals(ixs[i].cols))
           {
              // Serveral DBs automatically create an index for primary key fields
              // and return this index in getIndexInfo(). We remove this index from the script
              // because it would break the script with an index already exists error.
              continue;
           }

           Collections.sort(ixs[i].cols, IndexColInfo.ORDINAL_POSITION_COMPARATOR);

           StringBuilder indexSQL = new StringBuilder();
           indexSQL.append("CREATE");
           indexSQL.append(ixs[i].unique ? " UNIQUE ": " ");
           indexSQL.append("INDEX ");
           indexSQL.append(ixs[i].ixName);
           indexSQL.append(" ON ");
           indexSQL.append(ixs[i].table);

           if (ixs[i].cols.size() == 1) {
               indexSQL.append("(").append(ixs[i].cols.get(0));

               for (int j = 1; j < ixs[i].cols.size(); j++) {
                   indexSQL.append(",").append(ixs[i].cols.get(j));
               }
           } else {
               indexSQL.append("\n(\n");
               for (int j = 0; j < ixs[i].cols.size(); j++) {
                   indexSQL.append("  ");
                   indexSQL.append(ixs[i].cols.get(j));
                   if (j < ixs[i].cols.size() - 1) {
                       indexSQL.append(",\n");
                   } else {
                       indexSQL.append("\n");
                   }
               }
           }
           indexSQL.append(")");
           result.add(indexSQL.toString());
        }
        return result;
    }
    
    private static List<String> createConstraints(Table ti, 
                                                  List<Table> tables, 
                                                  CreateScriptPreferences prefs,
                                                  ISQLDatabaseMetaData md)
        throws SQLException,UnifyException
    {

        List<String> result = new ArrayList<String>();
        StringBuffer sbToAppend = new StringBuffer();

        ConstraintInfo[] cis = getConstraintInfos(ti, md);

        for (int i = 0; i < cis.length; i++) {
            if (!prefs.isIncludeExternalReferences()) {
                boolean found = false;
                for (Table table : tables) {
                    if(table.getName().equalsIgnoreCase(cis[i].pkTable)) {
                        found = true;
                        break;
                    }
                }
                if(false == found) {
                    continue;
                }
            }

            sbToAppend.append("ALTER TABLE " + cis[i].fkTable + "\n");
            sbToAppend.append("ADD CONSTRAINT " + cis[i].fkName + "\n");


            if(cis[i].fkCols.size() == 1)
            {
                sbToAppend.append("FOREIGN KEY (").append(cis[i].fkCols.get(0));

                for (int j = 1; j < cis[i].fkCols.size(); j++)
                {
                    sbToAppend.append(",").append(cis[i].fkCols.get(j));
                }
                sbToAppend.append(")\n");

                sbToAppend.append("REFERENCES " + cis[i].pkTable + "(");
                sbToAppend.append(cis[i].pkCols.get(0));
                for (int j = 1; j < cis[i].pkCols.size(); j++)
                {
                    sbToAppend.append(",").append(cis[i].pkCols.get(j));
                }
            }
            else
            {
                sbToAppend.append("FOREIGN KEY\n");
                sbToAppend.append("(\n");
                for (int j = 0; j < cis[i].fkCols.size(); j++)
                {
                    if(j < cis[i].fkCols.size() -1)
                    {
                        sbToAppend.append("  " + cis[i].fkCols.get(j) + ",\n");
                    }
                    else
                    {
                        sbToAppend.append("  " + cis[i].fkCols.get(j) + "\n");
                    }
                }
                sbToAppend.append(")\n");

                sbToAppend.append("REFERENCES " + cis[i].pkTable + "\n");
                sbToAppend.append("(\n");
                for (int j = 0; j < cis[i].pkCols.size(); j++)
                {
                    if(j < cis[i].pkCols.size() -1)
                    {
                        sbToAppend.append("  " + cis[i].pkCols.get(j) + ",\n");
                    }
                    else
                    {
                        sbToAppend.append("  " + cis[i].pkCols.get(j) + "\n");
                    }
                }
            }

            sbToAppend.append(")");

            if (prefs.isDeleteRefAction()) {
                sbToAppend.append(" ON DELETE ");
                sbToAppend.append(prefs.getRefActionByType(prefs.getDeleteAction()));
            } else {
                switch (cis[i].deleteRule) {
                case DatabaseMetaData.importedKeyCascade:
                    sbToAppend.append(" ON DELETE CASCADE");
                    break;
                case DatabaseMetaData.importedKeySetNull:
                    sbToAppend.append(" ON DELETE SET NULL");
                    break;
                case DatabaseMetaData.importedKeySetDefault:
                    sbToAppend.append(" ON DELETE SET DEFAULT");
                    break;
                case DatabaseMetaData.importedKeyRestrict:
                case DatabaseMetaData.importedKeyNoAction:
                default:
                    sbToAppend.append(" ON DELETE NO ACTION");
                }
            }
            if (prefs.isUpdateRefAction()) {
                sbToAppend.append(" ON UPDATE ");
                sbToAppend.append(prefs.getRefActionByType(prefs.getUpdateAction()));             
            } else {
                switch (cis[i].updateRule) {
                case DatabaseMetaData.importedKeyCascade:
                    sbToAppend.append(" ON UPDATE CASCADE");
                    break;
                case DatabaseMetaData.importedKeySetNull:
                    sbToAppend.append(" ON UPDATE SET NULL");
                    break;
                case DatabaseMetaData.importedKeySetDefault:
                    sbToAppend.append(" ON UPDATE SET DEFAULT");
                    break;
                case DatabaseMetaData.importedKeyRestrict:
                case DatabaseMetaData.importedKeyNoAction:
                default:
                    sbToAppend.append(" ON UPDATE NO ACTION");
                }             
            }
            sbToAppend.append("\n");
            result.add(sbToAppend.toString());
        }

        return result;
    }
    
    private static ConstraintInfo[] getConstraintInfos(Table ti, 
                                                       ISQLDatabaseMetaData md) 
        throws SQLException ,UnifyException
    {
        Hashtable<String, ConstraintInfo> buf = 
            new Hashtable<String, ConstraintInfo>();
        ForeignKey[] fkinfos = ti.getImportedKeys();
        for (ForeignKey fkinfo : fkinfos) {
            ConstraintInfo ci = buf.get(fkinfo.getFkName());

//            if(null == ci)
//            {
            Vector<String> fkCols = new Vector<String>();
            Vector<String> pkCols = new Vector<String>();
            for(int i=0;i<fkinfo.getNumberOfColumns();i++)
            {

               fkCols.add(fkinfo.getForeignColumnName(i));
               pkCols.add(fkinfo.getLocalColumnName(i));
               
            }
            ci = new ConstraintInfo(fkinfo.getForeignEntityName(), 
                    fkinfo.getLocalEntityName(), 
                    fkinfo.getFkName(), 
                    fkCols, 
                    pkCols,
                    (short)fkinfo.getDeleteRule(),
                    (short)fkinfo.getUpdateRule());
            buf.put(fkinfo.getFkName(), ci);
//            }
//            else
//            {
//               ci.fkCols.add(fkinfo.getForeignKeyColumnName());
//               ci.pkCols.add(fkinfo.getPrimaryKeyColumnName());
//            }
            
        }
        return buf.values().toArray(new ConstraintInfo[buf.size()]);
    }
    
    
    private static PrimaryKey getPrimaryKey(ISQLDatabaseMetaData md, 
                                                          Table ti,
                                                          boolean isJdbcOdbc) 
    {
        if (isJdbcOdbc) {
            return null;
        }
        try {
			return ti.getPrimaryKey();
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(e);
		} catch (UnifyException e) {
			LogProxy.errorReport(e.getMessage(), e);
		}
		return null;
    }
    
    private static List<String> getPKSequenceList(PrimaryKey info) {
        String[] result = new String[info.getNumberOfColumns()];
        for (int i=0;i<result.length;i++) {
            int iKeySeq = info.getKeySequence(i) - 1;
            result[iKeySeq] = info.getColumn(i);
        }
        return Arrays.asList(result);
    }
    
    
    
}
