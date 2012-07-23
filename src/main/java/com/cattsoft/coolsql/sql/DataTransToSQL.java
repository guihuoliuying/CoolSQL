/**
 * Create date:2008-4-29
 */
package com.cattsoft.coolsql.sql;

import java.sql.SQLException;

import javax.swing.JTable;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.DataType;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author Kenny liu
 *
 * 2008-4-29 create
 */
public class DataTransToSQL {

	private JTable table;
	
	private Entity entity;
	
	private Column[] columns;
	public DataTransToSQL(JTable table,Entity entity,Column[] columns)
	{
		this.table=table;
		this.entity=entity;
		this.columns=columns;
	}
	public String createInsertSQLS(boolean isSelectedRows) throws UnifyException, SQLException
	{
		if(table==null||entity==null)
			return "";
		
		if(columns==null)
			columns=entity.getColumns();
		
		Database db =entity.getBookmark().getDbInfoProvider();
		
		StringBuilder headerSB=new StringBuilder("INSERT INTO ");
		headerSB.append(entity.getName()).append(" (");
		
		StringBuilder sb=new StringBuilder();

		int i=0;
		for(;i<table.getColumnCount()-1;i++)
		{
			headerSB.append(table.getColumnName(i)).append(",");
		}
		headerSB.append(table.getColumnName(i)).append(") VALUES (");
		
		if(isSelectedRows)
		{
			int[] selectedRows=table.getSelectedRows();
			if(selectedRows==null||selectedRows.length==0)
				return "";
			for(i=0;i<selectedRows.length;i++)
			{
				StringBuilder tmp=new StringBuilder(headerSB);
				int j=0;
				for(;j<columns.length-1;j++)
				{
					DataType dataType = db.getDataType(columns[j].getTypeName());
					Object value=null;
					if(table instanceof DataSetTable)
					{
						value=((DataSetTable)table).getDisplayData(selectedRows[i], j);
					}else
					{
						value=table.getValueAt(selectedRows[i], j);
					}
					if(dataType==null)
		            {
						tmp.append(value==null?"null":SqlUtil.qualifyColumnValue(value.toString(), columns[j].getType())).append(",");
		            	continue;
		            }
					String realValue = (value == null ? "null"
			                    : (StringUtil.trim(dataType.getLiteralPrefix())
			                            + value.toString()
			                            + StringUtil.trim(dataType.getLiteralSuffix())));
					tmp.append(realValue).append(",");
				}
				DataType dataType = db.getDataType(columns[j].getTypeName());
				Object value=table.getValueAt(selectedRows[i], j);
				if(dataType==null)
	            {
					tmp.append(value==null?"null":SqlUtil.qualifyColumnValue(value.toString(), columns[j].getType()));
	            }else
	            {
					String realValue = (value == null ? "null)"
			                    : (StringUtil.trim(dataType.getLiteralPrefix())
			                            + value.toString()
			                            + StringUtil.trim(dataType.getLiteralSuffix()) + ")"));
					tmp.append(realValue);
	            }
				
				sb.append(tmp);
				sb.append(";\n");
			}
		}else
		{
			for(i=0;i<table.getRowCount();i++)
			{
				StringBuilder tmp=new StringBuilder(headerSB);
				int j=0;
				for(;j<columns.length-1;j++)
				{
					DataType dataType = db.getDataType(columns[j].getTypeName());
					Object value=table.getValueAt(i, j);
					String realValue = (value == null ? "null,"
			                    : (StringUtil.trim(dataType.getLiteralPrefix())
			                            + value.toString()
			                            + StringUtil.trim(dataType.getLiteralSuffix()) + ","));
					tmp.append(realValue);
				}
				DataType dataType = db.getDataType(columns[j].getTypeName());
				Object value=table.getValueAt(i, j);
				String realValue = (value == null ? "null)"
		                    : (StringUtil.trim(dataType.getLiteralPrefix())
		                            + value.toString()
		                            + StringUtil.trim(dataType.getLiteralSuffix()) + ")"));
				tmp.append(realValue);
				
				sb.append(tmp);
				sb.append(";\n");
			}
		}
		return sb.toString();
	}
	/**
	 * @return the table
	 */
	public JTable getTable() {
		return this.table;
	}
	/**
	 * @param table the table to set
	 */
	public void setTable(JTable table) {
		this.table = table;
	}
	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return this.entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
