/*
 * �������� 2006-11-7
 */
package com.cattsoft.coolsql.pub.parse.xml.token;

import org.jdom.Element;

import com.cattsoft.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;

/**
 * @author liu_xlin char������XML��ת��������
 */
public class CharacterToXML extends BeanAndXMLParse {
    public CharacterToXML() {
        this(null, null);
    }

    public CharacterToXML(String propertyName, Object propertyValue) {
        super(Character.TYPE, propertyName, propertyValue);
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getXMLElement()
     */
    public Element getXMLElement() throws XMLException {
        checkInfoIntegrality();

        Element element = getInitedEelement();
        if (getValue() == null) {
            //            element.addContent(XMLConstant.NULL);
            return element;
        }

        element.addContent(getValue().toString());
        return element;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        String txt = getElement().getTextTrim();
        if (!isNull()) {
            if (txt.length() != 1)
                throw new XMLException(
                        "must include only one character!(attribute name:"
                                + getName() + ",datatype:"
                                + getJavaClass().getName() + "//content " + txt
                                + ")");
            Object value = new Character(txt.charAt(0));
            return value;
        } else
            return new Character(' ');
    }

}
