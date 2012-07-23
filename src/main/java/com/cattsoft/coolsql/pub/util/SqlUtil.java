package com.cattsoft.coolsql.pub.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.cattsoft.coolsql.adapters.AdapterFactory;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.DriverInfo;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.sql.ISQLDatabaseMetaData;
import com.cattsoft.coolsql.sql.formater.SQLToken;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.model.EntityFactory;
import com.cattsoft.coolsql.view.sqleditor.SqlFormatter;

/**
 * Methods for manipulating and analyzing SQL statements.
 */
public class SqlUtil
{
	private static final Logger logger=Logger.getLogger(SqlUtil.class);
	 /** Internationalized strings for this class. */
    private static final StringManager s_stringMgr =
        StringManagerFactory.getStringManager(SqlUtil.class);
	//private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[$:\\\\/ \\.;,]");
	private static final Pattern SQL_IDENTIFIER = Pattern.compile("[a-zA-Z_][\\w\\$#@]*");
	
    /**
     * sql��׼�ӿڷ��صĽ����ֶ����ݵķֲ�
     */
    public static final int TABLE_METADATA_TABLE_CATALOG=1;
    public static final int TABLE_METADATA_TABLE_SCHEM = 2;

    public static final int TABLE_METADATA_TABLE_NAME = 3;

    public static final int TABLE_METADATA_TYPE_NAME = 4;
    
    public static final int TABLE_METADATA_REMARK = 5;

    public static final int TABLE_METADATA_TABLE_TYPE = 1;

    public static final int FOREIGN_KEY_METADATA_PKTABLE_SCHEM = 2;

    public static final int FOREIGN_KEY_METADATA_PKTABLE_NAME = 3;

    public static final int FOREIGN_KEY_METADATA_PKCOLUMN_NAME = 4;

    public static final int FOREIGN_KEY_METADATA_FKTABLE_SCHEM = 6;

    public static final int FOREIGN_KEY_METADATA_FKTABLE_NAME = 7;

    public static final int FOREIGN_KEY_METADATA_FKCOLUMN_NAME = 8;

    public static final int FOREIGN_KEY_METADATA_KEY_SEQ = 9;

    public static final int FOREIGN_KEY_METADATA_DELETE_RULE = 11;

    public static final int FOREIGN_KEY_METADATA_FK_NAME = 12;

    public static final int TYPE_INFO_METADATA_TYPE_NAME = 1;

    public static final int TYPE_INFO_METADATA_DATA_TYPE = 2;

    public static final int TYPE_INFO_METADATA_PRECISION = 3;

    public static final int TYPE_INFO_METADATA_LITERAL_PREFIX = 4;

    public static final int TYPE_INFO_METADATA_LITERAL_SUFFIX = 5;

    public static final int TYPE_INFO_METADATA_CREATE_PARMS = 6;
    
    
    /**
     * jdbc constant 
     */
    public static final String SEQUENCE="SEQUENCE";
    public static final String TABLE="TABLE";
    public static final String VIEW="VIEW";
    public static final String SYNONYM="SYNONYM";
    
	/**
	 * Removes the SQL verb of this command. The verb is defined
	 * as the first "word" in the SQL string that is not a comment.
	 * 
	 * @see #getSqlVerb(CharSequence)
	 */
	public static String stripVerb(String sql)
	{
		String result = "";
		try
		{
			SQLLexer l = new SQLLexer(sql);
			SQLToken t = l.getNextToken(false, false);
			int pos = -1;
			if (t != null) pos = t.getCharEnd();
			if (pos > -1) result = sql.substring(pos).trim();
		}
		catch (Exception e)
		{
			logger.error("Error cleaning up SQL", e);
		}
		return result;
	}

	
	public static String quoteObjectname(String object)
	{
		return quoteObjectname(object, false);
	}
	
	public static String quoteObjectname(String aColname, boolean quoteAlways)
	{
		if (aColname == null) return null;
		if (aColname.length() == 0) return "";
		aColname = aColname.trim();
		
		boolean doQuote = quoteAlways;
		
		if (!quoteAlways)
		{
			Matcher m = SQL_IDENTIFIER.matcher(aColname);
			//doQuote = m.find() || Character.isDigit(aColname.charAt(0));;
			doQuote = !m.matches();
		}
		if (!doQuote) return aColname;
		StringBuilder col = new StringBuilder(aColname.length() + 2);
		col.append('"');
		col.append(aColname);
		col.append('"');
		return col.toString();
	}

