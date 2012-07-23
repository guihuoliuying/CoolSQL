/*
 * �������� 2006-11-6
 */
package com.cattsoft.coolsql.pub.parse.xml;

import org.jdom.Element;

/**
 * @author liu_xlin
 * �ýӿ�������java����ת��Ϊxml�ĵ��ṹ
 */
public interface XMLPropertyForWrite extends XMLEditor{


    
    /**
     * ��ȡ���Ե�xml����
     * @return
     */
    public abstract Element getXMLElement() throws XMLException;
    
    /**
     * ��ȡbean����
     * @return
     */
    public abstract Object getBean();
}
