package com.cattsoft.coolsql.sql;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.cattsoft.coolsql.adapters.AdapterFactory;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.model.Schema;
import com.cattsoft.coolsql.sql.model.Table;
/**
 * This class represents the metadata for a database. It is essentially
 * a wrapper around <TT>java.sql.DatabaseMetaData</TT>.
 *
 * <P>Some data can be cached on the first retrieval in order to speed up
 * subsequent retrievals. To clear this cache call <TT>clearCache()</TT>.
 *
 * <P>From the JavaDoc for <TT>java.sql.DatabaseMetaData</TT>. &quot;Some
 * methods take arguments that are String patterns. These arguments all
 * have names such as fooPattern. Within a pattern String, "%" means match any
 * substring of 0 or more characters, and "_" means match any one character. Only
 * metadata entries matching the search pattern are returned. If a search pattern
 * argument is set to null, that argument's criterion will be dropped from the
 * search.&quot;
 *
 * <P>Additionally, it should be noted that some JDBC drivers (like Oracle) do
 * not handle multi-threaded access to methods that return ResultSets very well.
 * It is therefore highly recommended that methods in this class that return 
 * a ResultSet, should not be called outside of this class where this class' 
 * monitor has no jurisdiction.  Furthermore, methods that are meant to be 
 * called externally that create a ResultSet should package the data in some 
 * container object structure for use by the caller, and should always be 
 * synchronized on this class' monitor. 
 * 
 */
public class SQLDatabaseMetaData implements ISQLDatabaseMetaData
{

	/** Logger for this class. */
	private final static Logger logger =
		org.apache.log4j.Logger.getLogger(SQLDatabaseMetaData.class);

	private Bookmark bookmark;
	/** Connection to database this class is supplying information for. */
	private Connection _conn;

	/**
	 * Cache of commonly accessed metadata properties keyed by the method
	 * name that attempts to retrieve them.
    * Note, this cache should only be used for metadata that are not
    * likely to be changed during an open Session.
    * Meta data that is likely to be changed should be kept in SchemaInfo.
	 */
	private Map<String, Object> _cache = 
        Collections.synchronizedMap(new HashMap<String, Object>());

    /**
     * If previous attempts to getSuperTables fail, then this will be set to 
     * false, and prevent further attempts.
     */
//    private boolean supportsSuperTables = true;
    
