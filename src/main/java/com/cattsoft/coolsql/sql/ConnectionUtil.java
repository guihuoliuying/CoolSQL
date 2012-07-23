/*
 * �������� 2006-6-4
 *
 */
package com.cattsoft.coolsql.sql;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.DriverInfo;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.model.Schema;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ������ݿ�����ӣ��ͷ�,�Լ�sql����ִ�У�����ǲ�ѯsql����ȡ���������Ȼ�����bean
 */
public class ConnectionUtil {

    /**
     * key:current execute thread value:current statement
     */
    private static Map longTimeStatement = null;

    /**
     * �����ѯ���,�Լ���Ӧ��Statement����
     */
    private static Map st = null;
    static {
        st = Collections.synchronizedMap(new HashMap());
        longTimeStatement = Collections.synchronizedMap(new HashMap());
    }

    private ConnectionUtil() {
    }

    /**
     * ��ȡ��ݿ����ӣ������ø����ӵ��������
     */
    public static void getConnection(Bookmark bookmark)
            throws ConnectionException, ClassNotFoundException, SQLException, UnifyException {
        Connection con = connect(bookmark);
        bookmark.setConnection(con);
        bookmark.setAutoCommit(bookmark.isAutoCommit());
        try {
            bookmark.setDatabase(new Database(bookmark));
        } catch (Exception e) {
            bookmark.disconnect();   //����ʼ����ݿ����Database�����?���Ͽ���ݿ�����
        }
    }

