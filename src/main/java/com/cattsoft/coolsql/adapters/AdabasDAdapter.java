package com.cattsoft.coolsql.adapters;


public class AdabasDAdapter extends DatabaseAdapter
{

    protected AdabasDAdapter()
    {
        super("ADABASD");
    }

    public String getShowTableQuery(String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT OWNER, TABLENAME FROM TABLES WHERE OWNER = '" + qualifier.toUpperCase() + "'";
    }

    public String getShowViewQuery(String qualifier)
    {
        if(qualifier==null)
            return null;
        return "SELECT OWNER, VIEWNAME FROM VIEWS WHERE OWNER = '" + qualifier.toUpperCase() + "'";
    }
}