	/**
	 * ctor specifying the connection that we are retrieving metadata for.
	 *
	 * @param	conn	Connection to database.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown if null SQLConnection passed.
	 */
	SQLDatabaseMetaData(Bookmark bookmark)
	{
		super();
		if (bookmark == null)
		{
			throw new IllegalArgumentException("SQLDatabaseMetaData == null");
		}
		try {
			_conn = bookmark.getConnection();
		} catch (UnifyException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getUserName()
     */
    public synchronized String getUserName() throws SQLException
	{
		final String key = "getUserName";
		String value = (String)_cache.get(key);
		if (value == null)
		{
			value = privateGetJDBCMetaData().getUserName();
			_cache.put(key, value);
		}
		return value;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getDatabaseProductName()
     */
    public synchronized String getDatabaseProductName()
		throws SQLException
	{
		final String key = "getDatabaseProductName";
		String value = (String)_cache.get(key);
		if (value == null)
		{
			value = privateGetJDBCMetaData().getDatabaseProductName();
            _cache.put(key, value);
		}
		return value;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getDatabaseProductVersion()
     */
	public synchronized String getDatabaseProductVersion()
		throws SQLException
	{
		final String key = "getDatabaseProductVersion";
		String value = (String)_cache.get(key);
		if (value == null)
		{
			value = privateGetJDBCMetaData().getDatabaseProductVersion();
			_cache.put(key, value);
		}
		return value;
	}

    public synchronized int getDatabaseMajorVersion() 
        throws SQLException
    {
        final String key = "getDatabaseMajorVersion";
        Integer value = (Integer)_cache.get(key);
        if (value == null)
        {
            value = privateGetJDBCMetaData().getDatabaseMajorVersion();
            _cache.put(key, value);
        }
        return value;
    }
    
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getDriverName()
     */
	public synchronized String getDriverName() throws SQLException
	{
		final String key = "getDriverName";
		String value = (String)_cache.get(key);
		if (value == null)
		{
			value = privateGetJDBCMetaData().getDriverName();
			_cache.put(key, value);
		}
		return value;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getJDBCVersion()
     */
    public int getJDBCVersion() throws SQLException
	{
		final String key = "getJDBCVersion";
		Integer value = (Integer)_cache.get(key);
		if (value == null)
		{
			DatabaseMetaData md = privateGetJDBCMetaData();
            int major = md.getJDBCMajorVersion();
            int minor = md.getJDBCMinorVersion();
            int vers = (major * 100) + minor;
            value = Integer.valueOf(vers);
            _cache.put(key, value);
		}
		return value.intValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getIdentifierQuoteString()
     */
	public synchronized String getIdentifierQuoteString() throws SQLException
	{
		final String key = "getIdentifierQuoteString";
		String value = (String)_cache.get(key);
		if (value == null)
		{
			value = privateGetJDBCMetaData().getIdentifierQuoteString();
			if (value == null) {
			    value = "";
            }
			_cache.put(key, value);
		}
		return value;
	}
	/**
	 * get all schemas of current database by databasemetadata object 
	 */
	public synchronized Schema[] getSchemas() throws SQLException
	{
//		final String key = "getSchemas";
//		Schema[] value = (Schema[])_cache.get(key);
//		if (value != null)
//		{
//			return value;
//		}
		Schema[] value = null;
		final ArrayList<Schema> list = new ArrayList<Schema>();
		
		if(!supportsSchemas())  //��֧��ģʽʱ������һ�������ģʽ��ģʽ��Ϊnull�����Ǵ��ڷ�����ݣ�
		{
			String[] catalogs=getCatalogs();
			Schema[] schemas=new Schema[catalogs.length];
			for(int i=0;i<catalogs.length;i++)
			{
				schemas[i]=new Schema(catalogs[i],null,false);
			}
			return schemas;
		}
		
        ResultSet resultSet = privateGetJDBCMetaData().getSchemas();
        try {
        	String userName=getUserName();
        	int columnCount=resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
            	
            	String catalogName = null;
            	if(columnCount!=1)
            		catalogName=resultSet.getString("TABLE_CATALOG");
                String schemaName = resultSet.getString("TABLE_SCHEM");
                list.add(new Schema(catalogName,schemaName,userName.equals(schemaName)));
            }
            
        } finally {
            resultSet.close();
        }
		value = (Schema[])list.toArray(new Schema[list.size()]);
//		_cache.put(key, value);

		return value;
	}

    public boolean supportsSchemas() throws SQLException
	{
		return supportsSchemasInDataManipulation()
				|| supportsSchemasInTableDefinitions();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsSchemasInDataManipulation()
     */
    public synchronized boolean supportsSchemasInDataManipulation()
		throws SQLException
	{
		final String key = "supportsSchemasInDataManipulation";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		try
		{
			value = Boolean.valueOf(privateGetJDBCMetaData().supportsSchemasInDataManipulation());
		}
		catch (SQLException ex)
		{
            boolean isSQLServer = 
                AdapterFactory.isSyBase(bookmark) || AdapterFactory.isMSSQLServer(bookmark);
            
			if (isSQLServer)
			{
				value = Boolean.TRUE;
                _cache.put(key, value);
			}
			throw ex;
		}

		_cache.put(key, value);

		return value.booleanValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsSchemasInTableDefinitions()
     */
	public synchronized boolean supportsSchemasInTableDefinitions()
		throws SQLException
	{
		final String key = "supportsSchemasInTableDefinitions";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		try
		{
			value = Boolean.valueOf(privateGetJDBCMetaData().supportsSchemasInTableDefinitions());
		}
		catch (SQLException ex)
		{
            boolean isSQLServer = 
                AdapterFactory.isSyBase(bookmark) || AdapterFactory.isMSSQLServer(bookmark);            
			if (isSQLServer)
			{
				value = Boolean.TRUE;
                _cache.put(key, value);
			}
			throw ex;
		}

		_cache.put(key, value);

		return value.booleanValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsStoredProcedures()
     */
    public synchronized boolean supportsStoredProcedures() throws SQLException
	{
		final String key = "supportsStoredProcedures";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		// PostgreSQL (at least 7.3.2) returns false for
		// supportsStoredProcedures() even though it does support them.
		
		if (AdapterFactory.isPostgreSQL(bookmark))
		{
			value = Boolean.TRUE;
		}
		else
		{
			value = Boolean.valueOf(privateGetJDBCMetaData().supportsStoredProcedures());
		}
		_cache.put(key, value);

		return value.booleanValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsSavepoints()
     */
    public synchronized boolean supportsSavepoints() throws SQLException {
        
        final String key = "supportsSavepoints";
        Boolean value = (Boolean)_cache.get(key);
        if (value != null)
        {
            return value.booleanValue();
        }
        value = Boolean.valueOf(privateGetJDBCMetaData().supportsSavepoints());

        _cache.put(key, value);

        return value.booleanValue();        
    }
    
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsResultSetType(int)
     */
    public synchronized boolean supportsResultSetType(int type) 
        throws SQLException
    {
        final String key = "supportsResultSetType";
        Boolean value = (Boolean)_cache.get(key);
        if (value != null)
        {
            return value.booleanValue();
        }
        value = Boolean.valueOf(privateGetJDBCMetaData().supportsResultSetType(type));

        _cache.put(key, value);

        return value.booleanValue();                
    }

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getURL()
     */
    public synchronized String getURL() throws SQLException {
        final String key = "getURL";
        String value = (String)_cache.get(key);
        if (value != null) {
            return value;
        }
        
        value = privateGetJDBCMetaData().getURL();
        _cache.put(key, value);
        
        return value;
    }    
    
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getCatalogTerm()
     */
    public synchronized String getCatalogTerm() throws SQLException {
        final String key = "getCatalogTerm";
        String value = (String)_cache.get(key);
        if (value != null) {
            return value;
        }
        
        value = privateGetJDBCMetaData().getCatalogTerm();
        _cache.put(key, value);
        
        return value;
    }
    
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getSchemaTerm()
     */
    public synchronized String getSchemaTerm() throws SQLException {
        final String key = "getSchemaTerm";
        String value = (String)_cache.get(key);
        if (value != null) {
            return value;
        }
        
        value = privateGetJDBCMetaData().getSchemaTerm();
        _cache.put(key, value);
        
        return value;        
    }

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getProcedureTerm()
     */
    public synchronized String getProcedureTerm() throws SQLException {
        final String key = "getProcedureTerm";
        String value = (String)_cache.get(key);
        if (value != null) {
            return value;
        }
        
        value = privateGetJDBCMetaData().getProcedureTerm();
        _cache.put(key, value);
        
        return value;        
    }
    
    
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getCatalogSeparator()
     */
    public synchronized String getCatalogSeparator() throws SQLException
	{
		final String key = "getCatalogSeparator";
		String value = (String)_cache.get(key);
		if (value != null)
		{
			return value;
		}

		value = privateGetJDBCMetaData().getCatalogSeparator();
		_cache.put(key, value);

		return value;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsCatalogs()
     */
    public boolean supportsCatalogs() throws SQLException
	{
		return supportsCatalogsInTableDefinitions()
			|| supportsCatalogsInDataManipulation()
			|| supportsCatalogsInProcedureCalls();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsCatalogsInTableDefinitions()
     */
    public synchronized boolean supportsCatalogsInTableDefinitions() throws SQLException
	{
		final String key = "supportsCatalogsInTableDefinitions";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		try
		{
			value = Boolean.valueOf(privateGetJDBCMetaData().supportsCatalogsInTableDefinitions());
		}
		catch (SQLException ex)
		{
            boolean isSQLServer = 
                AdapterFactory.isSyBase(bookmark) || AdapterFactory.isMSSQLServer(bookmark);
            
			if (isSQLServer)
			{
				value = Boolean.TRUE;
                _cache.put(key, value);
			}
			throw ex;
		}

		_cache.put(key, value);

		return value.booleanValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsCatalogsInDataManipulation()
     */
    public synchronized boolean supportsCatalogsInDataManipulation() throws SQLException
	{
		final String key = "supportsCatalogsInDataManipulation";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		try
		{
			value = Boolean.valueOf(privateGetJDBCMetaData().supportsCatalogsInDataManipulation());
		}
		catch (SQLException ex)
		{
		    boolean isSQLServer = 
                AdapterFactory.isSyBase(bookmark) || AdapterFactory.isMSSQLServer(bookmark);

			if (isSQLServer)
			{
				value = Boolean.TRUE;
                _cache.put(key, value);
			}
			throw ex;
		}
		_cache.put(key, value);

		return value.booleanValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsCatalogsInProcedureCalls()
     */
	public synchronized boolean supportsCatalogsInProcedureCalls() throws SQLException
	{
		final String key = "supportsCatalogsInProcedureCalls";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		try
		{
			value = Boolean.valueOf(privateGetJDBCMetaData().supportsCatalogsInProcedureCalls());
		}
		catch (SQLException ex)
		{
            boolean isSQLServer = 
                AdapterFactory.isSyBase(bookmark) || AdapterFactory.isMSSQLServer(bookmark);
            
			if (isSQLServer)
			{
				value = Boolean.TRUE;
                _cache.put(key, value);
			}
			throw ex;
		}
		_cache.put(key, value);

		return value.booleanValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getJDBCMetaData()
     */
	public synchronized DatabaseMetaData getJDBCMetaData() throws SQLException
	{
		return privateGetJDBCMetaData();
	}
    
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getTypeInfo()
     * 
     * @deprecated  Replaced by getDataTypes
     */
    public ResultSet getTypeInfo() throws SQLException
	{
		return privateGetJDBCMetaData().getTypeInfo();
	}

   private void close(ResultSet rs)
   {
      try
      {
         if (rs != null)
         {
            rs.close();
         }
      }
      catch (Exception e)
      {
         logger.info("closing resultset failed", e);
      }
   }

   /* (non-Javadoc)
 * @see com.coolsql.sql.ISQLDatabaseMetaData2#getProcedures(java.lang.String, java.lang.String, java.lang.String, com.coolsql.sql.ProgressCallBack)
 */
//   public synchronized IProcedureInfo[] getProcedures(String catalog,
//                                                      String schemaPattern,
//                                                      String procedureNamePattern,
//                                                      ProgressCallBack progressCallBack)
//     throws SQLException
//  {
//     DatabaseMetaData md = privateGetJDBCMetaData();
//     ArrayList<ProcedureInfo> list = new ArrayList<ProcedureInfo>();
//     ResultSet rs = md.getProcedures(catalog, schemaPattern, procedureNamePattern);
//     int count = 0;
//     try
//     {
//        final int[] cols = new int[]{1, 2, 3, 7, 8};
//        final ResultSetReader rdr = new ResultSetReader(rs, cols);
//        Object[] row = null;
//        while ((row = rdr.readRow()) != null)
//        {
//           final int type = ((Number) row[4]).intValue();
//           ProcedureInfo pi = new ProcedureInfo(getAsString(row[0]), getAsString(row[1]),
//              getAsString(row[2]), getAsString(row[3]), type, this);
//
//           list.add(pi);
//
//           if (null != progressCallBack)
//           {
//              if(0 == count++ % 200 )
//              {
//                 progressCallBack.currentlyLoading(pi.getSimpleName());
//              }
//           }
//        }
//     }
//     finally
//     {
//        close(rs);
//
//     }
//     return list.toArray(new IProcedureInfo[list.size()]);
//  }

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getTableTypes()
     */
    public synchronized String[] getTableTypes() throws SQLException
	{
		final String key = "getTableTypes";
		String[] value = (String[])_cache.get(key);
		if (value != null)
		{
			return value;
		}

		final DatabaseMetaData md = privateGetJDBCMetaData();
        
		// Use a set rather than a list as some combinations of MS SQL and the
		// JDBC/ODBC return multiple copies of each table type.
		final Set<String> tableTypes = new TreeSet<String>();
		final ResultSet rs = md.getTableTypes();
		if (rs != null)
		{
			try
			{
				while (rs.next())
				{
					tableTypes.add(rs.getString(1).trim());
				}
			}
			finally
			{
            close(rs);
         }
		}

		final String dbProductName = getDatabaseProductName();
		final int nbrTableTypes = tableTypes.size();

		// InstantDB (at least version 3.13) only returns "TABLES"
		// for getTableTypes(). If you try to use this in a call to
		// DatabaseMetaData.getTables() no tables will be found. For the
		// moment hard code the types for InstantDB.
		if (nbrTableTypes == 1 && dbProductName.equals("InstantDB"))
		{
			tableTypes.clear();
			tableTypes.add("TABLE");
			tableTypes.add("SYSTEM TABLE");
		}

		// At least one version of PostgreSQL through the JDBC/ODBC
		// bridge returns an empty result set for the list of table
		// types. Another version of PostgreSQL returns 6 entries
		// of "SYSTEM TABLE" (which we have already filtered back to one).
		else if (dbProductName.equals("PostgreSQL"))
		{
			if (nbrTableTypes == 0 || nbrTableTypes == 1)
			{
                if (logger.isDebugEnabled()) {
                	logger.debug("Detected PostgreSQL and "+nbrTableTypes+
                                " table types - overriding to 4 table types");
                }
				tableTypes.clear();
				tableTypes.add("TABLE");
				tableTypes.add("SYSTEM TABLE");
				tableTypes.add("VIEW");
                tableTypes.add("SYSTEM VIEW");
			}
            // Treating indexes as tables interferes with the operation of the
            // PostgreSQL plugin
            if (tableTypes.contains("INDEX")) {
                tableTypes.remove("INDEX");
            }
            // Treating sequences as tables interferes with the operation of the
            // PostgreSQL plugin            
            if (tableTypes.contains("SEQUENCE")) {
                tableTypes.remove("SEQUENCE");
            }
            // There are many of these "tables", that PostgreSQL throws 
            // SQLExceptions for whenever a table-like operation is attempted. 
            if (tableTypes.contains("SYSTEM INDEX")) {
                tableTypes.remove("SYSTEM INDEX");
            }
		}

		value = tableTypes.toArray(new String[tableTypes.size()]);
		_cache.put(key, value);
		return value;
	}
   
   /* (non-Javadoc)
 * @see com.coolsql.sql.ISQLDatabaseMetaData2#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])
 */
//    public synchronized IUDTInfo[] getUDTs(String catalog, String schemaPattern,
//								           String typeNamePattern, int[] types)
//		throws SQLException
//	{
//		DatabaseMetaData md = privateGetJDBCMetaData();
//		ArrayList<UDTInfo> list = new ArrayList<UDTInfo>();
//		ResultSet rs = md.getUDTs(catalog, schemaPattern, typeNamePattern, types);
//		try
//		{
//			final int[] cols = new int[] {1, 2, 3, 4, 5, 6};
//			final ResultSetReader rdr = new ResultSetReader(rs, cols);
//			Object[] row = null;
//			while ((row = rdr.readRow()) != null)
//			{
//				list.add(new UDTInfo(getAsString(row[0]), getAsString(row[1]), getAsString(row[2]),
//									getAsString(row[3]), getAsString(row[4]), getAsString(row[5]),
//									this));
//			}
//		}
//		finally
//		{
//         close(rs);
//      }
//
//		return list.toArray(new IUDTInfo[list.size()]);
//	}
   @SuppressWarnings("unused")
   private String getAsString(Object val)
   {
      if(null == val)
      {
         return null;
      }
      else
      {
         if (val instanceof String) {
             return (String)val;
         } else {
             return "" + val;
         }
      }

   }

   /* (non-Javadoc)
 * @see com.coolsql.sql.ISQLDatabaseMetaData2#getNumericFunctions()
 */
   public synchronized String[] getNumericFunctions() throws SQLException
	{
		final String key = "getNumericFunctions";
		String[] value = (String[])_cache.get(key);
		if (value != null)
		{
			return value;
		}

		value = makeArray(privateGetJDBCMetaData().getNumericFunctions());
		_cache.put(key, value);
		return value;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getStringFunctions()
     */
	public synchronized String[] getStringFunctions() throws SQLException
	{
		final String key = "getStringFunctions";
		String[] value = (String[])_cache.get(key);
		if (value != null)
		{
			return value;
		}

		value = makeArray(privateGetJDBCMetaData().getStringFunctions());
		_cache.put(key, value);
		return value;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getSystemFunctions()
     */
	public synchronized String[] getSystemFunctions() throws SQLException
	{
		final String key = "getSystemFunctions";
		String[] value = (String[])_cache.get(key);
		if (value != null)
		{
			return value;
		}

		value = makeArray(privateGetJDBCMetaData().getSystemFunctions());
		_cache.put(key, value);
		return value;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getTimeDateFunctions()
     */
	public synchronized String[] getTimeDateFunctions() throws SQLException
	{
		final String key = "getTimeDateFunctions";
		String[] value = (String[])_cache.get(key);
		if (value != null)
		{
			return value;
		}

		value = makeArray(privateGetJDBCMetaData().getTimeDateFunctions());
		_cache.put(key, value);
		return value;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getSQLKeywords()
     */
	public synchronized String[] getSQLKeywords() throws SQLException
	{
		final String key = "getSQLKeywords";
		String[] value = (String[])_cache.get(key);
		if (value != null)
		{
			return value;
		}

		value = makeArray(privateGetJDBCMetaData().getSQLKeywords());
		_cache.put(key, value);
		return value;
	}
    
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#getTablePrivileges(com.coolsql.sql.ITableInfo)
     * 
     * @deprecated use getTablePrivilegesDataSet instead
     */ 
	public ResultSet getTablePrivileges(Table ti)
		throws SQLException
	{
		return privateGetJDBCMetaData().getTablePrivileges(ti.getCatalog(),
													ti.getSchema(),
													ti.getName());
	}
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#correctlySupportsSetMaxRows()
     */
    public boolean correctlySupportsSetMaxRows() throws SQLException
	{
		return true;
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#supportsMultipleResultSets()
     */
	public synchronized boolean supportsMultipleResultSets()
			throws SQLException
	{
		final String key = "supportsMultipleResultSets";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		value = Boolean.valueOf(privateGetJDBCMetaData().supportsMultipleResultSets());
		_cache.put(key, value);

		return value.booleanValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#storesUpperCaseIdentifiers()
     */
	public synchronized boolean storesUpperCaseIdentifiers()
		throws SQLException
	{
		final String key = "storesUpperCaseIdentifiers";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		value = Boolean.valueOf(privateGetJDBCMetaData().storesUpperCaseIdentifiers());
		_cache.put(key, value);

		return value.booleanValue();
	}
    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData2#storesLowerCaseIdentifiers()
     */
	public synchronized boolean storesLowerCaseIdentifiers()
		throws SQLException
	{
		final String key = "storesLowerCaseIdentifiers";
		Boolean value = (Boolean)_cache.get(key);
		if (value != null)
		{
			return value.booleanValue();
		}

		value = Boolean.valueOf(privateGetJDBCMetaData().storesLowerCaseIdentifiers());
		_cache.put(key, value);

		return value.booleanValue();
	}

    /* (non-Javadoc)
     * @see com.coolsql.sql.ISQLDatabaseMetaData#clearCache()
     */
	public void clearCache()
	{
		_cache.clear();
	}

	/**
	 * Make a String array of the passed string. Commas separate the elements
	 * in the input string. The array is sorted.
	 *
	 * @param	data	Data to be split into the array.
	 *
	 * @return	data as an array.
	 */
	private static String[] makeArray(String data)
	{
		if (data == null)
		{
			data = "";
		}

		final List<String> list = new ArrayList<String>();
		final StringTokenizer st = new StringTokenizer(data, ",");
		while (st.hasMoreTokens())
		{
			list.add(st.nextToken());
		}
		Collections.sort(list);

		return list.toArray(new String[list.size()]);
	}

	/**
	 * Return the <TT>DatabaseMetaData</TT> object for this connection.
	 *
	 * @return	The <TT>DatabaseMetaData</TT> object for this connection.
	 *
	 * @throws	SQLException	Thrown if an SQL error occurs.
	 */
	private DatabaseMetaData privateGetJDBCMetaData() throws SQLException
	{
//        checkThread();
		return _conn.getMetaData();
	}

    /**
     * 
     * @param fki
     * @return
     */
//	private String createForeignKeyInfoKey(ForeignKeyInfo fki)
//	{
//		final StringBuffer buf = new StringBuffer();
//		buf.append(fki.getForeignKeyCatalogName())
//			.append(fki.getForeignKeySchemaName())
//			.append(fki.getForeignKeyTableName())
//			.append(fki.getForeignKeyName())
//			.append(fki.getPrimaryKeyCatalogName())
//			.append(fki.getPrimaryKeySchemaName())
//			.append(fki.getPrimaryKeyTableName())
//			.append(fki.getPrimaryKeyName());
//		return buf.toString();
//	}

    public synchronized String[] getCatalogs() throws SQLException
	{
		final ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = privateGetJDBCMetaData().getCatalogs();
		try
		{
            while(rs.next())
            {
            	list.add(StringUtil.trim(rs.getString(1)));
            }
		}
		finally
		{
         close(rs);
		}

		return list.toArray(new String[list.size()]);
	}
}

