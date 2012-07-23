package com.cattsoft.coolsql.sql.model;

import com.cattsoft.coolsql.sql.util.TypesHelper;

/**
 * Data type definition
 * @author liu_xlin
 */
public class DataType {

    public DataType(int javaType, String databaseTypeName, long precision,
            String literalPrefix, String literalSuffix, String createParameters) {
        this.javaType = javaType;
        this.databaseTypeName = databaseTypeName;
        this.precision = precision;
        this.literalPrefix = literalPrefix;
        this.literalSuffix = literalSuffix;
        this.createParameters = createParameters;
    }

    public String getDatabaseTypeName() {
        return databaseTypeName;
    }

    public int getJavaType() {
        return javaType;
    }

    public String getJavaNameType() {
        return TypesHelper.getTypeName(javaType);
    }

    public String getCreateParameters() {
        return createParameters;
    }

    public String getLiteralPrefix() {
        return literalPrefix;
    }

    public String getLiteralSuffix() {
        return literalSuffix;
    }

    public long getPrecision() {
        return precision;
    }

    private final int javaType;  //java type responds to data type.

    private final String databaseTypeName;

    private final long precision;

    private final String literalPrefix;

    private final String literalSuffix;

    private final String createParameters;
}
