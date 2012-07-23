package com.cattsoft.coolsql.sql.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.util.SqlUtil;

/**
 * 
 * @author liu_xlin ��ʵ��bean
 */
public class TableImpl extends EntityImpl implements Table {

	private PrimaryKey pkInfoForCache;//Used to cache the primary keys information.
    public TableImpl(Bookmark bookmark,String catalog, String schema, String name, String remark,
            boolean isSynonym) {
        super(bookmark,catalog, schema, name, SqlUtil.TABLE,remark, isSynonym);
    }
    public TableImpl(Bookmark bookmark,String catalog, String schema, String name,String typeName,
    		String remark, boolean isSynonym) {
        super(bookmark,catalog, schema, name, typeName, remark, isSynonym);
    }
    public Integer getSize() {
        Integer size = null;
        try {
            size = new Integer(getBookmark().getDbInfoProvider().getSize(getBookmark(),
                    getBookmark().getConnection(), getQualifiedName(),
                    getBookmark().getAdapter()));
        } catch (SQLException sqlexception) {
        } catch (UnifyException connectionexception) {
        }
        return size;
    }

    public void deleteAllRows() throws SQLException, UnifyException {
        String sql = "DELETE FROM " + getQualifiedName();
        getBookmark().getDbInfoProvider().execute( getBookmark().getConnection(),
                sql);
    }
    public PrimaryKey getPrimaryKey() throws SQLException, UnifyException
    {
    	if(pkInfoForCache==null)
    	{
	    	DatabaseMetaData metaData=getBookmark().getConnection().getMetaData();
	    	PrimaryKey pk=null;
	    	try
	    	{
		    	ResultSet resultSet = metaData.getPrimaryKeys(getCatalog(), getSchema(), getName());
		        while (resultSet.next()) {
		            String name = resultSet.getString(PRIMARY_KEYS_METADATA_COLUMN_NAME);
		            short keySequence = resultSet.getShort(PRIMARY_KEYS_METADATA_KEY_SEQ);
		            if(pk==null)
		            {
		            	pk=new PrimaryKey(resultSet.getString(1),//catalog
		            			resultSet.getString(2),//schema
		            			resultSet.getString(3), //tablename
		            			resultSet.getString(6));//pkname
		            }
		            pk.addColumn(name, keySequence);
		        }
		        if(pk==null)
		        {
		        	pk=new PrimaryKey(getCatalog(),getSchema(),getName(),null);
		        }
	    	}catch(SQLException e)
	    	{
	    		if(!("IM001".equals(e.getSQLState())))
	    			throw e;
	        	boolean isJdbcOdbc = metaData.getURL().startsWith("jdbc:odbc:");
	        	if(isJdbcOdbc)//Only for jdbc:odbc bridge .
	        	{
	        		Index[] indexs=getIndexes();
	        		for(int i=0;i<indexs.length;i++)
	    			{
	    				if("PrimaryKey".equalsIgnoreCase(indexs[i].getName()))
	    				{
	    					for(int j=0;j<indexs[i].getNumberOfColumns();j++)
	    					{
	    						if(pk==null)
	    			            {
	    			            	pk=new PrimaryKey(getCatalog(),//catalog
	    			            			getSchema(),//schema
	    			            			getName(), //tablename
	    			            			"PrimaryKey");//pkname
	    			            }
	    						pk.addColumn(indexs[i].getColumnName(j), (short)j);
	    					}
	    				}
	    			}
	        		if(pk==null)
	        	    {
	        	        pk=new PrimaryKey(getCatalog(),getSchema(),getName(),null);
	        	    }
	        	}
	    	}
	    	pkInfoForCache=pk;
    	}
    	return pkInfoForCache;
    }
}
