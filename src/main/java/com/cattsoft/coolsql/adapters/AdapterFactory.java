package com.cattsoft.coolsql.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;

/**
 * 
 * @author liu_xlin
 *��ݿ��������Ĺ�����
 */
public class AdapterFactory
{
    static class ComparatorImpl
        implements Comparator<DatabaseAdapter>
    {

        public int compare(DatabaseAdapter arg0, DatabaseAdapter arg1)
        {
            DatabaseAdapter adapter0 = (DatabaseAdapter)arg0;
            DatabaseAdapter adapter1 = (DatabaseAdapter)arg1;
            if(adapter0 == null && adapter0 != null)
                return -1;
            if(adapter0 != null && adapter1 == null)
                return 1;
            if(adapter0 == null && adapter1 == null)
                return 0;
            else
                return adapter0.getDisplayName().compareTo(adapter1.getDisplayName());
        }

        ComparatorImpl()
        {
        }
    }

    static class DriverInfo
    {

        public String getType()
        {
            return type;
        }

        public String getURLPattern()
        {
            return urlPattern;
        }

        private final String type;
        private final String urlPattern;

        DriverInfo(String type)
        {
            this(type, null);
        }

        DriverInfo(String type, String urlPattern)
        {
            this.type = type;
            this.urlPattern = urlPattern;
        }
    }


    private AdapterFactory()
    {
        adapters = Collections.synchronizedMap(new HashMap<String,DatabaseAdapter>());
        addAdapter(new GenericAdapter("GENERIC"));
        addAdapter(new GenericAdapter("HSQLDB"));
        addAdapter(new OracleAdapter());
        addAdapter(new DB2Adapter());
        addAdapter(new DB2AS400Adapter());
        addAdapter(new PostgresAdapter());
        addAdapter(new MySQLAdapter());
        addAdapter(new AdabasDAdapter());
        addAdapter(new InformixAdapter());
        addAdapter(new RedBrickAdapter());
        addAdapter(new GenericAdapter("SYBASE"));
        addAdapter(new GenericAdapter("POINTBASE"));
        addAdapter(new GenericAdapter("JDBC_ODBC_BRIDGE"));
        addAdapter(new MSSQLServerAdapter());
        addAdapter(new GenericAdapter("DERBY"));
    }

    private void addAdapter(DatabaseAdapter adapter)
    {
        adapters.put(adapter.getType(), adapter);
    }

    public static synchronized AdapterFactory getInstance()
    {
        if(instance == null)
            instance = new AdapterFactory();
        return instance;
    }

    public DatabaseAdapter getAdapter(String type)
    {
        DatabaseAdapter result = (DatabaseAdapter)adapters.get(type);
        if(result == null)
            result = (DatabaseAdapter)adapters.get("GENERIC");
        return result != null ? result : null;
    }

    public DatabaseAdapter[] getDriverList()
    {
        List<DatabaseAdapter> list = new ArrayList<DatabaseAdapter>(adapters.values());
        Collections.sort(list, new ComparatorImpl());
        return (DatabaseAdapter[])list.toArray(new DatabaseAdapter[list.size()]);
    }

    public String getAdapterType(String driverClassName)
    {
        DriverInfo driverInfo = (DriverInfo)DRIVER_MAP.get(driverClassName);
        return driverInfo != null ? driverInfo.getType() : null;
    }

    public String getURLPattern(String driverClassName)
    {
        DriverInfo driverInfo = (DriverInfo)DRIVER_MAP.get(driverClassName);
        return driverInfo != null ? driverInfo.getURLPattern() : null;
    }

    public static final String GENERIC = "GENERIC";
    public static final String HSQLDB = "HSQLDB";
    public static final String ORACLE = "ORACLE";
    public static final String POSTGRES = "POSTGRES";
    public static final String MYSQL = "MYSQL";
    public static final String DB2 = "DB2";
    public static final String DB2AS400 = "DB2AS400";
    public static final String ADABASD = "ADABASD";
    public static final String INFORMIX = "INFORMIX";
    public static final String REDBRICK = "REDBRICK";
    public static final String POINTBASE = "POINTBASE";
    public static final String SYBASE = "SYBASE";
    public static final String JDBC_ODBC_BRIDGE = "JDBC_ODBC_BRIDGE";
    public static final String MS_SQL_SERVER = "MS_SQL_SERVER";
    public static final String DERBY = "DERBY";
    private static final Map<String,DriverInfo> DRIVER_MAP;
    private static AdapterFactory instance;
    private Map<String,DatabaseAdapter> adapters;

