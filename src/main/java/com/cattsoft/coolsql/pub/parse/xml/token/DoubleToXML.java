/*
 * �������� 2006-11-7
 */
package com.cattsoft.coolsql.pub.parse.xml.token;

import org.jdom.Element;

import com.cattsoft.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;

/**
 * @author liu_xlin
 *double������XML��ת��������
 */
public class DoubleToXML extends BeanAndXMLParse {
    public DoubleToXML()
    {
        this(null,null);
    }
    public DoubleToXML(String propertyName,Object propertyValue)
    {
        super(Double.TYPE,propertyName,propertyValue);   
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
                value=new Double(txt);
            }catch(NumberFormatException e)
            {
                value=new Double(0);
            }
            return value;
        }else
            return new Double(0);
    }

}
