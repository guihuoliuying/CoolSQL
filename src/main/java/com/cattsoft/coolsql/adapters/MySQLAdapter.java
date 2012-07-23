package com.cattsoft.coolsql.adapters;

import java.util.HashMap;
import java.util.Map;

public class MySQLAdapter extends GenericAdapter
{

    protected MySQLAdapter()
    {
        super("MYSQL");
    }

    public Map<String,String> getDefaultConnectionParameters()
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("port", "3306");
        map.put("hostname", "localhost");
        return map;
    }
}
