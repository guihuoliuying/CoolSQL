package com.cattsoft.coolsql.sql.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Types;

public class TypesHelper {

	public TypesHelper() {
	}

	private static int getType(String typeName, int defaultValue) {
		try {
			Field field = java.sql.Types.class.getField(typeName);
			defaultValue = field.getInt(null);
		} catch (NoSuchFieldException nosuchfieldexception) {
		} catch (IllegalAccessException illegalaccessexception) {
		}
		return defaultValue;
	}

	public static String getTypeName(int type) {
		String name = null;
		try {
			Field fields[] = com.cattsoft.coolsql.sql.util.TypesHelper.class.getFields();
			int i = 0;
			for (int length = fields != null ? fields.length : 0; name == null
					&& i < length; i++)
				if (Modifier.isStatic(fields[i].getModifiers())
						&& Modifier.isPublic(fields[i].getModifiers())
						&& fields[i].getType() == Integer.TYPE
						&& type == fields[i].getInt(null))
					name = fields[i].getName();

		} catch (IllegalAccessException illegalaccessexception) {
		}
		return name;
	}

	public static boolean isReal(int type) {
		return type == DECIMAL || type == DOUBLE || type == FLOAT
				|| type == NUMERIC || type == REAL;
	}

	public static boolean isNumberic(int type) {
		return type == DECIMAL || type == DOUBLE || type == FLOAT
				|| type == NUMERIC || type == REAL || type == BIGINT
				|| type == TINYINT || type == SMALLINT || type == INTEGER;
	}

	public static boolean isText(int type) {
		return type == CHAR || type == VARCHAR || type == LONGVARCHAR
				|| type == BINARY || type == VARBINARY || type == LONGVARBINARY
				|| type == CLOB;
	}

	public static boolean isDateTime(int type)
	{
		return type==DATE||type==TIME||type==TIMESTAMP;
	}
	public static boolean isLob(int type) {
		return type == JAVA_OBJECT || type == BLOB || type == CLOB;
	}

	public static boolean isLongType(int type)
	{
		return isLob(type)||
			type==Types.LONGVARBINARY||
			type==Types.LONGVARCHAR;
	}

	public static final int BIT = -7;

	public static final int TINYINT = -6;

	public static final int SMALLINT = 5;

	public static final int INTEGER = 4;

	public static final int BIGINT = -5;

	public static final int FLOAT = 6;

	public static final int REAL = 7;

	public static final int DOUBLE = 8;

	public static final int NUMERIC = 2;

	public static final int DECIMAL = 3;

	public static final int CHAR = 1;

	public static final int VARCHAR = 12;

	public static final int LONGVARCHAR = -1;

	public static final int DATE = 91;

	public static final int TIME = 92;

	public static final int TIMESTAMP = 93;

	public static final int BINARY = -2;

	public static final int VARBINARY = -3;

	public static final int LONGVARBINARY = -4;

	public static final int NULL = 0;

	public static final int OTHER = 1111;

	public static final int JAVA_OBJECT = 2000;

	public static final int DISTINCT = 2001;

	public static final int STRUCT = 2002;

	public static final int ARRAY = 2003;

	public static final int BLOB = 2004;

	public static final int CLOB = 2005;

	public static final int REF = 2006;

	public static final int DATALINK = getType("DATALINK", 70);

	public static final int BOOLEAN = getType("BOOLEAN", 16);

}
