/*
 * �������� 2006-8-30
 *
 */
package com.cattsoft.coolsql.exportdata.excel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import com.cattsoft.coolsql.sql.util.TypesHelper;

/**
 * @author liu_xlin
 *�����˱?ͷ�е���ʾ���
 */
public class CellDefined {
	/**
	 * Ԫ�ط��
	 */
	private HSSFCellStyle cellStyle=null;
	/**
	 * Ԫ������
	 */
	private int type=-1;
	/**
	 * ����
	 */
	private String headerName=null;
	/**
	 * Ĭ��Ϊ��������
	 *
	 */
	/**
	 * ���ڽ���Excel������������Ҫ�����ʽ��ת��,Ĭ���ޱ����ʽ
	 */
	private String encoding=null;
	public CellDefined()
	{
		this(0,"");
	}
	public CellDefined(int type ,String name)
	{
		this(type,name,null);
	}
	public CellDefined(int type,String name,HSSFCellStyle cellStyle)
	{
	  this.cellStyle=cellStyle;	
	  if(type<0||type>5)
	  {
	  	throw new IllegalArgumentException("������󣬱��Ԫ�����Ͳ���ȷ��type="+type);
	  }	  
	  this.type=type;
	  headerName=name;
	}	
	public HSSFCellStyle getCellStyle() {
		return cellStyle;
	}
	public void setCellStyle(HSSFCellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	/**
	 *��ݽ����ȡ������Ӧ�ֶε��ж���
	 * @param set
	 * @return
	 * @throws ExcelProcessException
	 */
	public static CellDefined[] createInstanceOfResult(ResultSet set) throws ExcelProcessException
	{
		if(set==null)
			throw new ExcelProcessException("�޽��");
		try {
			ResultSetMetaData metaData=set.getMetaData();
			int colCount=metaData.getColumnCount();
			CellDefined[] defined=new CellDefined[colCount];
			for(int i=0;i<colCount;i++)
			{
				int type=metaData.getColumnType(i+1);
				if(TypesHelper.isText(type))   //����ֶ�Ϊ�ı�����
				{
					type=HSSFCell.CELL_TYPE_STRING;
				}else if(TypesHelper.isNumberic(type))  //����ֶ�Ϊ�����
				{
					type=HSSFCell.CELL_TYPE_NUMERIC;
				}else                             //��������ݶ�Ϊ�ı���
					type=HSSFCell.CELL_TYPE_STRING;
				String des=metaData.getColumnLabel(i+1);
				CellDefined tmp=new CellDefined(type,des);
			    defined[i]=tmp;
			}
			return defined;
		} catch (SQLException e) {
			throw new ExcelProcessException(e);
		}
		
	}
	/**
	 * @return ���� encoding��
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * @param encoding Ҫ���õ� encoding��
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
