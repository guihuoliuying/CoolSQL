
package com.cattsoft.coolsql.sql;
/**
 * 
 * @author liu_xlin
 *
 */
public class SQLUpdateResults extends SQLResults
{

    public SQLUpdateResults(int updateCount)
    {
        this.updateCount = 0;
        this.updateCount = updateCount;
    }

    public int getUpdateCount()
    {
        return updateCount;
    }

    public boolean isResultSet()
    {
        return false;
    }

    private int updateCount;
}