    static 
    {
        DRIVER_MAP = Collections.synchronizedMap(new HashMap<String,DriverInfo>());
        DRIVER_MAP.put("com.ddtek.jdbc.informix.InformixDriver", new DriverInfo("INFORMIX"));
        DRIVER_MAP.put("com.ddtek.jdbc.db2.DB2Driver", new DriverInfo("DB2", "jdbc:datadirect:db2://${hostname}:${port};DatabaseName=${dbname}"));
        DRIVER_MAP.put("com.ddtek.jdbc.oracle.OracleDriver", new DriverInfo("ORACLE", "jdbc:oracle:thin:@{hostname}:{port}:{dbname}"));
        DRIVER_MAP.put("com.ddtek.jdbc.sqlserver.SQLServerDriver", new DriverInfo("GENERIC"));
        DRIVER_MAP.put("com.ddtek.jdbc.sybase.SybaseDriver", new DriverInfo("SYBASE"));
        DRIVER_MAP.put("com.ibm.as400.access.AS400JDBCDriver", new DriverInfo("DB2AS400"));
        DRIVER_MAP.put("COM.ibm.db2.jdbc.app.DB2Driver", new DriverInfo("DB2", "jdbc:db2:{dbname}"));
        DRIVER_MAP.put("COM.ibm.db2.jdbc.net.DB2Driver", new DriverInfo("DB2", "jdbc:db2://{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("com.inet.ora.OraDriver", new DriverInfo("ORACLE", "jdbc:oracle:thin:@{hostname}:{port}:{dbname}"));
        DRIVER_MAP.put("com.inet.drda.DRDADriver", new DriverInfo("DB2", "jdbc:inetdb2:{hostname}:{port}?database={dbname}"));
        DRIVER_MAP.put("com.inet.syb.SybDriver", new DriverInfo("SYBASE"));
        DRIVER_MAP.put("com.inet.tds.TdsDriver", new DriverInfo("MS_SQL_SERVER"));
        DRIVER_MAP.put("com.informix.jdbc.IfxDriver", new DriverInfo("INFORMIX", "jdbc:informix-sqli://{hostname}:{port}/{dbname}:INFORMIXSERVER={informixserver}"));
        DRIVER_MAP.put("com.microsoft.jdbc.sqlserver.SQLServerDriver", new DriverInfo("MS_SQL_SERVER","jdbc:microsoft:sqlserver://{hostname}:{port};DatabaseName={dbname}"));
        DRIVER_MAP.put("com.mysql.jdbc.Driver", new DriverInfo("MYSQL", "jdbc:mysql://{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("com.pointbase.jdbc.jdbcUniversalDriver", new DriverInfo("POINTBASE", "jdbc:pointbase:server://{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("com.sybase.jdbc.SybDriver", new DriverInfo("SYBASE", "jdbc:sybase:Tds:{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("com.sybase.jdbc2.jdbc.SybDriver", new DriverInfo("SYBASE", "jdbc:sybase:Tds:{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("com.sybase.jdbcx.SybDriver", new DriverInfo("SYBASE", "jdbc:sybase:Tds:{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("net.sourceforge.jtds.jdbc.Driver", new DriverInfo("MS_SQL_SERVER","jdbc:jtds:sqlserver://{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("oracle.jdbc.driver.OracleDriver", new DriverInfo("ORACLE", "jdbc:oracle:thin:@{hostname}:{port}:{dbname}"));
        DRIVER_MAP.put("oracle.jdbc.OracleDriver", new DriverInfo("ORACLE", "jdbc:oracle:thin:@{hostname}:{port}:{dbname}"));
        DRIVER_MAP.put("org.gjt.mm.mysql.Driver", new DriverInfo("MYSQL", "jdbc:mysql://{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("org.hsqldb.jdbcDriver", new DriverInfo("HSQLDB"));
        DRIVER_MAP.put("org.postgresql.Driver", new DriverInfo("POSTGRES", "jdbc:postgresql://{hostname}:{port}/{dbname}"));
        DRIVER_MAP.put("sun.jdbc.odbc.JdbcOdbcDriver", new DriverInfo("JDBC_ODBC_BRIDGE", "jdbc:odbc:{datasource}"));
        DRIVER_MAP.put("weblogic.jdbc.informix.InformixDriver", new DriverInfo("INFORMIX"));
        DRIVER_MAP.put("weblogic.jdbc.sqlserver.SybaseDriver", new DriverInfo("SYBASE"));
        DRIVER_MAP.put("org.apache.derby.jdbc.driver20", new DriverInfo("DERBY", "jdbc:derby:{dbname}"));
        DRIVER_MAP.put("org.apache.derby.jdbc.driver30", new DriverInfo("DERBY", "jdbc:derby:{dbname}"));
        DRIVER_MAP.put("org.apache.derby.jdbc.EmbeddedDriver", new DriverInfo("DERBY", "jdbc:derby:{dbname}"));
    }
    private static String getDatabaseType(Bookmark bookmark)
    {
    	return bookmark.getDriver().getType();
    }
    public static boolean isDB2(Bookmark bookmark)
    {
    	String type=getDatabaseType(bookmark);
    	return DB2.equals(type);
    }
    public static boolean isMSSQLServer(Bookmark bookmark)
    {
    	String type=getDatabaseType(bookmark);
    	return MS_SQL_SERVER.equals(type);
    }
    public static boolean isSyBase(Bookmark bookmark)
    {
    	String type=getDatabaseType(bookmark);
    	return SYBASE.equals(type);
    }
    public static boolean isPostgreSQL(Bookmark bookmark)
    {
    	String type=getDatabaseType(bookmark);
    	return POSTGRES.equals(type);
    }
    public static boolean isInformix(Bookmark bookmark)
    {
    	String type=getDatabaseType(bookmark);
    	return INFORMIX.equals(type);	
    }
    /**
     * Informix represents database objects in catalogs *and* schemas.  So a table
     * called <table> might be found in the <databasename> catalog, which lives in 
     * the <schemaname> schema and is addressed as:
     * 
     * <databasename>:"<schemaname>".<table>
     * 
     * It may also be referred to as simply <table>
     * 
     * This method returns a qualifed name that meets this criteria. 
     * 
     * @return a valid Informix qualified name - if catalog *and* schema are not
     *         null/empty, this returns the database object name such as
     *         catalog:schema.simpleName.  However, if either catalog or schema
     *         (or both) are null, this simply returns the simpleName
     */
    public static String getInformixQualifiedName(String catalog,String schema,String simpleName) {
        StringBuffer result = new StringBuffer();
        if (catalog != null && schema != null) {
            result.append(catalog);
            result.append(":");
            result.append("\"");
            result.append(schema);
            result.append("\"");
            result.append(".");
        }
        result.append(simpleName);
        return result.toString();
    }
}
