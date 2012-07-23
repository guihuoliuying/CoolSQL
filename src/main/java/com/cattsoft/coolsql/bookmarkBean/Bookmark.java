/*
 * Created on 2006-6-2
 */
package com.cattsoft.coolsql.bookmarkBean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import com.cattsoft.coolsql.adapters.AdapterFactory;
import com.cattsoft.coolsql.adapters.DatabaseAdapter;
import com.cattsoft.coolsql.main.frame.Launcher;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.ConnectionException;
import com.cattsoft.coolsql.sql.ConnectionUtil;
import com.cattsoft.coolsql.sql.Database;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.commonoperator.BookMarkPropertyOperator;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Catalog;
import com.cattsoft.coolsql.sql.model.Schema;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;
import com.cattsoft.coolsql.view.bookmarkview.model.CatalogNode;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.SQLGroupNode;
import com.cattsoft.coolsql.view.bookmarkview.model.SchemaNode;
import com.cattsoft.coolsql.view.log.LogProxy;


/**
 * The bookmark class used to hold connection information according to certain database.
 * @author Kenny liu
 */
public class Bookmark extends Identifier implements Serializable{
	private static final long serialVersionUID = 106312940339539290L;
	
	public final static String PROPERTY_CONNECTED="connected";
	public final static String PROPERTY_AUTOCOMMIT="autocommit";
	
    /**
     * The preference database that manages database.
     */
	private Database database=null;
    /**
	 * The alias name of bookmark.
	 */
	private String aliasName = null;

	/**
	 * the user name used to connect to database.
	 */
	private String userName = null;

	/**
	 * The connection password.
	 */
	private String pwd = null;

	/**
	 * The object describing the information about JDBC driver.
	 */
	private DriverInfo driver = null;

	/**
	 * The JDBC connection to database.
	 */
	private Connection connection = null;

	/**
	 * The flag indicates whether database should be committed automatically when saving modification.
	 */
	private boolean isAutoCommit = true;
//    private String autoCommitSet="Always True";
	/**
	 * the flag indicates whether a password should be popped up when connecting to database every time.
	 */
	private boolean isPromptPwd = false;
	
	/**
	 * The status of connection
	 * 1��Connecting
	 * 2�� Disconnecting
	 * 0�� Normal
	 */
    private int connectState=0;
    
	public Bookmark() {
		this(null, null, null, null, null, true, false);
	}

	public Bookmark(String aliasName, String userName, String pwd,
			String className, String connect, boolean isAutoCommit,
			boolean isPromptPwd) {
		super(BookMarkPubInfo.NODE_DATABASE,aliasName,null,false);
		super.setBookmark(this);
		this.aliasName=aliasName;
		this.setUserName(userName);
		this.setPwd(pwd);
		this.setClassName(className);
		this.isAutoCommit=isAutoCommit;
		this.setPromptPwd(isPromptPwd);
		this.setConnectUrl(connect); 
		driver=new DriverInfo();
	}
	
	public Connection getConnection() throws UnifyException{
	    if(connection==null)
	        throw new UnifyException(PublicResource.getSQLString("database.notconnected")+this.getAliasName());
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	/**
	 * Connect to database.
	 */
	public synchronized void connect() throws SQLException, UnifyException {
	    boolean isConnect=this.isConnected();
	    if(isConnect)
	    	return;
	    
        try {
            ConnectionUtil.getConnection(this);
        }catch (ConnectionException e) {
            throw new UnifyException(e.getMessage(),e);
        } catch (ClassNotFoundException e) {
			throw new UnifyException(e.getMessage(),e); 
		}finally
        {
            if(isConnect!=isConnected())
                pcs.firePropertyChange(PROPERTY_CONNECTED,isConnect,isConnected());
        }
    }
	/**
	 * Disconnect the connection with database.
	 */
    public void disconnect() throws SQLException
    {
        boolean isConnect=this.isConnected();
        try
        {
            if(connection!=null)
           ConnectionUtil.disconnect(connection);
           database.dispose();
           database=null;
        }finally
        {
            connection=null;
            if(isConnect!=isConnected())
                pcs.firePropertyChange(PROPERTY_CONNECTED,isConnect,isConnected());
        }
    }
    
	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	/**
	 * Set whether database should be commit automatically when saving modification.
	 */
	public void setAutoCommit(boolean isAutoCommit) throws SQLException, UnifyException {
		boolean oldValue=this.isAutoCommit;
		this.isAutoCommit = isAutoCommit;
		if(!Launcher.isInitializing&&isConnected())
		{
		    if(oldValue!=this.isAutoCommit&&this.isAutoCommit&&!oldValue)
		    {
			    String beforeThing=Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_BEFORE_ENABLEAUTOCOMMIT ,"commit");
			    if(beforeThing.equals("commit"))
			    {
			    	getConnection().commit();
			    }else
			    {
			    	getConnection().rollback();
			    }
		    }
		    getConnection().setAutoCommit(isAutoCommit);
		}
		pcs.firePropertyChange(PROPERTY_AUTOCOMMIT,oldValue,this.isAutoCommit);
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		String oldValue=this.aliasName;
		String newValue=aliasName;
		if(this.aliasName==null||this.aliasName.equals(this.getContent()))  
		   this.setContent(aliasName);
		this.aliasName = aliasName;
		
		pcs.firePropertyChange("aliasName",oldValue,newValue);
	}

	public String getConnectUrl() {
		return driver.getUrlPattern();
	}

	public void setConnectUrl(String connectUrl) {
		driver.setUrlPattern(connectUrl);
	}

	public DriverInfo getDriver() {
		return driver;
	}

