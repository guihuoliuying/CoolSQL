package com.cattsoft.coolsql.adapters;

import java.util.HashMap;
import java.util.Map;

import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.sql.util.TypesHelper;

public class PostgresAdapter extends DatabaseAdapter
{

    protected PostgresAdapter()
    {
        super("POSTGRES");
    }

    public String getShowTableQuery(String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT SCHEMANAME, TABLENAME FROM PG_TABLES WHERE SCHEMANAME = '" + qualifier + "'";
    }

    public String getShowViewQuery(String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT SCHEMANAME, VIEWNAME FROM PG_VIEWS WHERE SCHEMANAME = '" + qualifier + "'";
    }

    public String getShowSequenceQuery(String qualifier)
    {
        if(qualifier==null)
            return null;
        return "select pg_namespace.nspname, relname from pg_class, pg_namespace where relkind = 'S' and relnamespace = pg_namespace.oid and pg_namespace.nspname = '" + qualifier + "'";
    }

    public String getNextValue(String sequence, String owner)
    {
        return "SELECT NEXTVAL('" + getQualifiedName(owner, sequence) + "')";
    }

    public String getPrevValue(String sequence, String owner)
    {
        return "SELECT * FROM " + getQualifiedName(owner, sequence);
    }

    public String quote(String string, int type, String typeString)
    {
        if(type == -7 || type == TypesHelper.BOOLEAN)
        {
            if(string.indexOf('t') >= 0 || string.indexOf('T') >= 0)
                return "true";
            if(string.indexOf('f') >= 0 || string.indexOf('F') >= 0)
                return "false";
            else
                return string;
        } else
        {
            return super.quote(string, type, typeString);
        }
    }

    public String getTableQuery(String table)
    {
        return "SELECT * FROM " + filterTableName(table);
    }

    public String filterTableName(String tableName)
    {
        if(tableName.equals(tableName.toLowerCase()))
            return tableName;
        if(SqlUtil.getSchemaName(tableName).equals(""))
            return "\"" + tableName + "\"";
        else
            return "\"" + SqlUtil.getSchemaName(tableName) + "\".\"" + SqlUtil.getTableName(tableName) + "\"";
    }

    public String getDefaultSchema(String userid)
    {
        return "public";
    }

    public Map<String,String> getDefaultConnectionParameters()
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("port", "5432");
        map.put("hostname", "localhost");
        return map;
    }
}
