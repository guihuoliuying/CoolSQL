package com.cattsoft.coolsql.sql.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cattsoft.coolsql.adapters.DatabaseAdapter;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.sql.SQLMetaDataResults;

public abstract class EntityImpl implements Entity {
    public static final int INDEX_METADATA_INDEX_NAME = 6;

    public static final int INDEX_METADATA_COLUMN_NAME = 9;

    public static final int INDEX_METADATA_ASC_OR_DESC = 10;

    public static final int PRIMARY_KEYS_METADATA_COLUMN_NAME = 4;

    public static final int PRIMARY_KEYS_METADATA_KEY_SEQ = 5;

    public static final int COLUMN_METADATA_COLUMN_NAME = 4;

    public static final int COLUMN_METADATA_SCHEMA = 2;
    public static final int COLUMN_METADATA_TABLE = 3;
    
    public static final int COLUMN_METATDATA_DATA_TYPE = 5;

    public static final int COLUMN_METATDATA_TYPE_NAME = 6;

    public static final int COLUMN_METADATA_COLUMN_SIZE = 7;

    public static final int COLUMN_METADATA_DECIMAL_DIGITS = 9;

    public static final int COLUMN_METADATA_NUM_PREC_RADIX  = 10;
    public static final int COLUMN_METADATA_NULLABLE   = 11;
    public static final int COLUMN_METADATA_REMARKS = 12;

    public static final int COLUMN_METADATA_COLUMN_DEF = 13;
    public static final int COLUMN_METADATA_ORDINAL_POSITION = 17;

    public static final int COLUMN_METADATA_IS_NULLABLE = 18;

    public static final int COLUMN_METADATA_CHAR_OCTET_LENGTH = 16;
    private String catalog;
    private String schema;

    private String name;

    private String type;

    private String remark;
    
    private Bookmark bookmark;

    private Boolean exists;

    private boolean isSynonym;

    private Column columns[];
    public EntityImpl(Bookmark bookmark, String catalog, String schema, String name,
            String type, String remark, boolean isSynonym) {
        exists = Boolean.TRUE;
        this.isSynonym = false;
        columns = null;
        this.catalog=catalog;
        this.schema = schema;
        this.name = name;
        this.type = type;
        this.bookmark = bookmark;
        this.isSynonym = isSynonym;
        this.remark = remark;
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }
    public String getCatalog()
    {
    	return catalog;
    }
    public String getType() {
        return type;
    }

    public String getRemark() {
    	return remark;
    }
    
    public String getQualifiedName() {
    	
    	try {
			return SqlUtil.generateQualifiedName(catalog, schema, name, bookmark);
		} catch (UnifyException e) {
			e.printStackTrace();
			return name;
		}
    }

    public Column getColumn(String columnName) throws UnifyException,
            SQLException {
        Column column = null;
        if (columns == null)
            columns = getColumns();
        int i = 0;
        for (int length = columns != null ? columns.length : 0; column == null
                && i < length; i++)
            if (columnName != null && columnName.equals(columns[i].getName()))
                column = columns[i];

        return column;
    }
    public void refresh() throws UnifyException, SQLException
    {
        columns=null;
        getColumns();
    }
    /**
     * �Ӹ�ʵ���ڻ�ȡ���е�����Ϣ
     */
    public Column[] getColumns() throws UnifyException, SQLException {
        if (columns != null)
            return columns;
        Connection connection = bookmark.getConnection();
        try {
            columns = getColumnsFromMetaData(connection);
            return columns;
        } catch (SQLException e) {
            if ("S1C00".equals(e.getSQLState())
                    && "JDBC_ODBC_BRIDGE".equals(getBookmark().getDriver()
                            .getType())) {
                columns = getColumnsFromQuery(connection);
                return columns;
            } else {
                throw e;
            }
        }
    }

