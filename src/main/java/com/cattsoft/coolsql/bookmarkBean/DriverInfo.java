/*
 * �������� 2006-6-2
 *
 */
package com.cattsoft.coolsql.bookmarkBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.cattsoft.coolsql.adapters.AdapterFactory;
import com.cattsoft.coolsql.main.frame.Launcher;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.URLBuilder;

/**
 * @author liu_xlin
 * �������Ϣ��
 */
public class DriverInfo implements Serializable{
	private static final long serialVersionUID = 5584580757674025281L;
	/**
	 * ���������
	 */
	private String className = null;
    /**
     * ����
     */
	private String type = null;
    /**
     * ����ı���
     */
	private String driverName = null;
    /**
     * �����汾
     */
    private String driverVersion=null;
    /**
     * ���������Դ
     */
	private String filePath = null;
	/**
	 * �������Ӧ�����ӷ�ʽ
	 */
	private String urlPattern=null;
	
	/**
	 * ��ݿ��Ʒ���
	 */
	private String dbProductName=null;
	/**
	 * ��ݿ�汾
	 */
    private String dbPruductVersion=null;   

    /**
     * ��ݿ�����url�еĲ���datasource����dbname��address��port��
     */
    private Map<String,String> params=null;
	public DriverInfo() {
		this(null, null, null, null,null,null);
	}
    public DriverInfo(String className)
    {
    	params=new HashMap<String,String>();
    	this.className=className;
    	if(!Launcher.isInitializing)
    	    createInfoByClass(className);
    }
	public DriverInfo(String driverName, String className, String type,
			String filePath,
			String urlPattern,String version) {
		if (driverName != null) {
			this.driverName = driverName;
		}
		if (className != null) {
			this.className = className;
		}
		if (type != null) {
			this.type = type;
		}
		if (filePath != null) {
			this.filePath = filePath;
		}
		params=new HashMap<String,String>();
		if(urlPattern!=null)
		{
			this.urlPattern=urlPattern;
			AdapterFactory instance=AdapterFactory.getInstance();
			Map<String,String> map=instance.getAdapter(type).getDefaultConnectionParameters();
			createParams(URLBuilder.getVariableNames(urlPattern),map);
		}
	}
	/**
	 * ͨ��������������ݿ�����Ϣ
	 * @param className
	 */
	protected void createInfoByClass(String className)
    {
       if(className==null||className.trim().equals(""))
       {
       	 return ;
       }
       this.className=className;
       AdapterFactory instance=AdapterFactory.getInstance();
       this.setFilePath(LoadJar.getInstance().getLibPath(className));
       this.setType(instance.getAdapterType(className));
       this.setUrlPattern(instance.getURLPattern(className));
       this.setDriverName("");
       this.setDriverVersion("");
       
       params.clear();
       String[] args=URLBuilder.getVariableNames(urlPattern);
       Map<String,String> map=instance.getAdapter(type).getDefaultConnectionParameters();
       this.createParams(args,map);
    }
    /**
     * ��ʼ����ݿ����Ӳ���
     * @param args
     */
    protected void createParams(String[] args,Map<String,String> defaultMap)
    {
    	if(args==null||defaultMap==null)
    		return ;
    	for(int i=0;i<args.length;i++)
    	{
    		if(defaultMap.containsKey(args[i]))
    		    params.put(args[i],defaultMap.get(args[i]));
    		else
    			params.put(args[i],"");
    	}
    }
    /**
     * �Ƿ�ΪMysql��ݿ�
     * @return
     */
    public boolean isMysql()
    {
    	return "MYSQL".equals(type);
    }
    public boolean isJdbcOdbc()
    {
    	return "JDBC_ODBC_BRIDGE".equals(type);
    }
	/**
	 * @return ���� className��
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            Ҫ���õ� className��
	 */
	public void setClassName(String className) {
		this.className = className;
		if(!Launcher.isInitializing)
		    createInfoByClass(className);
	}

	/**
	 * @return ���� driverName��
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName
	 *            Ҫ���õ� driverName��
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return ���� filePath��
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            Ҫ���õ� filePath��
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return ���� type��
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            Ҫ���õ� type��
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return ���� urlPattern��
	 */
	public String getUrlPattern() {
		return urlPattern;
	}
	/**
	 * @param urlPattern Ҫ���õ� urlPattern��
	 */
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	/**
	 * @return ���� version��
	 */
	/**
	 * @return ���� params��
	 */
	public Map<String,String> getParams() {
		return params;
	}
	/**
	 * @param params Ҫ���õ� params��
	 */
	public void setParams(Map<String,String> params) {
	    this.params.clear();
		this.params = params;
	}
	/**
	 * ��ȡԭʼURL
	 * @return
	 */
	public String getOriginalURL()
	{
	    AdapterFactory instance=AdapterFactory.getInstance();
	    return instance.getURLPattern(className);
	}
    /**
     * @return ���� dbProductName��
     */
    public String getDbProductName() {
        return dbProductName;
    }
    /**
     * @param dbProductName Ҫ���õ� dbProductName��
     */
    public void setDbProductName(String dbProductName) {
        this.dbProductName = dbProductName;
    }
    /**
     * @return ���� dbPruductVersion��
     */
    public String getDbPruductVersion() {
        return dbPruductVersion;
    }
    /**
     * @param dbPruductVersion Ҫ���õ� dbPruductVersion��
     */
    public void setDbPruductVersion(String dbPruductVersion) {
        this.dbPruductVersion = dbPruductVersion;
    }
    /**
     * @return ���� driverVersion��
     */
    public String getDriverVersion() {
        return driverVersion;
    }
    /**
     * @param driverVersion Ҫ���õ� driverVersion��
     */
    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }
}
