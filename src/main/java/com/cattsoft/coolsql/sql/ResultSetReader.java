package com.cattsoft.coolsql.sql;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.sql.dataview.cellcomponent.BlobDescriptor;
import com.cattsoft.coolsql.sql.util.TypesHelper;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;

public class ResultSetReader
{
	private static final Logger logger=Logger.getLogger(ResultSetReader.class);
	
	private static final StringManager s_stringMgr =
		StringManagerFactory.getStringManager(ResultSetReader.class);

	/** The <TT>ResultSet</TT> being read. */
	private final ResultSet _rs;

	/**
	 * The indices into the <TT>ResultSet that we want to read, starting from
	 * 1 (not 0). If this contains {1, 5, 6} then only columns 1, 5, and 6 will
	 * be read. If <TT>null or empty then all columns are read.
	 */
	private final int[] _columnIndices;


	/**
	 * The number of columns to read. This may or may not be the same as the
	 * number of columns in the <TT>ResultSet</TT>. @see _columnIndices.
	 */
	private int _columnCount;

	/** <TT>true</TT> if an error occured reading a column in th previous row. */
	private boolean _errorOccured = false;

	/** Metadata for the <TT>ResultSet</TT>. */
	private ResultSetMetaData _rsmd;

    /** whether or not the user requested to cancel the query */
   private volatile boolean _stopExecution = false; 

   /** max size of value retrived from resultset*/
   	private int maxColumnWith;
   	
   	/** encoding with which displayed string is builded*/
   	private String encoding;
	public ResultSetReader(ResultSet rs)
		throws SQLException
	{
		this(rs, null,Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH, 2048),"");
	}

	public ResultSetReader(ResultSet rs, int[] columnIndices,int maxColumnWith,String encoding) throws SQLException
	{
		super();
		if (rs == null)
		{
			throw new IllegalArgumentException("ResultSet == null");
		}

		_rs = rs;

		if (columnIndices != null && columnIndices.length == 0)
		{
			columnIndices = null;
		}
		_columnIndices = columnIndices;

		_rsmd = rs.getMetaData();

		_columnCount = columnIndices != null ? columnIndices.length : _rsmd.getColumnCount();
		
	}

	/**
	 * Read the next row from the <TT>ResultSet</TT>. If no more rows then
	 * <TT>null</TT> will be returned, otherwise an <TT>Object[]</TT> will be
	 * returned where each element of the array is an object representing
	 * the contents of the column. These objects could be of type <TT>String</TT>,
	 * <TT>BigDecimal</TT> etc.
	 *
	 * <P>If an error occurs calling <TT>next()</TT> on the <TT>ResultSet</TT>
	 * then an <TT>SQLException will be thrown, however if an error occurs
	 * retrieving the data for a column an error msg will be placed in that
	 * element of the array, but no exception will be thrown. To see if an
	 * error occured retrieving column data you can call
	 * <TT>getColumnErrorInPreviousRow</TT> after the call to <TT>readRow()</TT>.
	 *
	 * @throws	SQLException	Error occured on <TT>ResultSet.next()</TT>.
	 */
	public Object[] readRow() throws SQLException
	{
		_errorOccured = false;
		if (_rs.next())
		{
			return doRead();
		}
		return null;
	}

	/**
	 * Read the next row from the <TT>ResultSet</TT> for use in the ContentTab.
	 * This is different from readRow() in that data is put into the Object array
	 * in a form controlled by the DataType objects, and may be used for editing
	 * the data and updating the DB. If no more rows then
	 * <TT>null</TT> will be returned, otherwise an <TT>Object[]</TT> will be
	 * returned where each element of the array is an object representing
	 * the contents of the column. These objects could be of type <TT>String</TT>,
	 * <TT>BigDecimal</TT> etc.
	 *
	 * <P>If an error occurs calling <TT>next()</TT> on the <TT>ResultSet</TT>
	 * then an <TT>SQLException will be thrown, however if an error occurs
	 * retrieving the data for a column an error msg will be placed in that
	 * element of the array, but no exception will be thrown. To see if an
	 * error occured retrieving column data you can call
	 * <TT>getColumnErrorInPreviousRow</TT> after the call to <TT>readRow()</TT>.
	 *
	 * @throws	SQLException	Error occured on <TT>ResultSet.next()</TT>.
	 */
