package com.cattsoft.coolsql.adapters;

import java.util.HashMap;
import java.util.Map;

public class PointbaseAdapter extends GenericAdapter
{

    protected PointbaseAdapter()
    {
        super("POINTBASE");
    }

    public Map<String,String> getDefaultConnectionParameters()
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("port", "9093");
        map.put("hostname", "localhost");
        return map;
    }
}
