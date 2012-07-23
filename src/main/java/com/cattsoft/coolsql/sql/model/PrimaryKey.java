/**
 * 
 */
package com.cattsoft.coolsql.sql.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-1-12 create
 */
public class PrimaryKey implements IPrimaryKey {

	private String catalog;
	private String schema;
	private String table;
	private String pkName;
	private List<String> columns;
	private List<Short> ksList;
//	private List keySequence;
	
	public PrimaryKey()
	{
		this(null,null,null,null,null,null);
	}
	public PrimaryKey(String catalog,String schema,String table,String pkName)
	{
		this(catalog,schema,table,pkName,null,null);
	}
	public PrimaryKey(String catalog,String schema,String table,String pkName,List<String> columns,List<Short> ksList)
	{
		this.catalog=catalog;
		this.schema=schema;
		this.table=table;
		this.pkName=pkName;
		this.columns=columns;
		this.ksList=ksList;
		if(this.ksList==null)
			this.ksList=new ArrayList<Short>();
		if(this.columns==null)
			this.columns=new ArrayList<String>();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IPrimaryKey#addColumn(java.lang.String)
	 */
	public void addColumn(String column,short ks) {
		columns.add(column);
		ksList.add(ks);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IPrimaryKey#getCatalog()
	 */
	public String getCatalog() {
		return catalog;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IPrimaryKey#getColumns()
	 */
	public String getColumn(int i) {
		return columns.get(i);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IPrimaryKey#getKeySequence()
	 */
	public short getKeySequence(int i) {
		
		return ksList.get(i);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IPrimaryKey#getSchema()
	 */
	public String getSchema() {
		
		return schema;
	}

	/* (non-Javadoc)
	 * @see com.coolsql.sql.model.IPrimaryKey#getTable()
	 */
	public String getTable() {
		
		return table;
	}
	public String getPkName()
	{
		return pkName;
	}
	public int getNumberOfColumns()
	{
		 return columns.size();
	}
}
