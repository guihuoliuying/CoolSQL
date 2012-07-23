/*
 * �������� 2006-11-27
 */
package com.cattsoft.coolsql.pub.parse.xml.token;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.cattsoft.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.cattsoft.coolsql.pub.parse.xml.XMLConstant;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.pub.parse.xml.XMLFactory;

/**
 * @author liu_xlin
 *���϶����������
 */
public class CollectionXMLToken extends BeanAndXMLParse {
    public CollectionXMLToken(Class cl)
    {
        this(cl,null,null);
    }
    public CollectionXMLToken(Class cl,String propertyName,Object propertyValue)
    {
        super(cl,propertyName,propertyValue);   
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
    /* <collection><value></value></collection>
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getXMLElement()
     */
    public Element getXMLElement() throws XMLException {
        Element element=getInitedEelement();
        if(getValue()==null)
        {
//            element.addContent(XMLConstant.NULL);
            return element;
        }
        
        BeanAndXMLParse parser=null;
        Collection tmpValue=(Collection)getValue();
        Iterator it=tmpValue.iterator();
        while(it.hasNext())
        {
            Object elementObject=it.next();
            parser=XMLFactory.getBeanToXMLParser(elementObject.getClass(),true);
            if(parser==null) //����Ҳ�����Ӧ�Ľ������ͣ������κδ��?ֱ�ӽ�����һ���ԵĽ���
                continue;
            parser.setBean(tmpValue);
            if(elementObject instanceof String)
            parser.setPropertyName(null);
            parser.setPropertyValue(elementObject);
            parser.setElementName(XMLConstant.TAG_PROPERTY_VALUE);
  
            element.addContent(parser.getXMLElement());
        }
        return element;
    }

    /* ���� Javadoc��
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        Element element = getElement();
        List children = element.getChildren();
        Iterator it = children.iterator();
        
        Collection collection=null;
        try {
            collection=(Collection)getJavaClass().newInstance();
        } catch (Exception e1) {
            throw new XMLException(e1);
        }
        
        while(it.hasNext())
        {
            Element child = (Element) it.next();  //Ԫ�ض���
   
            BeanAndXMLParse parser = XMLFactory.getBeanToXMLParser(child
                    .getAttributeValue(XMLConstant.TAG_ARRTIBUTE_DATATYPE), false); //��������
            parser.setElement(child);
            parser.setPropertyName(child.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME));
            Object ob = parser.getObjectInXML();
            collection.add(ob);
        }
        return collection;
    }

}
