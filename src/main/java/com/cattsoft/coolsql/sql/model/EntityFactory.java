/*
 * �������� 2006-9-9
 */
package com.cattsoft.coolsql.sql.model;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * This factory is in charge of creating entity.
 * @author liu_xlin
 */
public class EntityFactory {
    private EntityFactory()
    {
    }

    public static synchronized EntityFactory getInstance()
    {
        return instance;
    }

    public Entity create(Bookmark bookmark,String catalog,  String schema, String name, String type, String remark, boolean isSynonym)
    {
        if(type != null)
            type = type.trim();
        catalog=SqlUtil.validateSqlParam(catalog);
        schema=SqlUtil.validateSqlParam(schema);
        if(name==null||name.trim().equals(""))
        	return null;
        if(!checkTableType(bookmark,type))
        	throw new IllegalArgumentException("can't identify the table type :"+type);
        
        if(SqlUtil.TABLE.equals(type))
            return new TableImpl(bookmark, catalog,schema, name, remark, isSynonym);
        if(SqlUtil.VIEW.equals(type))
            return new ViewImpl(bookmark, catalog, schema, name, remark, isSynonym);
        if(SqlUtil.SEQUENCE.equals(type))
            return new SequenceImpl(bookmark, catalog, schema, name, remark, isSynonym);
        if(SqlUtil.SYNONYM.equals(type))
            return new TableImpl(bookmark, catalog, schema, name, remark, true);
        else
        	return new TableImpl(bookmark, catalog,schema, name,type, remark, isSynonym);
    }
    /**
     * Validate whether database contains specified entity type.
     * @param bookmark  bookmark object.
     * @param type  a entity type.
     * @return return true if database contains specified entity type, otherwise return false.
     */
    private boolean checkTableType(Bookmark bookmark,String type)
    {
    	String[] types;
		try {
			types = bookmark.getDbInfoProvider().getDatabaseMetaData().getTableTypes();
		} catch (Exception e) {
			LogProxy.errorMessage(e.getMessage());
			return false;
		}
    	for (int i = 0;i < types.length; i++)
    	{
    		if (types[i].equals(type))
    			return true;
    	}
    	return false;
    }
    private static EntityFactory instance = new EntityFactory();
}
