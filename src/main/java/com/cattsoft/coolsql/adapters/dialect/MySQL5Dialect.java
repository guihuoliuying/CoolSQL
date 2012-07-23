package com.cattsoft.coolsql.adapters.dialect;

public class MySQL5Dialect extends MySQLDialect {

    /**
     * @see com.coolsql.adapters.dialect.MySQLDialect#supportsProduct(java.lang.String, java.lang.String)
     */
    @Override
    public boolean supportsProduct(String databaseProductName, String databaseProductVersion) {
        if (databaseProductName == null || databaseProductVersion == null) {
            return false;
        }
        if (!databaseProductName.trim().toLowerCase().startsWith("mysql")) {
            return false;
        }
        return databaseProductVersion.startsWith("5");
    }

    
}
