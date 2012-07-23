package com.cattsoft.coolsql.adapters;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author liu_xlin
 *DB2��ݿ���Ϣ������
 */
public class DB2Adapter extends DatabaseAdapter
{

    protected DB2Adapter()
    {
        super("DB2");
    }
    public String getTableQuery(String catalog,String schema,String tabName)
    {
        if(tabName==null)
            return null;
        String schemaCondition="";
        if(schema!=null)
            schemaCondition=" tabschema = '"+schema.toUpperCase()+"' and ";
        return "SELECT tabschema, TABNAME FROM syscat.tables WHERE "+schemaCondition+" tabName='"+tabName+"' AND TYPE='T'";
    }
    public String getShowTableQuery(String catalog,String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT tabschema, TABNAME FROM syscat.tables WHERE tabschema = '" + qualifier.toUpperCase() + "' AND TYPE='T'";
    }
    public String getViewQuery(String catalog,String qualifier,String viewName)
    {
        if(viewName==null)
            return null;
        String schemaCondition="";
        if(qualifier!=null)
            schemaCondition=" tabschema = '"+qualifier.toUpperCase()+"' and ";
        return "SELECT tabschema, TABNAME FROM syscat.tables WHERE "+schemaCondition+" tabName='"+viewName+"' AND TYPE='V'";
    }
    public String getShowViewQuery(String catalog,String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT tabschema, TABNAME FROM syscat.tables WHERE tabschema = '" + qualifier.toUpperCase() + "' AND TYPE='V'";
    }
    public String getSequenceQuery(String catalog,String qualifier,String seqName)
    {
        if(seqName==null)
            return null;
        String schemaCondition="";
        if(qualifier!=null)
            schemaCondition=" seqschema = '"+qualifier.toUpperCase()+"' and ";
        return "SELECT seqschema, SEQNAME FROM sysibm.syssequences WHERE "+schemaCondition+" SEQNAME='"+seqName+"'";
    }
    public String getShowSequenceQuery(String catalog,String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT seqschema, SEQNAME FROM sysibm.syssequences WHERE seqschema = '" + qualifier.toUpperCase() + "'";
    }

    public String getNextValue(String sequence, String owner)
    {
        return "VALUES NEXTVAL FOR " + getQualifiedName(owner, sequence);
    }

    public String getPrevValue(String sequence, String owner)
    {
        return "VALUES PREVVAL FOR " + getQualifiedName(owner, sequence);
    }

    public Map<String,String> getDefaultConnectionParameters()
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("port", "50000");
        map.put("hostname", "localhost");
        return map;
    }
}
