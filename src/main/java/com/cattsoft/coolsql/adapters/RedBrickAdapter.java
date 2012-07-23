package com.cattsoft.coolsql.adapters;

public class RedBrickAdapter extends DatabaseAdapter
{

    protected RedBrickAdapter()
    {
        super("REDBRICK");
    }

    public String getShowTableQuery(String qualifier)
    {
        if(qualifier==null)
            return null;
        return "select name from rbw_tables where type = 'TABLE'";
    }

    public String getShowViewQuery(String qualifier)
    {
        if(qualifier==null)
            return null;
        return "select name from rbw_tables where type = 'VIEW'";
    }

    public String getDefaultSchema(String userid)
    {
        return null;
    }
}
