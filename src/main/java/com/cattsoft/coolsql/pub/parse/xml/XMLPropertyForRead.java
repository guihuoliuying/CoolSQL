/*
 * �������� 2006-11-8
 */
package com.cattsoft.coolsql.pub.parse.xml;

import org.jdom.Element;

/**
 * @author liu_xlin
 *�ýӿ���������xml�ĵ��еĶ�����Ϣ
 */
public interface XMLPropertyForRead extends XMLEditor{

    /**
     * ��ȡ��xml�ļ��н�����Ķ���
     * @return
     */
    public abstract Object getObjectInXML() throws XMLException;
    
    /**
     * ��ȡ�ϲ��ĵ�Ԫ��
     * @return
     */
    public abstract Element getElement();
}
