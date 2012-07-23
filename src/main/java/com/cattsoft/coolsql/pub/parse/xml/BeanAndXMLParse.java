/*
 * �������� 2006-11-7
 */
package com.cattsoft.coolsql.pub.parse.xml;

import java.beans.PropertyDescriptor;

import org.jdom.Element;


/**
 * @author liu_xlin
 * ���ͽ����Ļ���
 */
public abstract class BeanAndXMLParse implements XMLPropertyForWrite,XMLPropertyForRead {

    private String propertyName=null;   //�������
    private Object propertyValue=null;  //����ֵ
    private Class dataType=null;  //��������
    
    private Object bean=null;  //��ǰ������������
    
    private Element element=null;//���Ϊxml to bean����ô��Ҫ��ʼ����ֵ
    private String elementName=null;
    
    private boolean editorType;//�༭����
    private PropertyDescriptor propertyDes=null;  //���Ե�������
    
    private boolean isNull=false;//����ֵ�Ƿ�Ϊnull
    
    public BeanAndXMLParse(Class dataType,String propertyName,Object propertyValue)
    {
        this(dataType,propertyName,propertyValue,XMLConstant.TAG_PROPERTY,false);
    }   
    public BeanAndXMLParse(Class dataType,String propertyName,Object propertyValue,String elementName,boolean isNull)
    {
        this.dataType=dataType;
        this.propertyName=propertyName;
        this.propertyValue=propertyValue;
        this.elementName=elementName;
        this.isNull=isNull;
    }
    /* ���� Javadoc��
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getName()
     */
    public String getName() {
       return propertyName;
    }

    /* ���� Javadoc��
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getJavaClass()
     */
    public Class getJavaClass() {
        return dataType;
    }

    /* ���� Javadoc��
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getValue()
     */
    public Object getValue() {
        return propertyValue;
    }
    public Object getBean()
    {
        return bean;
    }
    /* ���� Javadoc��
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getParentElement()
     */
    public Element getElement() {
        return element;
    }
    public boolean editorType()
    {
        return editorType;
    }
    public PropertyDescriptor getPropertyDescriptor()
    {
      return propertyDes;   
    }
    public boolean isNull()
    {
        return isNull;
    }
    /**
     * Ĭ��Ϊ������������
     */
    public boolean isArray()
    {
        return false;
    }

    public void setDataType(Class dataType) {
        this.dataType = dataType;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }
    public void setElement(Element element) {
        this.element = element;
    }
    public void setNull(boolean isNull)
    {
      this.isNull=isNull;   
    }

    public boolean checkInfoIntegrality() throws XMLException
    {
//        if(propertyName!=null&&propertyValue!=null&&(isArray()||dataType!=null))
//            return true;
//        else
//            throw new XMLException("data is not integrated!");
        return true;
    }
    public void setBean(Object bean) {
        this.bean = bean;
    }
    public void setEditorType(boolean editorType) {
        this.editorType = editorType;
    }
    public void setPropertyDes(PropertyDescriptor propertyDes) {
        this.propertyDes = propertyDes;
    }
    /**
     * ��Ԫ�س�ʼ��
     * @return
     * @throws XMLException
     */
    public Element getInitedEelement() throws XMLException
    {
        Element element=new Element(getElementName());
        if(propertyName!=null)
            element.setAttribute(XMLConstant.TAG_ARRTIBUTE_NAME,propertyName);
        if(dataType!=null)
        {
            element.setAttribute(XMLConstant.TAG_ARRTIBUTE_DATATYPE,dataType.getName());
        }else
        {
            throw new XMLException("parse token has no data type information! ");
        }
        
        if(getValue()==null)
            element.setAttribute(XMLConstant.TAG_ARRTIBUTE_ISNULL,"true");
        
        return element;
    }
    public String getElementName() {
        return elementName;
    }
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