	/**
	 * Returns the type that is beeing created e.g. TABLE, VIEW, PROCEDURE
	 */
	public static String getCreateType(CharSequence sql)
	{
		try
		{
			SQLLexer lexer = new SQLLexer(sql);
			SQLToken t = lexer.getNextToken(false, false);
			String v = t.getContents();
			if (!v.equals("CREATE") && !v.equals("RECREATE") && !v.equals("CREATE OR REPLACE")) return null;
			SQLToken type = lexer.getNextToken(false, false);
			if (type == null) return null;
			
			// check for CREATE FORCE VIEW 
			if (type.getContents().equals("FORCE"))
			{
				SQLToken t2 = lexer.getNextToken(false, false);
				if (t2 == null) return null;
				return t2.getContents();
			}
			return type.getContents();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * If the given SQL is a DELETE [FROM] returns 
	 * the table from which rows will be deleted
	 */
	public static String getDeleteTable(CharSequence sql)
	{
		try
		{
			SQLLexer lexer = new SQLLexer(sql);
			SQLToken t = lexer.getNextToken(false, false);
			if (!t.getContents().equals("DELETE")) return null;
			t = lexer.getNextToken(false, false);
			// If the next token is not the FROM keyword (which is optional) 
			// then it must be the table name.
			if (t == null) return null;
			if (!t.getContents().equals("FROM")) return t.getContents(); 
			t = lexer.getNextToken(false, false);
			if (t == null) return null;
			return t.getContents();
		}
		catch (Exception e)
		{
			return null;
		}
	}	

	/**
	 * If the given SQL is an INSERT INTO... 
	 * returns the target table, otherwise null
	 */
	public static String getInsertTable(CharSequence sql)
	{
		try
		{
			SQLLexer lexer = new SQLLexer(sql);
			SQLToken t = lexer.getNextToken(false, false);
			if (t == null || !t.getContents().equals("INSERT")) return null;
			t = lexer.getNextToken(false, false);
			if (t == null || !t.getContents().equals("INTO")) return null;
			t = lexer.getNextToken(false, false);
			if (t == null) return null;
			return t.getContents();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * If the given SQL command is an UPDATE command, return 
	 * the table that is updated, otherwise return null;
	 */
	public static String getUpdateTable(CharSequence sql)
	{
		try
		{
			SQLLexer lexer = new SQLLexer(sql);
			SQLToken t = lexer.getNextToken(false, false);
			if (t == null || !t.getContents().equals("UPDATE")) return null;
			t = lexer.getNextToken(false, false);
			if (t == null) return null;
			return t.getContents();
		}
		catch (Exception e)
		{
			return null;
		}
	}	
	
	/**
	 *  Returns the SQL Verb for the given SQL string.
	 */
	public static String getSqlVerb(CharSequence sql)
	{
		if (StringUtil.isEmptyString(sql)) return "";
		
		SQLLexer l = new SQLLexer(sql);
		try
		{
			SQLToken t = l.getNextToken(false, false);
			if (t == null) return "";
			
			// The SQLLexer does not recognize @ as a keyword (which is basically
			// correct, but to support the Oracle style includes we'll treat it
			// as a keyword here.
			String v = t.getContents();
			if (v.charAt(0) == '@') return "@";
			
			return t.getContents().toUpperCase();
		}
		catch (Exception e)
		{
			return "";
		}
	}
//	
//	/**
//	 * Returns the columns for the result set defined by the passed
//	 * query.
//	 * This method will actually execute the given SQL query, but will 
//	 * not retrieve any rows (using setMaxRows(1).
//	 */
//	public static List<ColumnIdentifier> getResultSetColumns(String sql, WbConnection conn)
//		throws SQLException
//	{
//		if (conn == null) return null;
//
//		ResultInfo info = getResultInfoFromQuery(sql, conn);
//		if (info == null) return null;
//
//		int count = info.getColumnCount();
//		ArrayList<ColumnIdentifier> result = new ArrayList<ColumnIdentifier>(count);
//		for (int i = 0; i < count; i++)
//		{
//			result.add(info.getColumn(i));
//		}
//		return result;
//	}

//	public static ResultInfo getResultInfoFromQuery(String sql, WbConnection conn)
//		throws SQLException
//	{
//		if (conn == null) return null;
//
//		ResultSet rs = null;
//		Statement stmt = null;
//		ResultInfo result = null;
//
//		try
//		{
//			stmt = conn.createStatementForQuery();
//			stmt.setMaxRows(1);
//			rs = stmt.executeQuery(sql);
//			ResultSetMetaData meta = rs.getMetaData();
//			result = new ResultInfo(meta, conn);
//			List tables = getTables(sql, false);
//			if (tables.size() == 1)
//			{
//				String table = (String)tables.get(0);
//				TableIdentifier tbl = new TableIdentifier(table);
//				result.setUpdateTable(tbl);
//			}
//		}
//		finally
//		{
//			closeAll(rs, stmt);
//		}
//		return result;
//	}
	
	private static String getTableDefinition(String table, boolean keepAlias)
	{
		if (keepAlias) return table;
		int pos = StringUtil.findFirstWhiteSpace(table);
		if (pos > -1) return table.substring(0, pos);
		return table;
	}
	
	/**
	 * Parse the given SQL SELECT query and return the columns defined
	 * in the column list. If the SQL string does not start with SELECT
	 * returns an empty List
	 * @param select the SQL String to parse
	 * @param includeAlias if false, the "raw" column names will be returned, otherwise
	 *       the column name including the alias (e.g. "p.name AS person_name"
	 * @return a List of String objecs. 
	 */
	public static List<String> getSelectColumns(String select, boolean includeAlias)
	{
		List<String> result = new LinkedList<String>();
		try
		{
			SQLLexer lex = new SQLLexer(select);
			SQLToken t = lex.getNextToken(false, false);
			if (!"SELECT".equalsIgnoreCase(t.getContents())) return Collections.emptyList();
			t = lex.getNextToken(false, false);
			int lastColStart = t.getCharBegin();
			int bracketCount = 0;
			boolean nextIsCol = true;
			while (t != null)
			{
				String v = t.getContents();
				if ("(".equals(v))
				{
					bracketCount ++;
				}
				else if (")".equals(v))
				{
					bracketCount --;
				}
				else if (bracketCount == 0 && (",".equals(v) || SqlFormatter.SELECT_TERMINAL.contains(v)))
				{
					String col = select.substring(lastColStart, t.getCharBegin());
					if (includeAlias)
					{
						result.add(col.trim());
					}
					else
					{
						result.add(striptColumnAlias(col));
					}
					if (SqlFormatter.SELECT_TERMINAL.contains(v))
					{
						nextIsCol = false;
						lastColStart = -1;
						break;
					}
					nextIsCol = true;
				}
				else if (nextIsCol)
				{
					lastColStart = t.getCharBegin();
					nextIsCol = false;
				}
				t = lex.getNextToken(false, false);
			}
			if (lastColStart > -1)
			{
				// no FROM was found, so assume it's a partial SELECT x,y,z
				String col = select.substring(lastColStart);
				if (includeAlias)
				{
					result.add(col.trim());
				}
				else
				{
					result.add(striptColumnAlias(col));
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Error parsing SELECT statement", e);
			return Collections.emptyList();
		}
		
		return result;
	}
	
	public static String striptColumnAlias(String expression)
	{
		if (expression == null) return null;
		
		List elements = StringUtil.stringToList(expression, " ", true, true, true);
		
		return (String)elements.get(0);
	}
	
	public static List<String> getTables(String aSql)
	{
		return getTables(aSql, false);
	}

	public static final Set<String> JOIN_KEYWORDS = new HashSet<String>(6);
	static
	{
			JOIN_KEYWORDS.add("INNER JOIN");
			JOIN_KEYWORDS.add("LEFT JOIN");
			JOIN_KEYWORDS.add("RIGHT JOIN");
			JOIN_KEYWORDS.add("LEFT OUTER JOIN");
			JOIN_KEYWORDS.add("RIGHT OUTER JOIN");
			JOIN_KEYWORDS.add("CROSS JOIN");
			JOIN_KEYWORDS.add("FULL JOIN");
			JOIN_KEYWORDS.add("FULL OUTER JOIN");
	}
	
	/**
	 * Returns a List of tables defined in the SQL query. If the 
	 * query is not a SELECT query the result is undefined
	 */
	public static List<String> getTables(String sql, boolean includeAlias)
	{
		String from = SqlUtil.getFromPart(sql);
		if (from == null || from.trim().length() == 0) return Collections.emptyList();
		List<String> result = new LinkedList<String>();
		try
		{
				SQLLexer lex = new SQLLexer(from);
				SQLToken t = lex.getNextToken(false, false);

				boolean collectTable = true;
				StringBuilder currentTable = new StringBuilder();
				int bracketCount = 0;
				boolean subSelect = false;
				int subSelectBracketCount = -1;
				
				while (t != null)
				{
					String s = t.getContents();
					
					if (s.equals("SELECT") && bracketCount > 0)
					{
						subSelect = true;
						subSelectBracketCount = bracketCount;
					}
					
					if ("(".equals(s))
					{
						bracketCount ++;
					}
					else if (")".equals(s))
					{
						if (subSelect && bracketCount == subSelectBracketCount)
						{
							subSelect = false;
						}
						bracketCount --;
						t = lex.getNextToken(false, false);
						continue;
					}
					
					if (!subSelect)
					{
						if (JOIN_KEYWORDS.contains(s))
						{
							collectTable = true;
							if (currentTable.length() > 0)
							{
								result.add(getTableDefinition(currentTable.toString(), includeAlias));
								currentTable = new StringBuilder();
							}
						}
						else if (",".equals(s))
						{
								collectTable = true;
								result.add(getTableDefinition(currentTable.toString(), includeAlias));
								currentTable = new StringBuilder();
						}
						else if ("ON".equals(s))
						{
							collectTable = false;
							result.add(getTableDefinition(currentTable.toString(), includeAlias));
							currentTable = new StringBuilder();
						}
						else if (collectTable && !s.equals("("))
						{
							int size = currentTable.length();
							if (size > 0 && !s.equals(".") && currentTable.charAt(size-1) != '.') currentTable.append(' ');
							currentTable.append(s);
						}
					}					
					t = lex.getNextToken(false, false);
				}
				
				if (currentTable.length() > 0)
				{
					result.add(getTableDefinition(currentTable.toString(), includeAlias));
				}
		}
		catch (Exception e)
		{
			logger.error("Error parsing sql", e);
		}
		return result;
	}

	/**	
	 * Extract the FROM part of a SQL statement. That is anything after the FROM
	 * up to (but not including) the WHERE, GROUP BY, ORDER BY, whichever comes first
	 */
	public static String getFromPart(String sql)
	{
		int fromPos = getFromPosition(sql);
		if (fromPos == -1) return null;
		fromPos += "FROM".length();
		if (fromPos >= sql.length()) return null;
		int fromEnd = getKeywordPosition(SqlFormatter.FROM_TERMINAL, sql);
		if (fromEnd == -1)
		{
			return sql.substring(fromPos);
		}
		return sql.substring(fromPos, fromEnd);
	}
	
	/**
	 * Return the position of the FROM keyword in the given SQL
	 */
	public static int getFromPosition(String sql)
	{
		Set<String> s = new HashSet<String>();
		s.add("FROM");
		return getKeywordPosition(s, sql);
	}
	
	public static int getWherePosition(String sql)
	{
		Set<String> s = new HashSet<String>();
		s.add("WHERE");
		return getKeywordPosition(s, sql);
	}
	
	public static int getKeywordPosition(String keyword, CharSequence sql)
	{
		if (keyword == null) return -1;
		Set<String> s = new HashSet<String>();
		s.add(keyword.toUpperCase());
		return getKeywordPosition(s, sql);
	}
	
	public static int getKeywordPosition(Set<String> keywords, CharSequence sql)
	{
		int pos = -1;
		try
		{
			SQLLexer lexer = new SQLLexer(sql);

			SQLToken t = lexer.getNextToken(false, false);
			int bracketCount = 0;
			while (t != null)
			{
				String value = t.getContents();
				if ("(".equals(value)) 
				{
					bracketCount ++;
				}
				else if (")".equals(value))
				{
					bracketCount --;
				}
				else if (bracketCount == 0)
				{
					if (keywords.contains(value))
					{
						pos = t.getCharBegin();
						break;
					}
				}

				t = lexer.getNextToken(false, false);
			}		
		}
		catch (Exception e)
		{
			pos = -1;
		}
		return pos;
	}
	
	public static String makeCleanSql(String aSql, boolean keepNewlines)
	{
		return makeCleanSql(aSql, keepNewlines, '\'');
	}


	public static String makeCleanSql(String aSql, boolean keepNewlines, char quote)
	{
		return makeCleanSql(aSql, keepNewlines, false, quote);
	}

	/**
	 *	Replaces all white space characters with ' ' (But not inside
	 *	string literals) and removes -- style and Java style comments
	 *	@param aSql The sql script to "clean out"
	 *  @param keepNewlines if true, newline characters (\n) are kept
	 *  @param keepComments if true, comments (single line, block comments) are kept
	 *  @param quote The quote character
	 *	@return String
	 */
	public static String makeCleanSql(String aSql, boolean keepNewlines, boolean keepComments, char quote)
	{
		if (aSql == null) return null;
		aSql = aSql.trim();
		int count = aSql.length();
		if (count == 0) return aSql;
		boolean inComment = false;
		boolean inQuotes = false;
		boolean lineComment = false;

		StringBuilder newSql = new StringBuilder(count);

		// remove trailing semicolon
		if (aSql.charAt(count - 1) == ';') count --;
		char last = ' ';

		for (int i=0; i < count; i++)
		{
			char c = aSql.charAt(i);

			if (c == quote)
			{
				inQuotes = !inQuotes;
			}
			
			if (inQuotes)
			{
				newSql.append(c);
				last = c;
				continue;
			}

			if ((last == '\n' || last == '\r' || i == 0 ) && (c == '#'))
			{
				lineComment = true;
			}

			if (!(inComment || lineComment) || keepComments)
			{
				if ( c == '/' && i < count - 1 && aSql.charAt(i+1) == '*')
				{
					inComment = true;
					i++;
				}
				else if (c == '-' && i < count - 1 && aSql.charAt(i+1) == '-')
				{
					// ignore rest of line for -- style comments
					while (c != '\n' && i < count - 1)
					{
						i++;
						c = aSql.charAt(i);
					}
				}
				else
				{
					if ((c == '\n' || c == '\r') && !keepNewlines)
					{
						// only replace the \n, \r are simply removed
						// thus replacing \r\n with only one space
						if (c == '\n') newSql.append(' ');
					}
					else if (c != '\n' && (c < 32 || (c > 126 && c < 145) || c == 255))
					{
						newSql.append(' ');
					}
					else
					{
						newSql.append(c);
					}
				}
			}
			else
			{
				if ( c == '*' && i < count - 1 && aSql.charAt(i+1) == '/')
				{
					inComment = false;
					i++;
				}
				else if (c == '\n' || c == '\r' && lineComment)
				{
					lineComment = false;
				}
			}
			last = c;
		}
		String s = newSql.toString().trim();
		if (s.endsWith(";")) s = s.substring(0, s.length() - 1);
		return s;
	}

	/**
	 * returns true if the passed data type (from java.sql.Types)
	 * indicates a data type which can hold numeric values with
	 * decimals
	 */
	public static final boolean isDecimalType(int aSqlType, int aScale, int aPrecision)
	{
		if (aSqlType == Types.DECIMAL ||
			aSqlType == Types.DOUBLE ||
			aSqlType == Types.FLOAT ||
			aSqlType == Types.NUMERIC ||
			aSqlType == Types.REAL)
		{
			return (aScale > 0);
		}
		else
		{
			return false;
		}
	}

	/**
	 * returns true if the passed JDBC data type (from java.sql.Types)
	 * indicates a data type which maps to a integer type
	 */
	public static final boolean isIntegerType(int aSqlType)
	{
		return (aSqlType == Types.BIGINT ||
		        aSqlType == Types.INTEGER ||
		        aSqlType == Types.SMALLINT ||
		        aSqlType == Types.TINYINT);
	}

	/**
	 * Returns true if the given JDBC type maps to the String class. This
	 * returns fals for CLOB data.
	 */
	public static final boolean isStringType(int aSqlType)
	{
		return (aSqlType == Types.VARCHAR || 
		        aSqlType == Types.CHAR ||
		        aSqlType == Types.LONGVARCHAR);
	}
	
	/**
	 * Returns true if the given JDBC type indicates some kind of 
	 * character data (including CLOBs)
	 */
	public static final boolean isCharacterType(int aSqlType)
	{
		return (aSqlType == Types.VARCHAR || 
		        aSqlType == Types.CHAR ||
		        aSqlType == Types.CLOB ||
		        aSqlType == Types.LONGVARCHAR);
	}
	
	/**
	 * 	Returns true if the passed datatype (from java.sql.Types)
	 *  can hold a numeric value (either with or without decimals)
	 */
	public static final boolean isNumberType(int aSqlType)
	{
		return (aSqlType == Types.BIGINT ||
		        aSqlType == Types.INTEGER ||
		        aSqlType == Types.DECIMAL ||
		        aSqlType == Types.DOUBLE ||
		        aSqlType == Types.FLOAT ||
		        aSqlType == Types.NUMERIC ||
		        aSqlType == Types.REAL ||
		        aSqlType == Types.SMALLINT ||
		        aSqlType == Types.TINYINT);
	}
	
	public static final boolean isDateType(int aSqlType)
	{
		return (aSqlType == Types.DATE || aSqlType == Types.TIMESTAMP);
	}

	public static final boolean isClobType(int aSqlType)
	{
		return (aSqlType == Types.CLOB);
	}
	
//	public static final boolean isClobType(int aSqlType, DbSettings dbInfo)
//	{
//		if (dbInfo == null || !dbInfo.longVarcharIsClob()) return isClobType(aSqlType);
//		return (aSqlType == Types.CLOB || aSqlType == Types.LONGVARCHAR);
//	}
	
	public static final boolean isBlobType(int aSqlType)
	{
		return (aSqlType == Types.BLOB || 
		        aSqlType == Types.BINARY ||
		        aSqlType == Types.LONGVARBINARY ||
		        aSqlType == Types.VARBINARY);
	}
	public static CharSequence getWarnings(Connection con, Statement stmt)
	{
		try
		{
			// some DBMS return warnings on the connection rather then on the
			// statement. We need to check them here as well. Then some of
			// the DBMS return the same warnings on the Statement AND the
			// Connection object (and MySQL returns an error as the Exception itself
			// and additionally as a warning on the Statement...)
			// For this we keep a list of warnings which have been added
			// from the statement. They will not be added when the Warnings from
			// the connection are retrieved
			Set<String> added = new HashSet<String>();
			StringBuilder msg = null; 
			String s = null;
			SQLWarning warn = (stmt == null ? null : stmt.getWarnings());
			boolean hasWarnings = warn != null;
			int count = 0;
			
			while (warn != null)
			{
				count ++;
				s = warn.getMessage();
				if (s != null && s.length() > 0)
				{
					msg = append(msg, s);
					if (!s.endsWith("\n")) msg.append('\n');
					added.add(s);
				}
				if (count > 15) break; // prevent endless loop
				warn = warn.getNextWarning();
			}
			
			warn = (con == null ? null : con.getWarnings());
			hasWarnings = hasWarnings || (warn != null);
			count = 0;
			while (warn != null)
			{
				s = warn.getMessage();
				// Some JDBC drivers duplicate the warnings between 
				// the statement and the connection.
				// This is to prevent adding them twice
				if (!added.contains(s))
				{
					msg = append(msg, s);
					if (!s.endsWith("\n")) msg.append('\n');
				}
				if (count > 25) break; // prevent endless loop
				warn = warn.getNextWarning();
			}

			// make sure the warnings are cleared from both objects!
			con.clearWarnings();
			stmt.clearWarnings();
			StringUtil.trimTrailingWhitespace(msg);
			return msg;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 *	Convenience method to close a ResultSet without a possible
	 *  SQLException
	 */
	public static void closeResult(ResultSet rs)
	{
		if (rs == null) return;
		try { rs.close(); } catch (Throwable th) {}
	}

	/**
	 *	Convenience method to close a Statement without a possible
	 *  SQLException
	 */
	public static void closeStatement(Statement stmt)
	{
		if (stmt == null) return;
		try { stmt.close(); } catch (Throwable th) {}
	}

	/**
	 *	Convenience method to close a ResultSet and a Statement without
	 *  a possible SQLException
	 */
	public static void closeAll(ResultSet rs, Statement stmt)
	{
		closeResult(rs);
		closeStatement(stmt);
	}

	public static final String getTypeName(int aSqlType)
	{
		if (aSqlType == Types.ARRAY)
			return "ARRAY";
		else if (aSqlType == Types.BIGINT)
			return "BIGINT";
		else if (aSqlType == Types.BINARY)
			return "BINARY";
		else if (aSqlType == Types.BIT)
			return "BIT";
		else if (aSqlType == Types.BLOB)
			return "BLOB";
		else if (aSqlType == Types.BOOLEAN)
			return "BOOLEAN";
		else if (aSqlType == Types.CHAR)
			return "CHAR";
		else if (aSqlType == Types.CLOB)
			return "CLOB";
		else if (aSqlType == Types.DATALINK)
			return "DATALINK";
		else if (aSqlType == Types.DATE)
			return "DATE";
		else if (aSqlType == Types.DECIMAL)
			return "DECIMAL";
		else if (aSqlType == Types.DISTINCT)
			return "DISTINCT";
		else if (aSqlType == Types.DOUBLE)
			return "DOUBLE";
		else if (aSqlType == Types.FLOAT)
			return "FLOAT";
		else if (aSqlType == Types.INTEGER)
			return "INTEGER";
		else if (aSqlType == Types.JAVA_OBJECT)
			return "JAVA_OBJECT";
		else if (aSqlType == Types.LONGVARBINARY)
			return "LONGVARBINARY";
		else if (aSqlType == Types.LONGVARCHAR)
			return "LONGVARCHAR";
		else if (aSqlType == Types.NULL)
			return "NULL";
		else if (aSqlType == Types.NUMERIC)
			return "NUMERIC";
		else if (aSqlType == Types.OTHER)
			return "OTHER";
		else if (aSqlType == Types.REAL)
			return "REAL";
		else if (aSqlType == Types.REF)
			return "REF";
		else if (aSqlType == Types.SMALLINT)
			return "SMALLINT";
		else if (aSqlType == Types.STRUCT)
			return "STRUCT";
		else if (aSqlType == Types.TIME)
			return "TIME";
		else if (aSqlType == Types.TIMESTAMP)
			return "TIMESTAMP";
		else if (aSqlType == Types.TINYINT)
			return "TINYINT";
		else if (aSqlType == Types.VARBINARY)
			return "VARBINARY";
		else if (aSqlType == Types.VARCHAR)
			return "VARCHAR";
		else
			return "UNKNOWN";
	}
	
	/**
	 * Construct the SQL display name for the given SQL datatype.
	 * This is used when re-recreating the source for a table
	 */
	public static String getSqlTypeDisplay(String aTypeName, int sqlType, int size, int digits)
	{
		String display = aTypeName;

		switch (sqlType)
		{
			case Types.VARCHAR:
			case Types.CHAR:
				if ("text".equals(aTypeName) && size == Integer.MAX_VALUE) return aTypeName;
				if (size > 0) 
				{
					display = aTypeName + "(" + size + ")";
				}
				else
				{
					display = aTypeName;
				}
				break;
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
			case Types.FLOAT:
				if (aTypeName.equalsIgnoreCase("money")) // SQL Server
				{
					display = aTypeName;
				}
				else if ((aTypeName.indexOf('(') == -1))
				{
					if (digits > 0 && size > 0)
					{
						display = aTypeName + "(" + size + "," + digits + ")";
					}
					else if (size <= 0 && digits > 0)
					{
						display = aTypeName + "(" + digits + ")";
					}
					else if (size > 0 && digits <= 0)
					{
						display = aTypeName + "(" + size + ")";
					}
				}
				break;

			case Types.OTHER:
				// Oracle specific datatypes
				if ("NVARCHAR2".equalsIgnoreCase(aTypeName))
				{
					display = aTypeName + "(" + size + ")";
				}
				else if ("NCHAR".equalsIgnoreCase(aTypeName))
				{
					display = aTypeName + "(" + size + ")";
				}
				else if ("UROWID".equalsIgnoreCase(aTypeName))
				{
					display = aTypeName + "(" + size + ")";
				}
				else if ("RAW".equalsIgnoreCase(aTypeName))
				{
					display = aTypeName + "(" + size + ")";
				}
				break;
			default:
				display = aTypeName;
				break;
		}
		return display;
	}	
	@SuppressWarnings("unused")
	private static StringBuilder append(StringBuilder msg, CharSequence s)
	{
		if (msg == null) msg = new StringBuilder(100);
		msg.append(s);
		return msg;
	}
	public static String validateSqlParam(String value)
	{
		if(value!=null&&value.trim().equals(""))
			return null;
		else
			return value;
	}
	public static String generateQuelifiedNameWithoutQuote(Entity entity) throws UnifyException
	{
		if(entity==null)
			return "";
		return generateQuelifiedNameWithoutQuote(entity.getCatalog(),entity.getSchema(),entity.getName(),entity.getBookmark());
	}
	public static String generateQuelifiedNameWithoutQuote(String catalog,String schema,String simpleName,Bookmark bookmark) throws UnifyException
	{
		if(bookmark==null)
			return simpleName;
		 boolean supportsSchemasInDataManipulation = false;
	     boolean supportsCatalogsInDataManipulation = false;
	     String catSep = null;
	     ISQLDatabaseMetaData md=bookmark.getDbInfoProvider().getDatabaseMetaData();
	      try
	      {
	         supportsSchemasInDataManipulation = md.supportsSchemasInDataManipulation();
	      }
	      catch (SQLException ignore)
	      {
	         // Ignore.
	      }
	      try
	      {
	          supportsCatalogsInDataManipulation = md.supportsCatalogsInDataManipulation();
	      }
	      catch (SQLException ignore)
	      {
	         // Ignore.
	      }
	      
	      try
	      {
//	         if (supportsCatalogsInDataManipulation)
//	         {
	            catSep = md.getCatalogSeparator();
//	         }
	      }
	      catch (SQLException ignore)
	      {
	         // Ignore.
	      }
	      
	      
	      if (StringUtil.isEmpty(catSep))
	      {
	          catSep = ".";
	      }
	    		  
	      StringBuffer buf = new StringBuffer();
	      if (supportsCatalogsInDataManipulation
	    	&& !StringUtil.isEmpty(catalog))	  
	      {
	         buf.append(catalog);
	         buf.append(catSep);
	      }

	      if (supportsSchemasInDataManipulation && schema != null
	         && schema.length() > 0)
	      {
	         buf.append(schema);
	         
	         //buf.append(".");
	         buf.append(catSep);
	      }

	      String quoteExpandedName = quoteIdentifier(simpleName);
	      buf.append(quoteExpandedName);
	      return buf.toString();
	}
	public static String generateQualifiedName(String catalog,String schema,String simpleName,Bookmark bookmark) throws UnifyException
	{
		if(bookmark==null)
			return null;
		
		ISQLDatabaseMetaData md=bookmark.getDbInfoProvider().getDatabaseMetaData();
	      String catSep = null;
	      String identifierQuoteString = null;
	      boolean supportsSchemasInDataManipulation = false;
	      boolean supportsCatalogsInDataManipulation = false;

	      // check for Informix - it has very "special" qualified names
	      if (AdapterFactory.isInformix(bookmark)) {
	          return AdapterFactory.getInformixQualifiedName(catalog,schema,simpleName);
	      }
	      
	      try
	      {
	         supportsSchemasInDataManipulation = md.supportsSchemasInDataManipulation();
	      }
	      catch (SQLException ignore)
	      {
	         // Ignore.
	      }
	      try
	      {
	          supportsCatalogsInDataManipulation = md.supportsCatalogsInDataManipulation();
	      }
	      catch (SQLException ignore)
	      {
	         // Ignore.
	      }
	      
	      try
	      {
//	         if (supportsCatalogsInDataManipulation)
//	         {
	            catSep = md.getCatalogSeparator();
//	         }
	      }
	      catch (SQLException ignore)
	      {
	         // Ignore.
	      }
	      
	      
	      if (StringUtil.isEmpty(catSep))
	      {
	          catSep = ".";
	      }
	    		  
	      try
	      {
	         identifierQuoteString = md.getIdentifierQuoteString();
	         if (identifierQuoteString != null
	            && identifierQuoteString.equals(" "))
	         {
	            identifierQuoteString = null;
	         }
	      }
	      catch (SQLException ignore)
	      {
	         // Ignore.
	      }

	      StringBuffer buf = new StringBuffer();
	      //if (catSep != null
	      //   && catSep.length() > 0
	      //&& _catalog != null
	      //&& _catalog.length() > 0)
	      if (supportsCatalogsInDataManipulation
	    	&& !StringUtil.isEmpty(catalog))	  
	      {
	         if (identifierQuoteString != null)
	         {
	            buf.append(identifierQuoteString);
	         }
	         buf.append(catalog);
	         if (identifierQuoteString != null)
	         {
	            buf.append(identifierQuoteString);
	         }
	         buf.append(catSep);
	      }

	      if (supportsSchemasInDataManipulation && schema != null
	         && schema.length() > 0)
	      {
	         if (identifierQuoteString != null)
	         {
	            buf.append(identifierQuoteString);
	         }
	         buf.append(schema);
	         if (identifierQuoteString != null)
	         {
	            buf.append(identifierQuoteString);
	         }
	         
	         //buf.append(".");
	         buf.append(catSep);
	      }

	      if (identifierQuoteString != null)
	      {
	         buf.append(identifierQuoteString);
	      }
	      String quoteExpandedName = quoteIdentifier(simpleName);
	      buf.append(quoteExpandedName);
	      if (identifierQuoteString != null)
	      {
	         buf.append(identifierQuoteString);
	      }
	      return buf.toString();
	}
    /**
     * Contributed by Thomas Mueller to handle doubling quote characters 
     * found in an identifier. In H2 and other dbs, the following statement
     * creates a table with an embedded quote character:
     * 
     *  CREATE TABLE "foo""bar" (someid int);
     *  
     * However, what is returned by the driver for table name is:
     * 
     *  foo"bar
     *  
     * The reason is simple.  Just like embedded quotes in SQL strings, such as:
     * 
     * select 'I don''t know' from test
     * 
     * Similarly, embedded quote characters can also appear in identifiers 
     * such as table names, by doubling (or quoting, if you will) the quote.  
     * 
     * @param s the string to have embedded quotes expanded.
     * 
     * @return a new string with any embedded quotes doubled, or null if null is
     *         passed.
     */
    public static String quoteIdentifier(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder buff = null;
        buff = new StringBuilder();
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if(c == '"' && i != 0 && i != s.length()-1) {
                buff.append(c);
            }
            buff.append(c);
        }
        String result = buff.toString();
        return result;
    } 
    /**
     * parse the string gave by qualifiedName to a Entity object.
     * delete all quoter according to bookmark from qualifiedName.
     * @throws UnifyException threw this exception if database is not available or structure of qualifiedName is illegal.
     */
    public static Entity getTableObject(Bookmark bookmark,String qualifiedName) throws UnifyException
    {
    	if(!bookmark.isConnected())
    		throw new UnifyException("the current bookmark has not connected!");
    	qualifiedName=StringUtil.trim(qualifiedName);
    	if(qualifiedName.equals(""))
    		return null;
    	
    	ISQLDatabaseMetaData md=bookmark.getDbInfoProvider().getDatabaseMetaData();
    	String quoter;
    	try {
			quoter=md.getIdentifierQuoteString();
		} catch (SQLException e) {
			quoter=null;
		}
        if (quoter != null
	            && quoter.equals(" "))
	    {
        		quoter = null;
	    }
        
        boolean isSupportCatalog;
        boolean isSupportSchema;
		try {
			isSupportCatalog=md.supportsCatalogs();
			isSupportSchema=md.supportsSchemas();
		} catch (SQLException e) {
			throw new UnifyException(s_stringMgr.getString("sqlutil.parsequalifiedname.error.can'taccessdatabase")
					+e.getMessage(),e);
		}
		
        String object = null;
        String schema = null;
        String catalog = null;
        
    	String[] splits = qualifiedName.split("\\.");
    	if(splits.length==1)
    	{
    		object = removeQuotes(splits[0]);
            if(quoter!=null)
         	   object=object.replaceAll(quoter, "");
    	}else if(splits.length==2)
    	{
    		object=removeQuotes(splits[1]).replaceAll(quoter, "");
    		if(isSupportSchema)
    		{
    			schema=removeQuotes(splits[0]).replaceAll(quoter, "");
    		}else if(isSupportCatalog)
    		{
    			catalog=removeQuotes(splits[0]).replaceAll(quoter, "");
    		}else
    		{
    			object=removeQuotes(qualifiedName).replaceAll(quoter, "");
    		}
    		
    	}else if(splits.length==3)
    	{
    		object=removeQuotes(splits[2]).replaceAll(quoter, "");
    		schema=removeQuotes(splits[1]).replaceAll(quoter, "");
    		catalog=removeQuotes(splits[0]).replaceAll(quoter, "");
    	}else
    		throw new UnifyException(s_stringMgr.getString("sqlutil.parsequalifiedname.error.inputinvalid"));
    	
    	return EntityFactory.getInstance().create(bookmark, catalog, schema, object, TABLE, "", false);
    }
    private static String removeQuotes(String objectName)
    {
       String ret = objectName.trim();


       while(ret.startsWith("\"") || ret.startsWith("/"))
       {
          ret = ret.substring(1);
       }

       while(ret.endsWith("\"") || ret.endsWith("/"))
       {
           ret = ret.substring(0,ret.length()-1);
       }
       
       return ret;
    }
    

    public SqlUtil()
    {
    }
    /**
     * ��ȡģʽ��
     * @param qualifiedName
     * @return
     */
    public static String getSchemaName(String qualifiedName)
    {
        StringTokenizer st = new StringTokenizer(qualifiedName, ".");
        if(st.countTokens() > 1)
            return st.nextToken();
        else
            return "";
    }
    /**
     * ��ȡ����
     * @param qualifiedName
     * @return
     */
    public static String getTableName(String qualifiedName)
    {
        StringTokenizer st = new StringTokenizer(qualifiedName, ".");
        if(st.countTokens() > 1)
        {
            st.nextToken();
            return st.nextToken();
        } else
        {
            return qualifiedName;
        }
    }
    /**
     * �滻�س��ͻ���
     * @param untrans
     * @return
     */
    public static String transEscape(String untrans)
    {
        String trasposed = StringUtil.substituteString(untrans, "\\n", "\n");
        trasposed = StringUtil.substituteString(trasposed, "\\t", "\t");
        return trasposed;
    }
    /**
     *������ȡָ��ʵ�������е�sql���
     * @param entity
     * @return
     */
    public static String buildSelectAllColumnsNoRowsSql(Entity entity)
    {
        return "SELECT * FROM " + entity.getQuotedTableName() + " WHERE (1 = 0)";
    }
    /**
     * ������ȡָ��ʵ��������ݵ�sql���
     * @param entity
     * @return
     */
    public static String buildSelectAllColumnsAllRowsSql(Entity entity)
    {
        return "SELECT * FROM " + entity.getQuotedTableName();
    }
    /**
     * Get access driver created by sun (sun.jdbc.odbc.JdbcOdbcDriver)
     * @return  --return null if not exist
     */
    public static DriverInfo getDefaultDriver()
    {
        Class defaultDriver=null;
        try {
            defaultDriver=Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException e) {
            defaultDriver=null;
        }
        if(defaultDriver==null)
            return null;
        else if(java.sql.Driver.class.isAssignableFrom(defaultDriver))
            return new DriverInfo(defaultDriver.getName());
        else
            return null;
    }
    public static String qualifyColumnValue(String value,int dataType)
    {
    	
        if (isNumberType(dataType)) {
        	value=StringUtil.trim(value);
        	if(value.equals(""))
        	{
        		return "0";
        	}else
        		return value;
        } else {
        	if(value==null)
        		return value;
        	else
        		return "'"+value+"'";
        }
    }
}
