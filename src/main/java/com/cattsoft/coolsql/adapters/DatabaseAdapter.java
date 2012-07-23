package com.cattsoft.coolsql.adapters;

import java.util.HashMap;
import java.util.Map;

import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.util.Messages;
import com.cattsoft.coolsql.sql.util.TypesHelper;

/**
 * 
 * @author liu_xlin
 *��ݿ�����������ݿ���Ϣ�ĳ�����
 */
public abstract class DatabaseAdapter
{

    protected DatabaseAdapter(String type)
    {
        this.type = type;
    }

    public String getDisplayName()
    {
        return Messages.getString(com.cattsoft.coolsql.adapters.DatabaseAdapter.class, getType());
    }
    public String getTableQuery(String catalog,String schema,String tabName)
    {
        return null;
    }
    public String getShowTableQuery(String catalog,String schema)
    {
        return null;
    }
    public String getViewQuery(String catalog,String schema,String viewName)
    {
        return null;
    }
    public String getShowViewQuery(String catalog,String schema)
    {
        return null;
    }
    public String getSequenceQuery(String catalog,String schema,String seqName)
    {
        return null;
    }
    public String getShowSequenceQuery(String catalog,String schema)
    {
        return null;
    }

    public String getTableQuery(String table)
    {
        return "SELECT * FROM " + filterTableName(table);
    }

    public String getNextValue(String sequence, String owner)
    {
        return null;
    }

    public String getPrevValue(String sequence, String owner)
    {
        return null;
    }

    public String getCommentsQuery(String tableName, String columnName)
    {
        return null;
    }

    public String getEmptySetQuery(String table)
    {
        return "SELECT * FROM " + filterTableName(table) + " WHERE (1 = 0)";
    }

    public String quote(String string, int type, String typeString)
    {
        if(isTextType(type, typeString))
            return "'" + StringUtil.substituteString(string, "'", "''") + "'";
        if(type == 91 || type == 93)
        {
            string = string.trim();
            if(string.length() > 2)
            {
                String sub = string.substring(string.length() - 2, string.length() - 1);
                if(string.length() > 1 && sub.equals("."))
                    string = string.substring(0, string.length() - 2);
            }
            return "'" + string + "'";
        } else
        {
            return string;
        }
    }

    protected boolean isTextType(int type, String typeString)
    {
        return TypesHelper.isText(type);
    }

    public String getInsert(String tableName, String namesClause, String valuesClause)
    {
        String query = "INSERT INTO " + filterTableName(tableName);
        if(namesClause != "")
        {
            query = query + " (" + namesClause + ")";
            query = query + " VALUES (" + valuesClause;
            query = query + " )";
        }
        return query;
    }

    public String filterTableName(String tableName)
    {
        return tableName;
    }

    public String getCountQuery(String tableName)
    {
        return "SELECT COUNT(*) FROM " + filterTableName(tableName);
    }

    public void getUpdate(String s, String s1, StringBuffer stringbuffer, String s2)
    {
    }

    public String getUpdate(String tableName, String setClause, String whereClause)
    {
        String query = "UPDATE " + filterTableName(tableName);
        query = query + " SET " + setClause;
        if(!whereClause.equals(""))
            query = query + " WHERE " + whereClause;
        return query;
    }

    public String getDelete(String tableName, String whereClause)
    {
        String query = "DELETE FROM " + filterTableName(tableName);
        if(!whereClause.equals(""))
            query = query + " WHERE " + whereClause;
        return query;
    }

    public String getDefaultSchema(String userid)
    {
        return userid;
    }

    public String getType()
    {
        return type;
    }

    protected String getQualifiedName(String schema, String name)
    {
        return schema == null || schema.length() <= 0 ? name : schema + "." + name;
    }

    public Map<String,String> getDefaultConnectionParameters()
    {
        return new HashMap<String,String>();
    }

    public String getShowSynonymsQuery(String schema, String type)
    {
        return null;
    }

    private final String type;
}
