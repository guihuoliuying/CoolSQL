/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @author liu_xlin ��Ҫ���?�������ࡣ
 */
public class OperatorFactory {
	private static Map<String,Operatable> cacheOperators=Collections.synchronizedMap(new HashMap<String,Operatable>());
	private OperatorFactory() {
	}
	/**
	 * ͨ����������������ȡ������ʵ��
	 */
	public static Operatable getOperator(Class c)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if(c==null)
			return null;
		if(!Operatable.class.isAssignableFrom(c))
			return null;
		Operatable operator=cacheOperators.get(c.getName());
		if(operator==null)
		{
			operator=(Operatable)c.newInstance();
			cacheOperators.put(c.getName(), operator);
		}
		
		return operator;
	}
	public static Operatable getOperator(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return getOperator(className,null);
	}
	public static Operatable getOperator(String className,ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Operatable operator=cacheOperators.get(className);
		if(operator==null)
		{
			Class c;
			if(classLoader==null)
				c= Class.forName(className);
			else
				c=classLoader.loadClass(className);
			if(!Operatable.class.isAssignableFrom(c))
				return null;
			operator=(Operatable) c.newInstance();
			cacheOperators.put(className, operator);
		}
		
		return operator;
	}
}