    /**
     * ����ݿ�Ԫ����л�ȡʵ�������е�����Ϣ
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    private Column[] getColumnsFromMetaData(Connection connection)
            throws SQLException {
        Map temp = new HashMap();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getColumns(getCatalog(), getSchema(), getName(),
                null);
        try {
            while (resultSet.next()) {
                ColumnImpl column = new ColumnImpl(this, resultSet
                        .getString(COLUMN_METADATA_COLUMN_NAME), resultSet
                        .getString(COLUMN_METATDATA_TYPE_NAME), resultSet
                        .getInt(COLUMN_METATDATA_DATA_TYPE), resultSet
                        .getInt(COLUMN_METADATA_COLUMN_SIZE), resultSet
                        .getInt(COLUMN_METADATA_DECIMAL_DIGITS), "YES"
                        .equalsIgnoreCase(resultSet
                                .getString(COLUMN_METADATA_IS_NULLABLE)),
                        resultSet.getInt(COLUMN_METADATA_ORDINAL_POSITION),
                        resultSet.getString(COLUMN_METADATA_REMARKS),
                        resultSet.getInt(COLUMN_METADATA_NUM_PREC_RADIX),
                        resultSet.getInt(COLUMN_METADATA_CHAR_OCTET_LENGTH),
                        resultSet.getString(COLUMN_METADATA_COLUMN_DEF),
                        resultSet.getInt(COLUMN_METADATA_NULLABLE));
                temp.put(column.getName(), column);
            }
        } finally {
            resultSet.close();
            resultSet=null;
        }
        
        try {
        	resultSet = metaData.getPrimaryKeys(getCatalog(), getSchema(), getName());
            while (resultSet.next()) {
                String name = resultSet.getString(PRIMARY_KEYS_METADATA_COLUMN_NAME);
                short keySequence = resultSet.getShort(PRIMARY_KEYS_METADATA_KEY_SEQ);
                ColumnImpl column = (ColumnImpl) temp.get(name);
                if (column != null)
                    column.setPrimaryKeyOrder(keySequence);
            }
            List columnList = Collections.synchronizedList(new ArrayList(temp
                    .values()));
            Collections.sort(columnList);
            Column acolumn[] = (Column[]) columnList
                    .toArray(new Column[columnList.size()]);
            return acolumn;
        }catch(SQLException e)
        {
        	if("IM001".equals(e.getSQLState())&&"JDBC_ODBC_BRIDGE".equals(getBookmark().getDriver().getType()))
        	{
        		try {
					Index[] indexs=getIndexes();
					for(int i=0;i<indexs.length;i++)
					{
						if("PrimaryKey".equalsIgnoreCase(indexs[i].getName()))
						{
							for(int j=0;j<indexs[i].getNumberOfColumns();j++)
							{
								String columnName=indexs[i].getColumnName(j);
								ColumnImpl column = (ColumnImpl) temp.get(columnName);
				                if (column != null)
				                    column.setPrimaryKeyOrder(j);
							}
						}
					}
					List columnList = Collections.synchronizedList(new ArrayList(temp
		                    .values()));
		            Collections.sort(columnList);
		            Column acolumn[] = (Column[]) columnList
		                    .toArray(new Column[columnList.size()]);
		            return acolumn;
				} catch (Exception e1) {
					List columnList = Collections.synchronizedList(new ArrayList(temp
		                    .values()));
		            Collections.sort(columnList);
		            Column acolumn[] = (Column[]) columnList
		                    .toArray(new Column[columnList.size()]);
		            return acolumn;
				}
        		
        	}else
        		throw e;
        }
        finally {
        	if(resultSet!=null)
        		resultSet.close();
        }
    }

    /**
     * ��ͨ����ݿ�Ԫ����޷���ȡʵ������Ϣ��ʱ��ŵ��ø÷����� �÷�����ͨ��ֱ�Ӳ�ѯ����ݣ��ò�ѯ��������Ϊfalse����ѯ���Ϊ0����ݡ�
     * 
     * @param connection
     * @return
     * @throws SQLException
     * @throws UnifyException
     */
    private Column[] getColumnsFromQuery(Connection connection)
            throws SQLException, UnifyException {
        List temp = new ArrayList();
        SQLMetaDataResults results = (SQLMetaDataResults) getBookmark()
                .getDbInfoProvider().getMetaData(this, connection);
        com.cattsoft.coolsql.sql.SQLResultSetResults.Row rows[] = results.getRows();
        int i = 0;
        for (int length = results.getRowCount(); i < length; i++) {
            ColumnImpl column = new ColumnImpl(this, (String) rows[i].get(1),
                    (String) rows[i].get(2), ((Integer) rows[i].get(7))
                            .intValue(), ((Long) rows[i].get(3)).longValue(),
                    ((Integer) rows[i].get(4)).intValue(), "Nullable"
                            .equalsIgnoreCase((String) rows[i].get(5)), i + 1,
                     //assign some values that is not real
                    "",2,((Integer) rows[i].get(3)).intValue(),"",0);
            temp.add(column);
        }

        return (Column[]) temp.toArray(new Column[temp.size()]);
    }

