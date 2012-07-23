package com.cattsoft.coolsql.adapters;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class InformixAdapter extends GenericAdapter
{

    protected InformixAdapter()
    {
        super("INFORMIX");
    }

    public Map<String,String> getDefaultConnectionParameters()
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("hostname", getHostName());
        map.put("informixserver", "ol_" + getHostName());
        map.put("port", "1526");
        return map;
    }

    private String getHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch(UnknownHostException e)
        {
            return "localhost";
        }
    }
}