//	public Object[] readRow(ColumnDisplayDefinition colDefs[]) throws SQLException
//	{
//		_errorOccured = false;
//		if (_rs.next())
//		{
//			return doContentTabRead(colDefs);
//		}
//		return null;
//	}

	/**
	 * Retrieve whether an error occured reading a column in the previous row.
	 *
	 * @return	<TT>true</TT> if error occured.
	 */
	public boolean getColumnErrorInPreviousRow()
	{
		return _errorOccured;
	}

	/**
     * Attempt to get the column type name - for some drivers this results in an
     * SQLException. Log an INFO message if this is unavailable.
     * 
     * @param idx
     *        the column index to get the type name for.
     * 
     * @return the type name of the column, or the Java class, or "Unavailable"
     *         if neither are available.
     */
	@SuppressWarnings("unused")
	private String safelyGetColumnTypeName(int idx) {
	    String columnTypeName = null;
        try
        {
           /*
            * Fails on DB2 8.1 for Linux 
            * However, Windows 8.1 fixpak 14 driver (2.10.52) works without 
            * exception
            * Also, Linux 9.0.1 server with 3.1.57 driver works fine as well
            */ 
           columnTypeName = _rsmd.getColumnTypeName(idx);
        }
        catch (SQLException e)
        {
           if (logger.isInfoEnabled()) {
        	   logger.info("doRead: ResultSetMetaData.getColumnTypeName("+
                   idx+") threw an unexpected exception - "+e.getMessage());
        	   logger.info("Unable to determine column type name so " +
                    "any custom types provided by plugins will be " +
                    "unavailable.  This is a driver bug.");
           }
        }
        if (columnTypeName == null) {
            try {
                columnTypeName = _rsmd.getColumnClassName(idx);
            } catch (SQLException e) {
                if (logger.isInfoEnabled()) {
                	logger.info("doRead: ResultSetMetaData.getColumnClassName("+
                        idx+") threw an unexpected exception - "+e.getMessage());
                    
                }
            }
        }
        if (columnTypeName == null) {
            columnTypeName = "Unavailable";
        }
        return columnTypeName;
	}
	
	/**
	 * Method used to read data for all Tabs except the ContentsTab, where
	 * the data is used only for reading.
	 * The only data read in the non-ContentsTab tabs is Meta-data about the DB,
	 * which means that there should be no BLOBs, CLOBs, or unknown fields.
	 */
	private Object[] doRead()
	{
		Object[] row = new Object[_columnCount];
		for (int i = 0; i < _columnCount && !_stopExecution; ++i)
		{
			int idx = _columnIndices != null ? _columnIndices[i] : i + 1;
			try
			{
				int columnType = _rsmd.getColumnType(idx);
//				String columnTypeName = safelyGetColumnTypeName(idx);
				
				switch (columnType)
				{
					case Types.NULL:
						row[i] = null;
						break;

					case Types.BIT:
					case Types.BOOLEAN:
						row[i] = _rs.getObject(idx);
						if (row[i] != null
							&& !(row[i] instanceof Boolean))
						{
							if (row[i] instanceof Number)
							{
								if (((Number) row[i]).intValue() == 0)
								{
									row[i] = Boolean.FALSE;
								}
								else
								{
									row[i] = Boolean.TRUE;
								}
							}
							else
							{
								row[i] = Boolean.valueOf(row[i].toString());
							}
						}
						break;

					case Types.TIME :
						row[i] = _rs.getTime(idx);
						break;

					case Types.DATE :
//                        if (DataTypeDate.getReadDateAsTimestamp()) {
//                            row[i] = _rs.getTimestamp(idx);
//                        } else {
                            row[i] = _rs.getDate(idx);
//                            	DataTypeDate.staticReadResultSet(_rs, idx, false);
//                        }
						break;

					case Types.TIMESTAMP :
                    case -101 : // Oracle's 'TIMESTAMP WITH TIME ZONE' == -101  
                    case -102 : // Oracle's 'TIMESTAMP WITH LOCAL TIME ZONE' == -102
						row[i] = _rs.getTimestamp(idx);
						break;

					case Types.BIGINT :
						row[i] = _rs.getObject(idx);
						if (row[i] != null
							&& !(row[i] instanceof Long))
						{
							if (row[i] instanceof Number)
							{
								row[i] = Long.valueOf(((Number)row[i]).longValue());
							}
							else
							{
								row[i] = Long.valueOf(row[i].toString());
							}
						}
						break;

					case Types.DOUBLE:
					case Types.FLOAT:
					case Types.REAL:
						row[i] = _rs.getObject(idx);
						if (row[i] != null
							&& !(row[i] instanceof Double))
						{
							if (row[i] instanceof Number)
							{
								Number nbr = (Number)row[i];
								row[i] = new Double(nbr.doubleValue());
							}
							else
							{
								row[i] = new Double(row[i].toString());
							}
						}
						break;

					case Types.DECIMAL:
					case Types.NUMERIC:
						row[i] = _rs.getObject(idx);
						if (row[i] != null
							&& !(row[i] instanceof BigDecimal))
						{
							if (row[i] instanceof Number)
							{
								Number nbr = (Number)row[i];
								row[i] = new BigDecimal(nbr.doubleValue());
							}
							else
							{
								row[i] = new BigDecimal(row[i].toString());
							}
						}
						break;

					case Types.INTEGER:
					case Types.SMALLINT:
					case Types.TINYINT:
						row[i] = _rs.getObject(idx);
						if (_rs.wasNull())
						{
							row[i] = null;
						}
						if (row[i] != null
							&& !(row[i] instanceof Integer))
						{
							if (row[i] instanceof Number)
							{
								row[i] = Integer.valueOf(((Number)row[i]).intValue());
							}
							else
							{
								row[i] = new Integer(row[i].toString());
							}
						}
						break;

						// TODO: Hard coded -. JDBC/ODBC bridge JDK1.4
						// brings back -9 for nvarchar columns in
						// MS SQL Server tables.
						// -8 is ROWID in Oracle.
					case Types.CHAR:
					case Types.VARCHAR:
					case Types.LONGVARCHAR:
					case -9:
					case -8:
						row[i] = _rs.getString(idx);
						if (_rs.wasNull())
						{
							row[i] = null;
						}
						break;

					case Types.BINARY:
					case Types.VARBINARY:
					case Types.LONGVARBINARY:
						row[i] = _rs.getString(idx);
						break;

					case Types.BLOB:
						// Since we are reading Meta-data about the DB, we should
						// never see a BLOB. If we do, the contents are not interpretable
						// , so just tell the user that it is a BLOB and that it
						// has data.

                        row[i] = readBlog(_rs,idx);
//                        	DataTypeBlob.staticReadResultSet(_rs, idx);

						break;

					case Types.CLOB:
						// Since we are reading Meta-data about the DB, we should
						// never see a CLOB. However, if we do we assume that
						// it is printable text and that the user wants to see it, so
						// read in the entire thing.
                        row[i] = _rs.getString(idx);

						break;

						//Add begin
					case Types.JAVA_OBJECT:
					    row[i] = _rs.getObject(idx);
					    if (_rs.wasNull())
					    {
					        row[i] = null;
					    }
					    break;
					    //Add end


					case Types.OTHER:
					    // Since we are reading Meta-data, there really should never be
					    // a field with SQL type Other (1111).
					    // If there is, we REALLY do not know how to handle it,
					    // so do not attempt to read.
//					    ??						if (_largeObjInfo.getReadSQLOther())
//					    ??						{
//					    ??							// Running getObject on a java class attempts
//					    ??							// to load the class in memory which we don't want.
//					    ??							// getString() just gets the value without loading
//					    ??							// the class (at least under PostgreSQL).
//					    ??							//row[i] = _rs.getObject(idx);
//					    ??							row[i] = _rs.getString(idx);
//					    ??						}
//					    ??						else
//					    ??						{
					    row[i] = s_stringMgr.getString("ResultSetReader.other");
//					    ??						}
					    break;

					default:
//					    /* 
//					     * See if there is a plugin-registered DataTypeComponent
//					     * that can handle this column.
//					     */
//					    row[i] = 
//					        CellComponentFactory.readResultWithPluginRegisteredDataType(_rs, 
//					                                                     columnType, 
//					                                                     columnTypeName, 
//					                                                     idx);
//					    if (row[i] == null) {
//                            Integer colTypeInteger = Integer.valueOf(columnType);
//							row[i] = s_stringMgr.getString("ResultSetReader.unknown", 
//                                                            colTypeInteger);
//					    }
						getDefaultValue(_rs,idx,getMaxColumnWith(),getEncoding());
				}
				if(row[i]==null||_rs.wasNull())
				{
					row[i]="<NULL>";
				}
			}catch (Throwable th)
			{
                // Don't bother the user with details about where the result fetch
                // failed if they cancelled the query.
                if (!_stopExecution) {
                    _errorOccured = true;
                    row[i] = s_stringMgr.getString("ResultSetReader.error");
                    StringBuffer msg = new StringBuffer("Error reading column data");
                    msg.append(", column index = ").append(idx);
                    logger.error(msg.toString(), th);
                }
			}
		}

		return row;
	}
	private Object getDefaultValue(ResultSet set,int index, int maxColumnWidth, String encoding) throws SQLException
	{
		Object value=null;
        int columnType=set.getMetaData().getColumnType(index);
        ResultSetMetaData rsmd=set.getMetaData();
        try {
            if (columnType == TypesHelper.LONGVARBINARY) //�����͵��ֶ���ݣ�ʹ�ö�����������ȡ���
                value = getEncodedStringFromBinaryStream(set,
                		encoding, index,maxColumnWidth);
            else if (rsmd.getColumnDisplaySize(index)< maxColumnWidth)  //ʹ����ͨ�ֽ�(byte)�ֶ�����ȡ���
                value = getEncodedString(set, encoding, index);
            else if ("".equals(encoding))           //����ֶγ��ȳ�������ʾ���ȣ����ұ����ʽû��ָ���������ַ�������ȡ���       
                value = getStringFromCharacterSteam(set, index,maxColumnWidth);
            else                                        //������������ö�����������ȡ���
                value = getEncodedStringFromBinaryStream(set,
                		encoding, index,maxColumnWidth);
        } catch (IOException e) {
            value = set.getString(index);
        } catch (RuntimeException e) {
            value = set.getString(index);
        }
        if (value == null && !set.wasNull())
            value = set.getString(index);
        
        return value;
	}
	/**
	 * Method used to read data for the ContentsTab, where
	 * the data is used for both reading and editing.
	 */
