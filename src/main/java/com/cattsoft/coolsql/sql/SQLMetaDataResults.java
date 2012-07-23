package com.cattsoft.coolsql.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.sql.model.Entity;
/**
 * 
 * @author liu_xlin
 * 
 */
public class SQLMetaDataResults extends SQLResultSetResults
{

    private SQLMetaDataResults(String query, Bookmark bookmark, Entity entity[])
    {
        super(query, bookmark, entity);
    }

    static SQLResultSetResults create(Bookmark bookmark, ResultSet set, String query, Entity entity[])
        throws SQLException
    {
        SQLMetaDataResults results = new SQLMetaDataResults(query, bookmark, entity);
        results.parseResultSet(set);
        return results;
    }

    protected void parseResultSet(ResultSet set)
        throws SQLException
    {
        List columns = new ArrayList();
        columns.add(new SQLResultSetResults.Column("ColumnName", "", 0,0,false));
        columns.add(new SQLResultSetResults.Column("Type Name", "", 0,0,false));
        columns.add(new SQLResultSetResults.Column("Size", "", 0,0,false));
        columns.add(new SQLResultSetResults.Column("Scale", "", 0,0,false));
        columns.add(new SQLResultSetResults.Column("Nullable", "", 0,0,false));
        columns.add(new SQLResultSetResults.Column( "AutoIncrement", "", 0,0,false));
        columns.add(new SQLResultSetResults.Column("Type", "", 0,0,false));
        setColumns((SQLResultSetResults.Column[])columns.toArray(new SQLResultSetResults.Column[columns.size()]));
        ResultSetMetaData metaData = set.getMetaData();
        List rowList = new ArrayList();
        for(int i = 1; i <= metaData.getColumnCount(); i++)
        {
            Vector row = new Vector();
            row.addElement(metaData.getColumnName(i));
            row.addElement(metaData.getColumnTypeName(i));
            long precision = 0L;
            try
            {
                precision = metaData.getPrecision(i);
            }
            catch(Throwable throwable) { }
            if(precision == 0L)
                precision = metaData.getColumnDisplaySize(i);
            int scale = metaData.getScale(i);
            row.addElement(new Long(precision));
            row.addElement(new Integer(scale));
            int nullable = metaData.isNullable(i);
            if(nullable == 0)
                row.addElement("Not Null");
            else
            if(nullable == 1)
                row.addElement("Nullable");
            else
            if(nullable == 2)
                row.addElement("Nullable");
            else
                row.addElement("<Error>");
            row.addElement((metaData.isAutoIncrement(i) ? Boolean.TRUE : Boolean.FALSE).toString());           
            row.addElement(new Integer(metaData.getColumnType(i)));
            rowList.add(new SQLResultSetResults.Row( row));
        }

        setRows((SQLResultSetResults.Row[])rowList.toArray(new SQLResultSetResults.Row[rowList.size()]));
    }

    public boolean isMetaData()
    {
        return true;
    }
}