    /**
     * Connect to database server, and get a connection.
     * 
     */
    public static Connection connect(Bookmark bookmark) throws SQLException,
            ClassNotFoundException, ConnectionException {
        LogProxy proxy = LogProxy.getProxy();
        proxy.info("Connecting to database : " + bookmark.getAliasName());
        try {
            DriverInfo jdbcDriver = bookmark.getDriver();
            String driverClass = jdbcDriver.getClassName();
            Driver driver;
            //ʵ���������
            driver = (Driver) (LoadJar.getInstance()
                    .getDriverClass(driverClass).newInstance());

            if (driverClass != null) {
            	//Set login timeout
            	DriverManager.setLoginTimeout(Setting.getInstance().
                		getIntProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_CONNECT_TIMEOUT, 60));
                //������ݿ���������Ҫ����Ϣ
                Properties props = new Properties();
                props.put("user", bookmark.getUserName());
                props.put("password", bookmark.getPwd());
                
                if (jdbcDriver.getType().equals("ORACLE")) { //����oracle��ݿ���Ҫ���ö������������
                    props.put("remarksReporting", "true");
                    props.put("includeSynonyms", "true");
                    props.put("resultSetMetaDataOptions ", 1);
                }

                /** -----------------------------------------*/
    			// identify the program name when connecting
    			// this is different for each DBMS.
    			String propName = null;
    			String url=bookmark.getConnectUrl();
    			if (url.startsWith("jdbc:oracle"))
    			{
    				propName = "v$session.program";
    				
    				// it seems that the Oracle 10 driver does not 
    				// add this to the properties automatically
    				// (as the drivers for 8 and 9 did)
    				String user = System.getProperty("user.name",null);
    				if (user != null) props.put("v$session.osuser", user);
    			}
    			else if (url.startsWith("jdbc:inetdae"))
    			{
    				propName = "appname";
    			}
    			else if (url.startsWith("jdbc:jtds"))
    			{
    				propName = "APPNAME";
    			}
    			else if (url.startsWith("jdbc:microsoft:sqlserver"))
    			{
    				// Old MS SQL Server driver
    				propName = "ProgramName";
    			}
    			else if (url.startsWith("jdbc:sqlserver:"))
    			{
    				// New SQL Server 2005 JDBC driver
    				propName = "applicationName";
    				if (!props.containsKey("workstationID"))
    				{
    					InetAddress localhost = InetAddress.getLocalHost();
    					String localName = (localhost != null ? localhost.getHostName() : null);
    					if (localName != null)
    					{
    						props.put("workstationID", localName);
    					}
    				}
    			}
    			if (propName != null && !props.containsKey(propName))
    			{
    				String appName ="CoolSQL";
    				props.put(propName, appName);
    			}
                /** -----------------------------------------*/
                
                Connection connection = driver.connect(
                		url, props); //������ݿ�
                if (connection == null) //��ȡ����ʧ��
                {
                    throw new ConnectionException(
                            "Error: Driver returned a null connection: "
                                    + bookmark.toString());
                } else //��ȡ���ӳɹ�����ȡ��ݿ������Ϣ
                {

                    DatabaseMetaData metaData = connection.getMetaData();
                    jdbcDriver.setDriverName(metaData.getDriverName()); //��������
                    jdbcDriver.setDriverVersion(metaData.getDriverVersion());
                    jdbcDriver.setDbProductName(metaData
                            .getDatabaseProductName());
                    jdbcDriver.setDbPruductVersion(metaData
                            .getDatabaseProductVersion());
                    proxy.info("Connected to: " + bookmark.getAliasName());
                    return connection;
                }
            } else { //��ǩû�ж�Ӧ����ݿ�����
                throw new ConnectionException(PublicResource
                        .getSQLString("database.connect.nodriver"));
            }
        } catch (SQLException e) {
            throw e;
        } catch(Throwable e)
        {
        	String msg="Error connecting to database. (" + e.getClass().getName() + " - " + e.getMessage() + ")";
        	LogProxy.errorLog(msg,e);
        	throw new SQLException(msg);
        }
    }

    /**
     * �Ͽ���ݿ������
     * 
     * @param con
     * @throws SQLException
     */
    public static void disconnect(Connection con) throws SQLException {
        if (con == null)
            return;
        if(!con.getAutoCommit())
        {
        	String setting=Setting.getInstance().getProperty(
    				PropertyConstant.PROPERTY_VIEW_BOOKMARK_BEFORE_DISCONNECT ,"commit");
        	if(setting.equals("commit"))
        	{
        		con.commit();
        	}else
        	{
        		con.rollback();
        	}
        }
        con.close();
        System.out.println("disconnect!");
    }

    /**
     * �ύ
     * 
     * @param con
     * @throws SQLException
     */
    public static void commit(Connection con) throws SQLException {
        con.commit();
    }

    /**
     * �ع�
     * 
     * @param con
     * @throws SQLException
     */
    public static void rollback(Connection con) throws SQLException {

        con.rollback();
    }

    /**
     * ��ȡ��ǰ��ݿ������ģʽ��
     * @deprecated
     * @param con
     * @return
     * @throws SQLException
     * 
     */
    public static List getSchemas(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        if (!metaData.supportsSchemasInTableDefinitions()
				|| !metaData.supportsSchemasInDataManipulation()
				|| !metaData.supportsSchemasInIndexDefinitions())
			return new ArrayList();
        ResultSet set = metaData.getSchemas();
        List schemas = new ArrayList();
        try {
            while (set.next()) {
                schemas.add(StringUtil.trim(set.getString(1)));
            }
        } finally {
            if (set != null)
                set.close();
        }
        return schemas;
    }
    /**
     * ��ݷ����ȡ�������µ�ģʽ����
     * @param bookmark
     * @param catalog
     * @return
     * @throws UnifyException
     * @throws SQLException
     */
    public static Schema[] getSchemas(Bookmark bookmark,String catalog) throws UnifyException, SQLException
    {
    	return bookmark.getDbInfoProvider().getSchemas(catalog);
    }
    public static SQLResults execute(Bookmark bookmark, Connection con,
            Entity entity[], String sql, int numberOfRowsPerPage)
            throws SQLException {
        long startTime = System.currentTimeMillis();

        LogProxy log = LogProxy.getProxy();
        log.debug("SQL (" + bookmark.getAliasName() + ") [" + sql + "]");
        Statement statement = con.createStatement();
        //���浱ǰִ�е��߳����Ӧ��Statement����
        longTimeStatement.put(Thread.currentThread(), statement);
        try {
        	long costTime;
            SQLResults results;
            if(numberOfRowsPerPage>0&&numberOfRowsPerPage!=Integer.MAX_VALUE)
            	statement.setMaxRows(numberOfRowsPerPage+1); //max rows was setted as the value of numberOfRowsPerPage first
            if (statement.execute(sql)) {
            	costTime=System.currentTimeMillis() - startTime;
                //ִ����Ϻ󣬽���ǰ�̶߳�Ӧ�ļ�ֵ��ɾ��

                ResultSet set = statement.getResultSet();
                try {
                    results = SQLStandardResultSetResults.create(set, bookmark,
                            sql, entity, numberOfRowsPerPage);
                } finally {
                    set.close();
                }
            } else {
            	costTime=System.currentTimeMillis() - startTime;
                //ִ����Ϻ󣬽���ǰ�̶߳�Ӧ�ļ�ֵ��ɾ��
                longTimeStatement.remove(Thread.currentThread());

                int updates = statement.getUpdateCount();
                results = new SQLUpdateResults(updates);
                results.setSql(sql);
            }
            log.debug("Success: result set displayed");
            if (results != null) {
                results.setTime(startTime);//����ִ��ʱ��
                results.setCostTime(costTime); //����ִ�����ʱ��
            }
            return results;
        } finally {
            longTimeStatement.remove(Thread.currentThread());
            statement.close();           
        }
    }
    public static ResultSet executeQuery(Bookmark bookmark, String sql) throws UnifyException, SQLException
    {
    	return executeQuery(bookmark,sql,false);
    }
    /**
     * Execute specified SQL statement. User can specify the flag whether getting all row data.
     * 
     * @param bookmark --Bookmark object.
     * @param sql --SQL statement
     * @param isQueryAll --It indicates whether querying all row data.
     *  The true value means that the statement used for executing will not invoke setMaxRows() method. 
     * @return ResultSet 
     */
    public static ResultSet executeQuery(Bookmark bookmark, String sql, boolean isQueryAll)
            throws UnifyException, SQLException {
        LogProxy log = LogProxy.getProxy();
        log.debug("SQL (" + bookmark.getAliasName() + ") [" + sql + "]");

        Connection con = bookmark.getConnection();
        Statement statement = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        longTimeStatement.put(Thread.currentThread(), statement);
        int numberOfRowsPerPage=bookmark.getDbInfoProvider().getNumberOfRowsPerPage();
        if(!isQueryAll)
        {
	        if(numberOfRowsPerPage>0&&numberOfRowsPerPage!=Integer.MAX_VALUE)
	        	statement.setMaxRows(numberOfRowsPerPage+1);
        }
        if (statement.execute(sql)) { //sqlΪ��ѯ����
            longTimeStatement.remove(Thread.currentThread());

            ResultSet set = statement.getResultSet();
            log.debug("execute successfully!");
            regStatement(set, statement);
            return set;
        } else { //sqlΪ�޸ĸ�������
            longTimeStatement.remove(Thread.currentThread());

            statement.close();
            throw new UnifyException(PublicResource
                    .getSQLString("sql.execute.sqltype.queryonly"));
        }
    }
    

    /**
     * ��ݽ���ҵ���Ӧ��Statement����Ȼ����ر�
     * 
     * @param set
     *            --�����󣬵��ô˷����󣬸ý�����ʧЧ
     * @throws SQLException
     */
    public static void closeStatement(ResultSet set) throws SQLException {
        Statement statement = (Statement) st.remove(set);
        if (statement != null) {
            statement.close();
        }
    }

    /**
     * ��ÿ�β�ѯ�Ľ��Ǽ����ڴ��У�������ݽ�����ɺ󣬹ر���صĶ���
     * 
     * @param set
     * @param statement
     */
    public static void regStatement(ResultSet set, Statement statement) {
        System.out.println(":" + longTimeStatement.size());
        st.put(set, statement);
    }

    /**
     * ������Ƿ�����������÷��������next
     * 
     * @param set
     * @return
     * @throws SQLException
     */
    public static boolean hasNext(ResultSet set) throws SQLException {
        boolean tmp = set.next();
        if (tmp)
            set.previous();
        return tmp;
    }

    /**
     * ����Statement�������ִ��֮ǰ���ö��󱣴�
     * 
     * @param th
     *            --ִ�г�ʱ�䴦����߳�
     * @param st
     *            --Statement����
     */
    public static void addLongTimeStatement(Thread th, Statement st) {
        if (longTimeStatement.size() > 0) {
            LogProxy.message("���ֳ�ʱ��δ�رյ�Statement����", 1);
        }
        System.out.println("longTimeStatement:" + longTimeStatement.size());
        longTimeStatement.put(th, st);
    }

    /**
     * ִ����ɺ���ø÷���
     * 
     * @param th
     *            --ִ�г�ʱ�䴦����߳�
     */
    public static void removeLongTimeStatement(Thread th) {
        longTimeStatement.remove(th);
    }

    /**
     * ��ִ�ж���ȡ��
     * 
     * @param th
     *            --ִ�г�ʱ�䴦����߳�
     * @throws SQLException
     */
    public static void quitLongTimeStatement(Thread th) throws SQLException {
        Statement st = (Statement) longTimeStatement.get(th);
        if (st != null)
        {
        	try
        	{
            	st.cancel();
        	}catch(Throwable t)
        	{
        		LogProxy.errorLog("cancel statement failed", t);
        	}
        	try
        	{
        		st.close();
        	}catch(Throwable t)
        	{
        		LogProxy.errorLog("closing statement is not successful", t);
        	}
        }
        removeLongTimeStatement(th);
    }
}