//	private Object[] doContentTabRead(ColumnDisplayDefinition colDefs[])
//	{
//		Object[] row = new Object[_columnCount];
//		for (int i = 0; i < _columnCount && !_stopExecution; ++i)
//		{
//			int idx = _columnIndices != null ? _columnIndices[i] : i + 1;
//			try
//			{
//				final int columnType = _rsmd.getColumnType(idx);
//				//final String columnClassName = _rsmd.getColumnClassName(idx);
//				switch (columnType)
//				{
//					case Types.NULL:
//						row[i] = null;
//						break;
//
//					// all of the following have been converted to use the DataType objects
//                    // So, why not just have case Types.NULL and default??? (this seems pointless)
//                    // RMM 20070726
//					case Types.BIT:
//					case Types.BOOLEAN:
//
//					case Types.DECIMAL:
//					case Types.NUMERIC:
//
//					case Types.INTEGER:
//					case Types.SMALLINT:
//					case Types.TINYINT:
//					case Types.BIGINT :
//
//					case Types.DOUBLE:
//					case Types.FLOAT:
//					case Types.REAL:
//
//					case Types.DATE :
//					case Types.TIME :
//					case Types.TIMESTAMP :
//
//					// TODO: Hard coded -. JDBC/ODBC bridge JDK1.4
//					// brings back -9 for nvarchar columns in
//					// MS SQL Server tables.
//					// -8 is ROWID in Oracle.
//					case Types.CHAR:
//					case Types.VARCHAR:
//					case Types.LONGVARCHAR:
//					case -9:
//					case -8:
//
//					// binary types
//					case Types.BINARY:
//					case Types.VARBINARY:
//					case Types.LONGVARBINARY:
//
//					case Types.CLOB:
//					case Types.BLOB:
//
//					case Types.OTHER:
//
//					default:
//						row[i] = CellComponentFactory.readResultSet(
//								colDefs[i], _rs, idx, true);
//
//						break;
//
//				}
//			}
//			catch (Throwable th)
//			{
//				_errorOccured = true;
//				row[i] = s_stringMgr.getString("ResultSetReader.error");
//                if (!_stopExecution) {
//                    StringBuffer msg = new StringBuffer("Error reading column data");
//                    msg.append(", column index = ").append(idx);
//                    logger.error(msg.toString(), th);
//                }
//			}
//		}
//
//		return row;
//	}

    /**
     * @param _stopExecution The _stopExecution to set.
     */
    public void setStopExecution(boolean _stopExecution) {
        this._stopExecution = _stopExecution;
    }

    /**
     * @return Returns the _stopExecution.
     */
    public boolean isStopExecution() {
        return _stopExecution;
    }

	/**
	 * @return the maxColumnWith
	 */
	public int getMaxColumnWith() {
		return this.maxColumnWith;
	}

	/**
	 * @param maxColumnWith the maxColumnWith to set
	 */
	public void setMaxColumnWith(int maxColumnWith) {
		this.maxColumnWith = maxColumnWith;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return this.encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
    /**
     * Process column according to LONGVARBINARY , building result with encoding.
     */
    private String getEncodedStringFromBinaryStream(ResultSet set,
            String encoding, int columnNumber,int maxColumnWidth) throws SQLException,
            IOException, UnsupportedEncodingException {
        InputStream binaryStream = set.getBinaryStream(columnNumber);
        if (binaryStream != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                int c = binaryStream.read();
                for (int count = 0; c >= 0 && count <= maxColumnWidth; count++) {
                    baos.write(c);
                    c = binaryStream.read();
                }

            } finally {
                binaryStream.close();
            }
            if ("".equals(encoding))
                return new String(baos.toByteArray());
            else
                return new String(baos.toByteArray(), encoding);
        } else {
            return null;
        }
    }

    /**
     */
    private String getStringFromCharacterSteam(ResultSet set, int columnNumber,int maxColumnWidth)
            throws SQLException, IOException {
        Reader reader = set.getCharacterStream(columnNumber);
        if (reader != null) {
            StringBuffer buffer = new StringBuffer();
            int retVal = reader.read();
            int count = 0;
            while (retVal >= 0) {
                buffer.append((char) retVal);
                retVal = reader.read();
                if (++count > maxColumnWidth) {
                    buffer.append("...>>>");
                    break;
                }
            }
            reader.close();
            return buffer.toString();
        } else {
            return null;
        }
    }

    private String getEncodedString(ResultSet set, String encoding, int index)
            throws SQLException {
        try {
            if (encoding == null || encoding.trim().length() == 0)
                return set.getString(index);
            byte colBytes[] = set.getBytes(index);
            if (colBytes == null)
                return null;
            else
                return new String(colBytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return set.getString(index);
        }
    }
	public static Object readBlog(ResultSet rs,int index) throws SQLException
	{
		// We always get the BLOB, even when we are not reading the contents.
		// Since the BLOB is just a pointer to the BLOB data rather than the
		// data itself, this operation should not take much time (as opposed
		// to getting all of the data in the blob).
		Blob blob = rs.getBlob(index);

		if (rs.wasNull())
			return null;

		// BLOB exists, so try to read the data from it
		// based on the user's directions
		boolean _readBlobs=true;//TODO: depend on user direction
		boolean _readCompleteBlobs=false;//TODO: depend on user direction
		int _readBlobsSize=50000;//TODO: depend on user direction
		if (_readBlobs)
		{
			// User said to read at least some of the data from the blob
			byte[] blobData = null;
			if (blob != null)
			{
				int len = (int)blob.length();
				if (len > 0)
				{
					int charsToRead = len;
					if (! _readCompleteBlobs)
					{
						charsToRead = _readBlobsSize;
					}
					if (charsToRead > len)
					{
						charsToRead = len;
					}
					blobData = blob.getBytes(1, charsToRead);
				}
			}

			// determine whether we read all there was in the blob or not
			boolean wholeBlobRead = false;
			if (_readCompleteBlobs ||
				blobData.length < _readBlobsSize)
				wholeBlobRead = true;

			return new BlobDescriptor(blob, blobData, true, wholeBlobRead,
				_readBlobsSize);
		}
		else
		{
			// user said not to read any of the data from the blob
			return new BlobDescriptor(blob, null, false, false, 0);
		}
	}

}
