package com.cattsoft.coolsql.sql;

/**
 * The base class of result of SQL statment execution.
 * @author liu_xlin 
 */
public abstract class SQLResults {

    //SQL statement.
    protected String sql="";
    /**
     * Starting time of execution
     */
    private long time;
    
    /**
     * Time that execution costs.(millisecond)
     */
    private long costTime;
    public SQLResults() {
    }
    public abstract boolean isResultSet();

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    /**
     * @return return sql��
     */
    public String getSql() {
        return sql;
    }
    /**
     * @param sql a SQL statement which is going to be executed.
     */
    public void setSql(String sql) {
        this.sql = sql;
    }
    public long getCostTime() {
        return costTime;
    }
    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }
}
