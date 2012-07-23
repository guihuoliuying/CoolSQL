package com.cattsoft.coolsql.adapters;

import java.util.HashMap;
import java.util.Map;

import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.sql.util.Messages;

/**
 * 
 * @author liu_xlin
 *oracle��ݿ�������
 */
public class OracleAdapter extends DatabaseAdapter
{

    protected OracleAdapter()
    {
        super("ORACLE");
    }
//    @Override
//    public String getShowSequenceQuery(String qualifier)
//    {
//        if(qualifier==null)
//            return null;
//        return "SELECT SEQUENCE_OWNER, SEQUENCE_NAME FROM ALL_SEQUENCES WHERE SEQUENCE_OWNER = '" + qualifier + "'";
//    }
    @Override
    public String getShowSequenceQuery(String catalog,String schema)
    {
    	 if(catalog==null&&schema==null)
             return null;
        return "SELECT SEQUENCE_OWNER, SEQUENCE_NAME FROM ALL_SEQUENCES WHERE SEQUENCE_OWNER = '" + schema + "'";
    }
    public String getPrevValue(String sequence, String owner)
    {
        return "SELECT " + getQualifiedName(owner, sequence) + ".CURRVAL FROM DUAL";
    }

    public String getNextValue(String sequence, String owner)
    {
        return "SELECT " + getQualifiedName(owner, sequence) + ".NEXTVAL FROM DUAL";
    }

    public String getCommentsQuery(String tableName, String column)
    {
        if(tableName==null||column==null)
            return null;
        String query = "SELECT COMMENTS FROM ALL_COL_COMMENTS WHERE TABLE_NAME = '";
        query = query + SqlUtil.getTableName(tableName) + "' AND COLUMN_NAME = '" + column + "'";
        if(!SqlUtil.getSchemaName(tableName).equals(""))
            query = query + " AND OWNER = '" + SqlUtil.getSchemaName(tableName) + "'";
        return query;
    }

    public String quote(String string, int type, String typeString)
    {
        if(type == 91 || type == 93)
        {
            string = string.trim();
            if(string.length() > 1)
            {
                String sub = string.substring(string.length() - 2, string.length() - 1);
                if(sub.equals(Messages.getString(".")))
                    string = string.substring(0, string.length() - 2);
            }
            return "TO_DATE('" + string + "','yyyy-mm-dd hh24:mi:ss')";
        } else
        {
            return super.quote(string, type, typeString);
        }
    }

    public String filterTableName(String tableName)
    {
        if(tableName.equals(tableName.toUpperCase()))
            return tableName;
        if(SqlUtil.getSchemaName(tableName).equals(""))
            return "\"" + tableName + "\"";
        else
            return SqlUtil.getSchemaName(tableName) + ".\"" + SqlUtil.getTableName(tableName) + "\"";
    }

    public String getDefaultSchema(String userid)
    {
        return super.getDefaultSchema(userid).toUpperCase();
    }

    public Map<String,String> getDefaultConnectionParameters()
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("port", "1521");
        map.put("hostname", "localhost");
        return map;
    }

    public String getShowSynonymsQuery(String schema, String type)
    {
        if(schema==null||type==null)
            return null;
        String schemaCondition="";
        if(schema!=null)
            schemaCondition="ALL_SYNONYMS.OWNER = '" + schema + "' and " ;
        return "select SYNONYM_NAME from ALL_SYNONYMS, ALL_OBJECTS where  "+schemaCondition+" ALL_SYNONYMS.TABLE_OWNER = ALL_OBJECTS.OWNER" + " and ALL_SYNONYMS.TABLE_NAME = ALL_OBJECTS.OBJECT_NAME" + " and ALL_OBJECTS.OBJECT_TYPE = '" + type + "'";
    }
}
