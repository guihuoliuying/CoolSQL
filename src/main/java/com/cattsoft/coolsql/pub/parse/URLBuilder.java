
package com.cattsoft.coolsql.pub.parse;

import java.util.*;

/**
 * 
 * @author liu_xlin ����ݿ����ӷ�ʽ���н���
 */
public class URLBuilder {

	public URLBuilder() {
	}

	/**
	 * �����ӷ�ʽ����Ҫ�����ֶν��и�ֵ������µ����ӵ�ַ
	 * 
	 * @param urlPattern
	 * @param properties
	 * @return
	 */
	public static String createURL(String urlPattern, Map properties) {
		StringBuffer buffer = new StringBuffer();
		boolean isVariable = false;
		String variableName = null;
		for (StringTokenizer tokenizer = new StringTokenizer(urlPattern, "{}",
				true); tokenizer.hasMoreTokens();) {
			String token = tokenizer.nextToken();
			if ("{".equals(token) && !isVariable)
				isVariable = true;
			else {
				if (isVariable && "}".equals(token) && variableName != null) {
					if (!properties.containsKey(variableName)) {
						buffer.append("{");
						buffer.append(variableName);
						buffer.append("}");
					} else {
						buffer.append(properties.get(variableName));
					}
					isVariable = false;
				} else if (isVariable)
					variableName = token;
				else
					buffer.append(token);
			}
		}

		return buffer.toString();
	}
    /**
     * ��ȡ���ӷ�ʽ����Ҫ���е��ֶ�
     * @param urlPattern
     * @return
     */
	public static String[] getVariableNames(String urlPattern) {
		List list = new ArrayList();
		if (urlPattern != null) {
			boolean isVariable = false;
			String variableName = null;
			for (StringTokenizer tokenizer = new StringTokenizer(urlPattern,
					"{}", true); tokenizer.hasMoreTokens();) {
				String token = tokenizer.nextToken();
				if ("{".equals(token) && !isVariable)
					isVariable = true;
				else if (isVariable && "}".equals(token)
						&& variableName != null) {
					list.add(variableName);
					isVariable = false;
				} else if (isVariable)
					variableName = token;
			}

		}
		return (String[]) list.toArray(new String[list.size()]);
	}
}
