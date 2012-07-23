package com.cattsoft.coolsql.pub.display;

import java.util.Collection;
import java.util.Iterator;
/**
 * ANSI-SQL token marker.
 *
 */
public class AnsiSQLTokenMarker 
	extends SQLTokenMarker
{
	// public members
	public AnsiSQLTokenMarker()
	{
		super();
		initKeywordMap();
	}

	public void setSqlKeyWords(Collection keywords)
	{
		this.addKeywordList(keywords, Token.KEYWORD1);
	}

	public void setSqlFunctions(Collection functions)
	{
		this.addKeywordList(functions, Token.KEYWORD3);
	}

	private void addKeywordList(Collection words, byte anId)
	{
		if (words == null) return;
		for (Iterator it=words.iterator();it.hasNext();)
		{
			String keyword=(String)it.next();
			if (!keywords.containsKey(keyword))
			{
				//System.out.println("adding key=" + keyword);
				keywords.add(keyword.toUpperCase().trim(),anId);
			}
		}
	}

	public void setIsMySQL(boolean flag)
	{
		this.isMySql = flag;
	}

	public void initKeywordMap()
	{
		keywords = new KeywordMap(true, 80);
		addKeywords();
		addDataTypes();
		addSystemFunctions();
		addOperators();
	}

	private void addKeywords()
	{
		keywords.add("AVG",Token.KEYWORD1);
		keywords.add("ALIAS",Token.KEYWORD1);
		keywords.add("ADD",Token.KEYWORD1);
		keywords.add("ALTER",Token.KEYWORD1);
		keywords.add("AS",Token.KEYWORD1);
		keywords.add("ASC",Token.KEYWORD1);
		keywords.add("BEGIN",Token.KEYWORD1);
		keywords.add("BREAK",Token.KEYWORD1);
		keywords.add("BY",Token.KEYWORD1);
		keywords.add("CASE",Token.KEYWORD1);
		keywords.add("CASCADE",Token.KEYWORD1);
		keywords.add("CHECK",Token.KEYWORD1);
		keywords.add("CHECKPOINT",Token.KEYWORD1);
		keywords.add("CLOSE",Token.KEYWORD1);
		keywords.add("CLUSTERED",Token.KEYWORD1);
		keywords.add("COLUMN",Token.KEYWORD1);
		keywords.add("COMMENT",Token.KEYWORD1);
		keywords.add("COMMIT",Token.KEYWORD1);
		keywords.add("CONSTRAINT",Token.KEYWORD1);
		keywords.add("CREATE",Token.KEYWORD1);
		keywords.add("CURRENT",Token.KEYWORD1);
		keywords.add("CURRENT_DATE",Token.KEYWORD1);
		keywords.add("CURRENT_TIME",Token.KEYWORD1);
		keywords.add("CURRENT_TIMESTAMP",Token.KEYWORD1);
		keywords.add("CURSOR",Token.KEYWORD1);
		keywords.add("DATABASE",Token.KEYWORD1);
		keywords.add("DECLARE",Token.KEYWORD1);
		keywords.add("DEFAULT",Token.KEYWORD1);
		keywords.add("DEFERRABLE",Token.KEYWORD1);
		keywords.add("DEFERRED",Token.KEYWORD1);
		keywords.add("DELETE",Token.KEYWORD1);
		keywords.add("DENY",Token.KEYWORD1);
		keywords.add("DISTINCT",Token.KEYWORD1);
		keywords.add("DROP",Token.KEYWORD1);
		keywords.add("EXEC",Token.KEYWORD1);
		keywords.add("EXECUTE",Token.KEYWORD1);
		keywords.add("EXIT",Token.KEYWORD1);
		keywords.add("END",Token.KEYWORD1);
		keywords.add("ELSE",Token.KEYWORD1);
		keywords.add("FETCH",Token.KEYWORD1);
		keywords.add("FOR",Token.KEYWORD1);
		keywords.add("FOREIGN",Token.KEYWORD1);
		keywords.add("FROM",Token.KEYWORD1);
		keywords.add("GRANT",Token.KEYWORD1);
		keywords.add("GROUP",Token.KEYWORD1);
		keywords.add("HAVING",Token.KEYWORD1);
		keywords.add("IF",Token.KEYWORD1);
		keywords.add("INDEX",Token.KEYWORD1);
		keywords.add("INNER",Token.KEYWORD1);
		keywords.add("INITIALLY",Token.KEYWORD1);
		keywords.add("INSERT",Token.KEYWORD1);
		keywords.add("INTO",Token.KEYWORD1);
		keywords.add("IS",Token.KEYWORD1);
		keywords.add("ISOLATION",Token.KEYWORD1);
		keywords.add("KEY",Token.KEYWORD1);
		keywords.add("LEVEL",Token.KEYWORD1);
		keywords.add("MAX",Token.KEYWORD1);
		keywords.add("MIN",Token.KEYWORD1);
		keywords.add("MIRROREXIT",Token.KEYWORD1);
		keywords.add("NATIONAL",Token.KEYWORD1);
		keywords.add("NOCHECK",Token.KEYWORD1);
		keywords.add("OF",Token.KEYWORD1);
		keywords.add("ON",Token.KEYWORD1);
		keywords.add("ORDER",Token.KEYWORD1);
		keywords.add("PREPARE",Token.KEYWORD1);
		keywords.add("PRIMARY",Token.KEYWORD1);
		keywords.add("PRIVILEGES",Token.KEYWORD1);
		keywords.add("PROCEDURE",Token.KEYWORD1);
		keywords.add("FUNCTION",Token.KEYWORD1);
		keywords.add("PACKAGE",Token.KEYWORD1);
		keywords.add("BODY",Token.KEYWORD1);
		keywords.add("REFERENCES",Token.KEYWORD1);
		keywords.add("RESTORE",Token.KEYWORD1);
		keywords.add("RESTRICT",Token.KEYWORD1);
		keywords.add("REVOKE",Token.KEYWORD1);
		keywords.add("ROLLBACK",Token.KEYWORD1);
		keywords.add("SCHEMA",Token.KEYWORD1);
		keywords.add("SYNONYM",Token.KEYWORD1);
		keywords.add("SELECT",Token.KEYWORD1);
		keywords.add("SET",Token.KEYWORD1);
		keywords.add("TABLE",Token.KEYWORD1);
		keywords.add("TO",Token.KEYWORD1);
		keywords.add("TRANSACTION",Token.KEYWORD1);
		keywords.add("TRIGGER",Token.KEYWORD1);
		keywords.add("TRUNCATE",Token.KEYWORD1);
		keywords.add("UNION",Token.KEYWORD1);
		keywords.add("MINUS",Token.KEYWORD1);
		keywords.add("INTERSECT",Token.KEYWORD1);
		keywords.add("UNIQUE",Token.KEYWORD1);
		keywords.add("UPDATE",Token.KEYWORD1);
		keywords.add("VALUES",Token.KEYWORD1);
		keywords.add("VARYING",Token.KEYWORD1);
		keywords.add("VIEW",Token.KEYWORD1);
		keywords.add("WHERE",Token.KEYWORD1);
		keywords.add("WHEN",Token.KEYWORD1);
		keywords.add("WITH",Token.KEYWORD1);
		keywords.add("WORK",Token.KEYWORD1);

		keywords.add("DESC",Token.KEYWORD2);
		keywords.add("DESCRIBE",Token.KEYWORD2);
//		keywords.add("WBLIST",Token.KEYWORD2);
//		keywords.add("WBLISTPROCS",Token.KEYWORD2);
//		keywords.add("WBLISTDB",Token.KEYWORD2);
//		keywords.add("WBLISTCAT",Token.KEYWORD2);
//		keywords.add(WbConfirm.VERB,Token.KEYWORD2);
//		keywords.add(WbEnableOraOutput.VERB,Token.KEYWORD2);
//		keywords.add(WbDisableOraOutput.VERB,Token.KEYWORD2);
//		keywords.add(WbExport.VERB,Token.KEYWORD2);
//		keywords.add(WbImport.VERB,Token.KEYWORD2);
//		keywords.add(WbFeedback.VERB,Token.KEYWORD2);
//		keywords.add(WbInclude.VERB,Token.KEYWORD2);
//		keywords.add(WbCopy.VERB,Token.KEYWORD2);
//		keywords.add(WbDefineVar.VERB_DEFINE_SHORT,Token.KEYWORD2);
//		keywords.add(WbDefineVar.VERB_DEFINE_LONG,Token.KEYWORD2);
//		keywords.add(WbListVars.VERB,Token.KEYWORD2);
//		keywords.add(WbRemoveVar.VERB,Token.KEYWORD2);
//		keywords.add(WbStartBatch.VERB,Token.KEYWORD2);
//		keywords.add(WbEndBatch.VERB,Token.KEYWORD2);
//		keywords.add(WbFeedback.VERB,Token.KEYWORD2);
//		keywords.add(WbSchemaReport.VERB,Token.KEYWORD2);
//		keywords.add(WbSchemaDiff.VERB,Token.KEYWORD2);
//		keywords.add(WbXslt.VERB,Token.KEYWORD2);
//		keywords.add(WbSelectBlob.VERB,Token.KEYWORD2);
//		keywords.add(WbDefinePk.VERB,Token.KEYWORD2);
//		keywords.add(WbListPkDef.VERB,Token.KEYWORD2);
//		keywords.add(WbSavePkMapping.VERB,Token.KEYWORD2);
//		keywords.add(WbLoadPkMapping.VERB,Token.KEYWORD2);
//		keywords.add(WbCall.VERB, Token.KEYWORD2);
	}

	private void addDataTypes()
	{
		keywords.add("binary",Token.KEYWORD2);
		keywords.add("bit",Token.KEYWORD2);
		keywords.add("char",Token.KEYWORD2);
		keywords.add("character",Token.KEYWORD2);
		keywords.add("datetime",Token.KEYWORD2);
		keywords.add("date",Token.KEYWORD2);
		keywords.add("decimal",Token.KEYWORD2);
		keywords.add("float",Token.KEYWORD2);
		keywords.add("image",Token.KEYWORD2);
		keywords.add("int",Token.KEYWORD2);
		keywords.add("int4",Token.KEYWORD2);
		keywords.add("int8",Token.KEYWORD2);
		keywords.add("integer",Token.KEYWORD2);
		keywords.add("bigint",Token.KEYWORD2);
		keywords.add("money",Token.KEYWORD2);
		//keywords.add("name",Token.KEYWORD1);
		keywords.add("number",Token.KEYWORD2);
		keywords.add("numeric",Token.KEYWORD2);
		keywords.add("nchar",Token.KEYWORD2);
		keywords.add("nvarchar",Token.KEYWORD2);
		keywords.add("ntext",Token.KEYWORD2);
		keywords.add("real",Token.KEYWORD1);
		keywords.add("smalldatetime",Token.KEYWORD2);
		keywords.add("smallint",Token.KEYWORD2);
		keywords.add("smallmoney",Token.KEYWORD2);
		keywords.add("text",Token.KEYWORD2);
		keywords.add("timestamp",Token.KEYWORD2);
		keywords.add("tinyint",Token.KEYWORD2);
		keywords.add("uniqueidentifier",Token.KEYWORD2);
		keywords.add("varbinary",Token.KEYWORD2);
		keywords.add("varchar",Token.KEYWORD2);
		keywords.add("nvarchar",Token.KEYWORD2);
		keywords.add("varchar2",Token.KEYWORD2);
		keywords.add("nvarchar2",Token.KEYWORD2);
		keywords.add("clob",Token.KEYWORD2);
		keywords.add("nclob",Token.KEYWORD2);
	}

	private void addSystemFunctions()
	{
		keywords.add("ABS",Token.KEYWORD3);
		keywords.add("ACOS",Token.KEYWORD3);
		keywords.add("ASIN",Token.KEYWORD3);
		keywords.add("ATAN",Token.KEYWORD3);
		keywords.add("ATN2",Token.KEYWORD3);
		keywords.add("CAST",Token.KEYWORD3);
		keywords.add("CEILING",Token.KEYWORD3);
		keywords.add("COS",Token.KEYWORD3);
		keywords.add("COT",Token.KEYWORD3);
		keywords.add("COUNT", Token.KEYWORD3);
		keywords.add("CURRENT_TIME",Token.KEYWORD3);
		keywords.add("CURRENT_DATE",Token.KEYWORD3);
		keywords.add("CURRENT_TIMESTAMP",Token.KEYWORD3);
		keywords.add("CURRENT_USER",Token.KEYWORD3);
		keywords.add("DATALENGTH",Token.KEYWORD3);
		keywords.add("DATEADD",Token.KEYWORD3);
		keywords.add("DATEDIFF",Token.KEYWORD3);
		keywords.add("DATENAME",Token.KEYWORD3);
		keywords.add("DATEPART",Token.KEYWORD3);
		keywords.add("DAY",Token.KEYWORD3);
		keywords.add("EXP",Token.KEYWORD3);
		keywords.add("FLOOR",Token.KEYWORD3);
		keywords.add("LOG",Token.KEYWORD3);
		keywords.add("MONTH",Token.KEYWORD3);
		keywords.add("ROUND",Token.KEYWORD3);
		keywords.add("SIN",Token.KEYWORD3);
		keywords.add("SOUNDEX",Token.KEYWORD3);
		keywords.add("SPACE",Token.KEYWORD3);
		keywords.add("SQRT",Token.KEYWORD3);
		keywords.add("SQUARE",Token.KEYWORD3);
		keywords.add("TAN",Token.KEYWORD3);
		keywords.add("UPPER",Token.KEYWORD3);
		keywords.add("USER",Token.KEYWORD3);
		keywords.add("YEAR",Token.KEYWORD3);
	}

	private void addOperators()
	{
		keywords.add("ALL",Token.OPERATOR);
		keywords.add("AND",Token.OPERATOR);
		keywords.add("ANY",Token.OPERATOR);
		keywords.add("BETWEEN",Token.OPERATOR);
		keywords.add("CROSS",Token.OPERATOR);
		keywords.add("EXISTS",Token.OPERATOR);
		keywords.add("IN",Token.OPERATOR);
		keywords.add("JOIN",Token.OPERATOR);
		keywords.add("LIKE",Token.OPERATOR);
		keywords.add("NOT",Token.OPERATOR);
		keywords.add("NULL",Token.OPERATOR);
		keywords.add("OR",Token.OPERATOR);
		keywords.add("OUTER",Token.OPERATOR);
		keywords.add("SOME",Token.OPERATOR);
		keywords.add("RIGHT",Token.OPERATOR);
		keywords.add("LEFT",Token.OPERATOR);
		keywords.add("FULL",Token.OPERATOR);
	}
}