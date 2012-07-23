/*
 * �������� 2006-11-7
 */
package com.cattsoft.coolsql.pub.parse.xml;

import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.xml.token.ArrayToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.BooleanToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.CharacterToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.CollectionXMLToken;
import com.cattsoft.coolsql.pub.parse.xml.token.DoubleToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.FloatToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.IntegerToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.LongToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.MapXMLToken;
import com.cattsoft.coolsql.pub.parse.xml.token.ObjectToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.ShortToXML;
import com.cattsoft.coolsql.pub.parse.xml.token.StringToXML;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * The factory for constructing XML.
 * @author liu_xlin
 */
public class XMLFactory {
    public static BeanAndXMLParse getBeanToXMLParser(String javaClass)
    {
        Class<?> type = null;
        try {
            type = LoadJar.getInstance().getClassLoader().loadClass(javaClass);
        } catch (ClassNotFoundException e) {
            LogProxy.internalError(e);
        }
        return getBeanToXMLParser(type);
    }
    /**
     * Retrieve the object parser.
     * @param javaClass A qualified class name
     * @param editorType  --true:bean to XML ,false:xml to bean
     */
    public static BeanAndXMLParse getBeanToXMLParser(String javaClass, boolean editorType)
    {
        Class<?> type = null;
        try {
            type = LoadJar.getInstance().getClassLoader().loadClass(javaClass);
        } catch (ClassNotFoundException e) {
            LogProxy.internalError(e);
        }
        return getBeanToXMLParser(type,editorType);
    }
    /**
     * ��ȡ���������
     * @param javaClass
     * @param editorType  --true:bean to xml ,false:xml to bean
     * @return
     */
    public static BeanAndXMLParse getBeanToXMLParser(Class<?> javaClass,boolean editorType)
    {
        BeanAndXMLParse tmp=getBeanToXMLParser(javaClass);
        if(tmp!=null)
            tmp.setEditorType(editorType);
        
        return tmp;
    }
    /**
     * ��ȡ���������
     */
    private static BeanAndXMLParse getBeanToXMLParser(Class<?> javaClass)
    {
        
        if(javaClass.isArray())   //����
        {
            return new ArrayToXML(javaClass);
        }else if(javaClass==Boolean.TYPE)  //ԭʼ�����ж� ------start----
        {
            return new BooleanToXML();
        }else if(javaClass==Integer.TYPE)
        {
            return new IntegerToXML();
        }else if(javaClass==Short.TYPE)
        {
            return new ShortToXML();
        }else if(javaClass==Long.TYPE)
        {
            return new LongToXML();
        }else if(javaClass==Float.TYPE)
        {
            return new FloatToXML();
        }else if(javaClass==Double.TYPE)
        {
            return new DoubleToXML();
        }else if(javaClass==Character.TYPE)  //ԭʼ�����ж�  -------end-----
        {
            return new CharacterToXML();
        }else if(javaClass==java.lang.String.class)
        {
            return new StringToXML();
        }else if(java.util.Map.class.isAssignableFrom(javaClass))   //map����
        {
            return new MapXMLToken(javaClass);
        }
        else if(java.util.Collection.class.isAssignableFrom(javaClass))  //��������
        {
           return new CollectionXMLToken(javaClass);   
        }else                           //�����������
        {
           return new ObjectToXML(javaClass);
        }
    }
    /**
     * �Ƿ��ǽ�������
     * @param type --class����
     * @return  --����Ƿ���true���񷵻�falses
     */
    public static boolean isParseFactor(Class<?> type)
    {
        if(getBeanToXMLParser(type) instanceof ObjectToXML)
        {
            return false;
        }else
            return true;
    }
}
