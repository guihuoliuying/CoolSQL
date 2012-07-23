package com.cattsoft.coolsql.exportdata;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;

/**
 * Export data from database.
 * @author Kenny liu 
 */
public abstract class ExportDBData extends ExportData {

    protected String sql = null;

    public ExportDBData(Bookmark source)
    {
        this(source,null);
    }
    public ExportDBData(Bookmark source, String sql) {
        super(source);
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
