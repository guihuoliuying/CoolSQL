package com.cattsoft.coolsql.adapters;

/**
 * 
 * @author liu_xlin
 *
 */
public class DB2AS400Adapter extends DatabaseAdapter
{

    protected DB2AS400Adapter()
    {
        super("DB2AS400");
    }

    public String getShowTableQuery(String catalog,String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT table_schema, TABLE_NAME FROM QSYS2.SYSTABLES WHERE table_schema  = '" + qualifier.toUpperCase() + "' AND TABLE_TYPE IN ('T', 'P')";
    }

    public String getShowViewQuery(String catalog,String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT table_schema, TABLE_NAME FROM QSYS2.SYSTABLES WHERE table_schema  = '" + qualifier.toUpperCase() + "' AND TABLE_TYPE IN ('V', 'L')";
    }
}