    private String getComments(String iniComment, String tableName,
            String columnName) throws UnifyException, SQLException {
        if (iniComment != null && iniComment.length() > 0)
            return iniComment;
        String comment = "";
            Connection con = bookmark.getConnection();
            DatabaseAdapter adapter = bookmark.getAdapter();
            Statement stmt = con.createStatement();
            try {
                if (adapter != null
                        && stmt != null
                        && adapter.getCommentsQuery(tableName, columnName) != null) {
                    stmt.execute(adapter
                            .getCommentsQuery(tableName, columnName));
                    ResultSet set = stmt.getResultSet();
                    try {
                        if (set.next())
                            comment = set.getString(1);
                    } finally {
                        set.close();
                    }
                }
            } finally {
                stmt.close();
            }
        
        return comment;
    }

    public Index[] getIndexes() throws UnifyException, SQLException{
        List indexList = new ArrayList();
        Map temp = new HashMap();
            Connection connection = bookmark.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet;
            IndexImpl index;
            String columnName;
            String ascending;
            int odinalPosition=-1;
            for (resultSet = metaData.getIndexInfo(getCatalog(), getSchema(),
                    getName(), false, false); resultSet.next(); index
                    .addColumn(columnName, ascending != null ? ascending
                            .toUpperCase().startsWith("A") ? Boolean.TRUE
                            : Boolean.FALSE : null,odinalPosition)) {
                String indexName = resultSet.getString(6);
                index = (IndexImpl) temp.get(indexName);
                if (index == null) {
                	boolean isNonUnique=resultSet.getBoolean(4);
                    index = new IndexImpl(this, indexName,isNonUnique);
                    temp.put(indexName, index);
                }
                columnName = resultSet.getString(9);
                ascending = resultSet.getString(10);
                odinalPosition=resultSet.getInt(8);
            }

            resultSet.close();
            indexList.addAll(temp.values());
        
        return (Index[]) indexList.toArray(new Index[indexList.size()]);
    }

    public Boolean exists() {
        return exists;
    }

    public String getQuotedTableName() {
        return getBookmark().getAdapter().filterTableName(getQualifiedName());
    }

    public ForeignKey[] getExportedKeys() throws SQLException, UnifyException {
        return bookmark.getDbInfoProvider().getExportedKeys(getCatalog(),getSchema(), getName());
    }

    public ForeignKey[] getImportedKeys() throws SQLException, UnifyException {
        return bookmark.getDbInfoProvider().getImportedKeys(getCatalog(),getSchema(), getName());
    }

    public ForeignKey[] getReferences() throws SQLException, UnifyException {
        ForeignKey importedKeys[] = getImportedKeys();
        ForeignKey exportedKeys[] = getExportedKeys();
        List list = new ArrayList();
        int i = 0;
        for (int length = importedKeys != null ? importedKeys.length : 0; i < length; i++)
            list.add(importedKeys[i]);

        i = 0;
        for (int length = exportedKeys != null ? exportedKeys.length : 0; i < length; i++)
            if (!list.contains(exportedKeys[i]))
                list.add(exportedKeys[i]);

        return (ForeignKey[]) list.toArray(new ForeignKey[list.size()]);
    }
    public int compareTo(Entity that) {
        if (that.getQualifiedName() == null && getQualifiedName() != null)
            return 1;
        if (getQualifiedName() == null && that.getQualifiedName() != null)
            return -1;
        if (getQualifiedName() == null && that.getQualifiedName() == null)
            return 0;
        else
            return getQualifiedName().compareTo(that.getQualifiedName());
    }

    public boolean isSynonym() {
        return isSynonym;
    }

    public String toString()
    {
        return getName();
    }
    public boolean equals(Object ob)
    {
    	if(ob==null)
    		return false;
    	if(!(ob instanceof EntityImpl))
    		return false;
    	EntityImpl tmp=(EntityImpl)ob;
    	if(bookmark!=tmp.bookmark)  //�˴�����ǩ�������ʹ��ͬһ������
    		return false;
    	if(catalog==null)
    	{
    		if(tmp.catalog!=null)
    			return false;
    	}else
    	{
    		if(tmp.catalog==null||!catalog.equals(tmp.catalog))
    			return false;
    	}
    	if(schema==null)
    	{
    		if(tmp.schema!=null)
    			return false;
    	}else
    	{
    		if(tmp.schema==null||!schema.equals(tmp.schema))
    			return false;
    	}
    	if(name==null)
    	{
    		if(tmp.name!=null)
    			return false;
    	}else
    	{
    		if(tmp.name==null||!name.equals(tmp.name))
    			return false;
    	}
    	if(type==null)
    	{
    		if(tmp.type!=null)
    			return false;
    	}else
    	{
    		if(tmp.type==null||!type.equals(tmp.type))
    			return false;
    	}
    	//columns���Լ������־���ԾͲ����ж���
    	return true;
    }
}
