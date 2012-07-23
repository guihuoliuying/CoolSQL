package com.cattsoft.coolsql.adapters;

import java.util.HashMap;
import java.util.Map;

public class MSSQLServerAdapter extends GenericAdapter
{

    protected MSSQLServerAdapter()
    {
        super("MS_SQL_SERVER");
    }

    public Map<String,String> getDefaultConnectionParameters()
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("port", "1433");
        map.put("hostname", "localhost");
        return map;
    }
}
