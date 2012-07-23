/*
 * �������� 2006-11-10
 */
package com.cattsoft.coolsql.pub.parse.xml.token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;

import com.cattsoft.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.cattsoft.coolsql.pub.parse.xml.XMLBeanUtil;
import com.cattsoft.coolsql.pub.parse.xml.XMLConstant;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.pub.parse.xml.XMLFactory;

/**
 * @author liu_xlin ����map����ֵ�ԣ���xml��ת��
 */
public class MapXMLToken extends BeanAndXMLParse {

    public MapXMLToken() {
        this(java.util.HashMap.class);
    }

    public MapXMLToken(Class type) {
        super(type, null, null);
    }

    /**
     * ��������ֵ�����Ǹ÷�������ȷ����ԭʼ������͵���������ʵ��
     */
    public void setPropertyValue(Object value) {
        super.setPropertyValue(value);
        if (this.getValue() != null)
            this.setDataType(this.getValue().getClass());
    }

    /*
     * <map> <key-value> <key> </key> <value> </value> </key-value> </map>
     * 
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getXMLElement()
     */
    public Element getXMLElement() throws XMLException {
        Element root = this.getInitedEelement();
        Map map = (Map) this.getValue();
        if (map == null)
            return null;

        Set set = map.keySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Element e = new Element(XMLConstant.TAG_PROPERTY_KEYVALUE);

            //��ȡ������Ԫ��
            Object key = it.next();
            BeanAndXMLParse parser = XMLFactory.getBeanToXMLParser(key
                    .getClass(), true);
            parser.setPropertyValue(key);
            parser.setPropertyName(XMLConstant.NO_NAME);
            parser.setBean(map);
            parser.setElementName(XMLConstant.TAG_PROPERTY);

            Element keyElement =new Element(XMLConstant.TAG_PROPERTY_KEY);
            keyElement.addContent(parser.getXMLElement());
            e.addContent(keyElement);

            //��ȡֵ�����Ԫ��
            Object value = map.get(key);
            parser = XMLFactory.getBeanToXMLParser(value.getClass(), true);
            parser.setPropertyValue(value);
            parser.setPropertyName(XMLConstant.NO_NAME);
            parser.setElementName(XMLConstant.TAG_PROPERTY);
            parser.setBean(map);

            Element valueElement = new Element(XMLConstant.TAG_PROPERTY_VALUE);
            valueElement.addContent(parser.getXMLElement());
            e.addContent(valueElement);

            root.addContent(e);
        }
        return root;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        Element element = getElement();
        List children = element.getChildren();
        Iterator it = children.iterator();

        XMLBeanUtil xml = new XMLBeanUtil();
        Map map;
        try {
            map = (Map) this.getJavaClass().newInstance();
        } catch (Exception e1) {
            throw new XMLException(e1);
        }
        while (it.hasNext()) {
            Element e = (Element) it.next();//��key-value��
            Element keyElement = e.getChild(XMLConstant.TAG_PROPERTY_KEY); //key
            if (keyElement == null)
                throw new XMLException(
                        "key-value of map token in xml file must include key element");
            List keyChildren=keyElement.getChildren();
            if(keyChildren.size()>1)
            	throw new XMLException("The key element of map element must have only one child");
            else if(keyChildren.size()==0)
            {
            	throw new XMLException("The key element of map element must have a child");
            }
            keyElement=(Element)keyChildren.get(0);
            
            BeanAndXMLParse parser = XMLFactory.getBeanToXMLParser(keyElement
                    .getAttributeValue(XMLConstant.TAG_ARRTIBUTE_DATATYPE), false); //��������
            parser.setElement(keyElement);
            parser.setPropertyName(keyElement.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME));
            
            Object key = parser.getObjectInXML();

            Element valueElement = e.getChild(XMLConstant.TAG_PROPERTY_VALUE); //value
            if (valueElement == null)
                throw new XMLException(
                        "key-value of map token in xml file must include value element");
            List valueChildren=valueElement.getChildren();
            Object value=null;
            if(valueChildren.size()>1)
            	throw new XMLException("The value element of map element must have only one child");
            else if(valueChildren.size()==0)
            {
            	
            }else
            {
		        valueElement = (Element) valueChildren.get(0);
				parser = XMLFactory.getBeanToXMLParser(valueElement
						.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_DATATYPE),
						false); // ��������
				parser.setElement(valueElement);
				parser.setPropertyName(valueElement
						.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME));
				value = parser.getObjectInXML();
            }
             

            map.put(key, value);
        }

        return map;
    }

}
