/*
 * �������� 2006-11-7
 */
package com.cattsoft.coolsql.pub.parse.xml.token;

import java.util.List;

import org.jdom.Element;

import com.cattsoft.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.cattsoft.coolsql.pub.parse.xml.XMLConstant;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.pub.parse.xml.XMLFactory;

/**
 * @author liu_xlin
 *����������XML��ת��������
 */
public class ArrayToXML extends BeanAndXMLParse {
    public ArrayToXML(Class cl)
    {
        this(cl,null,null);
    }
    public ArrayToXML(Class cl,String propertyName,Object propertyValue)
    {
        super(null,propertyName,propertyValue);   
    }
    /* ���� Javadoc��
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getXMLElement()
     */
    public Element getXMLElement() throws XMLException {
        checkInfoIntegrality();
        
        Element element=getInitedEelement();
        
        if(getValue()==null)
        {
//            element.addContent(XMLConstant.NULL);
            return element;
        }
        
        Object[] tmpValue=(Object[])getValue();
        BeanAndXMLParse parser=XMLFactory.getBeanToXMLParser(getJavaClass().getComponentType(),true);
        if(parser==null) //����Ҳ�����Ӧ�Ľ������ͣ������κδ��?ֱ�ӽ�����һ���ԵĽ���
            return null;
        
        for(int i=0;i<tmpValue.length;i++)
        {
            parser.setBean(getBean());
            parser.setPropertyName(XMLConstant.NODEFINED);
            parser.setPropertyValue(tmpValue[i]);
  
            element.addContent(parser.getXMLElement());
        }
        return element;
    }

    public boolean isArray()
    {
        return true;
    }
    /**
     * ��������ֵ�����Ǹ÷�������ȷ����ԭʼ������͵���������ʵ��
     */
    public void setPropertyValue(Object value)
    {
        super.setPropertyValue(value);
        if(getValue()!=null)
            setDataType(getValue().getClass());
    }
    /* ���� Javadoc��
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        Element element=getElement();
        List children=element.getChildren();
        /**
         * ʵ��һ��ָ�����͵��������
         */
        Object[] data = new Object[children.size()];
        
        BeanAndXMLParse parser=XMLFactory.getBeanToXMLParser(getJavaClass().getComponentType(),false);
        
        for(int i=0;i<children.size();i++)
        {
            Element e=(Element)children.get(i);
            parser.setPropertyName(e.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME));
            parser.setElement(e);
            
            data[i]=parser.getObjectInXML();
        }
        return data;
    }
}
