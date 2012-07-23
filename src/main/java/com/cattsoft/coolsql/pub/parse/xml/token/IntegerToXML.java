/*
 * �������� 2006-11-7
 */
package com.cattsoft.coolsql.pub.parse.xml.token;

import org.jdom.Element;

import com.cattsoft.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;

/**
 * @author liu_xlin
 *int������XML��ת��������
 */
public class IntegerToXML extends BeanAndXMLParse {
    public IntegerToXML()
    {
        this(null,null);
    }
    public IntegerToXML(String propertyName,Object propertyValue)
    {
        super(Integer.TYPE,propertyName,propertyValue);   
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
        
        element.addContent(getValue().toString());
        return element;
    }
    /* ���� Javadoc��
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        String txt=getElement().getTextTrim();
        if(!isNull())
        {
            Object value=null;
            try
            {
                value=new Integer(txt);
            }catch(NumberFormatException e)
            {
                value=new Integer(0);
            }
            return value;
        }else
            return new Integer(0);
    }

}