	public void setDriver(DriverInfo driver) {
		this.driver = driver;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isConnected() {
		return connection != null;
	}

	public boolean isPromptPwd() {
		return isPromptPwd;
	}

	public void setPromptPwd(boolean isPromptPwd) {
		this.isPromptPwd = isPromptPwd;
	}

	public String getClassName() {
		return driver==null?null:driver.getClassName();
	}

	public void setClassName(String className) {
	    String oldValue=driver!=null?driver.getClassName():null;
	    if(driver!=null)
		    driver.setClassName(className);
	    else
	        driver=new DriverInfo(className);
		pcs.firePropertyChange("classname",oldValue,className);
	}
	/**
	 * Return the adapter of database.
	 */
	public DatabaseAdapter getAdapter()
	{
	    return driver != null ? AdapterFactory.getInstance().getAdapter(driver.getType()) : null;
	}
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append("aliasName=");
        buffer.append(aliasName);
        buffer.append(", ");
        buffer.append("username=");
        buffer.append(userName);
        buffer.append(", ");
        buffer.append("password=****");
        buffer.append(", ");
        buffer.append("connect=");
        buffer.append(getConnectUrl());
        buffer.append(", ");
        buffer.append("driver=");
        buffer.append(driver.getClassName());
        buffer.append("]");
        return buffer.toString();
    }
    
    /**
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent,INodeFilter filter) {
       //Do nothing
    }
    /**
     * ˢ����ǩ�ڵ�,���»�ȡģʽ�ڵ���Ϣ
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#refresh(com.coolsql.view.bookmarkview.model.DefaultTreeNode)
     */
    public void refresh(DefaultTreeNode node,INodeFilter filter) throws SQLException,UnifyException
    {       
    	Database db=getDbInfoProvider();
    	ISQLDatabaseMetaData dbmd=db.getDatabaseMetaData();
        boolean changed=false; //�Ƿ��б仯
        HashMap<Object,Object> temp=new HashMap<Object,Object>();
        
        int childCount=node.getChildCount();
        for(int i=0;i<childCount;i++)
        {
            DefaultTreeNode tmp=(DefaultTreeNode)node.getChildAt(i);       
            if(!(tmp.getUserObject() instanceof SQLGroupNode))  //���ýڵ�Ϊģʽ�ڵ㣬�����ִ��sql��Ͻڵ����
                temp.put(((Identifier)tmp.getUserObject()).getContent(),tmp);
        }
        
    	if(dbmd.supportsCatalogs())
    	{
    		String[] catalogs=dbmd.getCatalogs();
    		for(int i=0;i<catalogs.length;i++)
    		{
    			DefaultTreeNode tmp=(DefaultTreeNode)(temp.remove(catalogs[i]));
    			 if(tmp==null)//�÷���Ϊ������
    	            {
    	                String s=catalogs[i];
    	                CatalogNode sh=new CatalogNode(s,this,new Catalog(s));
    	                if(filter!=null&&!filter.filter(sh))
    	                	continue;
    	                
    	                node.addChild(sh,1);  //���˵�recentsql�ڵ�
    	                changed=true;
    	            }else
    	            {
    	            	if(filter!=null&&!filter.filter(tmp.getUserObject()))//The node will be removed
    					{
    						temp.put(catalogs[i], tmp);
    					}
    	            }
    		}
    	}else
    	{
    		Schema[] schemas = db.getSchemas(null); // ��ȡ��ݿ������ģʽ��
			for (int i = 0; i < schemas.length; i++) {
				DefaultTreeNode tmp = (DefaultTreeNode) (temp.remove(schemas[i].getName()));
				if (tmp == null)// ��ģʽΪ������
				{
					String s = (String) schemas[i].getName();
					SchemaNode sh = new SchemaNode(s, this, schemas[i]);
					if(filter!=null&&!filter.filter(sh))
	                	continue;
					
					node.addChild(sh, 1); // ���˵�recentsql�ڵ�
					changed = true;
				}else
				{
					if(filter!=null&&!filter.filter(tmp.getUserObject()))//The node will be removed
					{
						temp.put(schemas[i].getName(), tmp);
					}
				}
			}
    	}
        for(Iterator<Object> it = temp.values().iterator(); it.hasNext();) //ɾ����ڵķ���/ģʽ
        {
            node.remove((DefaultTreeNode)it.next());
            changed = true;
        }
        if(changed)
        	BookmarkTreeUtil.getInstance().refreshBookmarkTree(node); //ˢ�½ڵ���ģ��
        
        for(int i=0;i<node.getChildCount();i++)  //�ݹ�ˢ��
        {
            DefaultTreeNode tmpNode=(DefaultTreeNode)node.getChildAt(i);
            Identifier id=(Identifier)tmpNode.getUserObject();
            id.refresh(tmpNode,tmpNode.getNodeFilter());
        }
    }
    public int getConnectState() {
        return connectState;
    }
    public void setConnectState(int connectState) {
        this.connectState = connectState;
    }
    /**
     * Return the provider for meta information of database. 
     * @throws UnifyException  --Such exception will be thrown if there is no database preference in Bookmark object.
     */
    public Database getDbInfoProvider() throws UnifyException {
        if(database==null)
            throw new UnifyException(PublicResource.getString("bookmark.nodatabase"));
        return database;
    }
    
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Override
    public void property() throws UnifyException, SQLException {
        try {
            Operatable operator=OperatorFactory.getOperator(BookMarkPropertyOperator.class);
            operator.operate(this);
        } catch (ClassNotFoundException e) {
            LogProxy.internalError(e);
        } catch (InstantiationException e) {
            LogProxy.internalError(e);
        } catch (IllegalAccessException e) {
            LogProxy.internalError(e);
        }
    }
    /**
     * Return whether type of database is MySql 
     */
    public boolean isMysql()
    {
    	if(driver==null)
    		return false;
    	return driver.isMysql();
    }
}
