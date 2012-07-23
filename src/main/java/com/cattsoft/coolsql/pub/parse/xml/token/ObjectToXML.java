/*
 * �������� 2006-11-7
 */
package com.cattsoft.coolsql.pub.parse.xml.token;

import org.jdom.Element;

import com.cattsoft.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.cattsoft.coolsql.pub.parse.xml.XMLBeanUtil;
import com.cattsoft.coolsql.pub.parse.xml.XMLConstant;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;

/**
 * @author liu_xlin ����������XML��ת��������
 */
public class ObjectToXML extends BeanAndXMLParse {
    public ObjectToXML(Class cl) {
        super(cl, null, null);
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getXMLElement()
     */
    public Element getXMLElement() throws XMLException {
        this.checkInfoIntegrality();

        XMLBeanUtil xml = new XMLBeanUtil();
        Element element = xml.parseBean(this.getValue(),
                XMLConstant.TAG_PROPERTY_BEAN);
        if(element==null)
            throw new XMLException("can't get a java bean");
        
        if (getName() != null)
            element
                    .setAttribute(XMLConstant.TAG_ARRTIBUTE_NAME,getName());
        element.setAttribute(XMLConstant.TAG_ARRTIBUTE_DATATYPE,getJavaClass().getName());

        return element;
    }

    /**
     * ��������ֵ�����Ǹ÷�������ȷ����ԭʼ������͵���������ʵ��
     */
    public void setPropertyValue(Object value) {
        super.setPropertyValue(value);
        if (this.getValue() != null)
            setDataType(getValue().getClass());
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        XMLBeanUtil xml = new XMLBeanUtil();
        return xml.getBean(getElement());

    }

}
