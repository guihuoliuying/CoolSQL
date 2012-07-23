/*
 * �������� 2006-11-6
 */
package com.cattsoft.coolsql.pub.parse.xml;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.xml.token.ObjectToXML;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��java bean����ΪxmlԪ��
 */
public class XMLBeanUtil {

	private String encoding;
	private ClassLoader loader;
    public XMLBeanUtil() {
    	this(null,XMLBeanUtil.class.getClassLoader());
    }
    public XMLBeanUtil(String encoding)
    {
    	this(encoding,XMLBeanUtil.class.getClassLoader());
    }
    public XMLBeanUtil(ClassLoader loader)
    {
    	this(null,loader);
    }
    public XMLBeanUtil(String encoding,ClassLoader loader) {
    	if(encoding==null)
    		this.encoding=System.getProperty("file.encoding");
    	else
    		this.encoding=encoding;
    	
    	if(loader==null)
    		this.loader=XMLBeanUtil.class.getClassLoader();
    	else
    		this.loader=loader;
    }
    /**
     * ��ȡjavabean�����Լ���
     * 
     * @param bean
     * @return
     */
    private PropertyDescriptor[] getBeanPropertys(Object bean) {
        BeanInfo info = null;
        try {
            if (bean != null)
                info = Introspector.getBeanInfo(bean.getClass(),
                        java.lang.Object.class);
        } catch (IntrospectionException ex) {
            LogProxy.internalError(ex);
        }
        if (info != null)
            return info.getPropertyDescriptors();
        else
            return null;
    }

    public Element createRootElement(String name) {
        if (StringUtil.trim(name).equals(""))
            name = "bean";
        return new Element(name);
    }

    /**
     * ����javabeanΪxml��Ӧ���ĵ��ṹ
     * 
     * @param bean
     * @param name  --��ǩ���
     * @return
     * @throws XMLException
     */
    public Element parseBean(Object bean, String name) throws XMLException {
        /**
         * ���ȼ���Ƿ�Ϊֱ�ӿɽ�������
         */
        Element root =checkRootBean(bean);
        if(root!=null)
            return root;
        
        /**
         * ���Ϊ��ֱ�ӽ������󣬶�bean�����Խ��н���
         */
        root=createRootElement(name);

        if (bean == null) {
//            root.addContent(XMLConstant.NULL);
            root.setAttribute(XMLConstant.TAG_ARRTIBUTE_ISNULL,"true");
            return root;
        }

        root.setAttribute(XMLConstant.TAG_ARRTIBUTE_DATATYPE, bean.getClass()
                .getName());
        Vector factors = beanToXMLParser(bean);

        Iterator it = factors.iterator();
        while (it.hasNext()) {
            BeanAndXMLParse parser = (BeanAndXMLParse) it.next();

            Element element = parser.getXMLElement();
            if (element != null)
                root.addContent(element);
        }

        return root;
    }
    /**
     * ��������Ƿ��ǽ���Ԫ�ض��󣬸÷����������Ƿ����ֱ�ӽ��н�����
     * @param bean
     * @return  --���bean�ǽ���Ԫ�ض��󣬷�����Ӧ���ĵ�Ԫ�ض��󣬷��򷵻�nullֵ
     * @throws XMLException
     */
    private Element checkRootBean(Object bean) throws XMLException
    {
        if(bean==null)
            return null;
        BeanAndXMLParse parse=checkIsParseFactor(bean);
        if(parse!=null)
        {
            return parse.getXMLElement();
        }
        return null;
    }
    /**
     * ��java����ת��Ϊ��Ӧ��xml�ĵ�����
     * 
     * @param bean
     * @param name
     * @return
     * @throws XMLException
     */
    public Document beanToXMLDoc(Object bean, String name) throws XMLException {
        Document doc = new Document(parseBean(bean, name));
        return doc;
    }

    /**
     * ��ȡjavabean�����ԣ�Ȼ�����ʼ��ΪXMLԪ�ؽ�������
     * 
     * @param bean
     * @return
     */
    private Vector beanToXMLParser(Object bean) {
        Vector factors=null;
        PropertyDescriptor[] pros = getBeanPropertys(bean);
        if (pros != null) {
            factors = new Vector();

            for (int i = 0; i < pros.length; i++) {
                String propName = pros[i].getName();
                Method getter = pros[i].getReadMethod();
                Method setter=pros[i].getWriteMethod();
                if(getter==null||setter==null)  //���û��get�����������б���
                {
                    continue;
                }
                Class returnType = getter.getReturnType();

                BeanAndXMLParse parse = XMLFactory.getBeanToXMLParser(
                        returnType, true);
                if (parse == null) //����Ҳ�����Ӧ�Ľ������ͣ������κδ��?ֱ�ӽ�����һ���ԵĽ���
                    continue;

                parse.setPropertyDes(pros[i]);
                parse.setPropertyName(propName);
                parse.setBean(bean);
                try {
                    parse.setPropertyValue(getter.invoke(bean, null));
                    if (parse.getValue() == bean) //��ֹ��ѭ��
                        continue;
                } catch (Exception e) {
                    parse.setPropertyValue(null);
                }

                factors.add(parse);
            }

            return factors;
        } else {
            return new Vector();
        }
    }
    /**
     * У�����������Ƿ�Ϊ�Ƕ���������ӣ�Ȼ�󷵻ؽ�������
     * @param bean
     * @return  --BeanAndXMLParse����
     */
    private BeanAndXMLParse checkIsParseFactor(Object bean)
    {
        BeanAndXMLParse parse = XMLFactory.getBeanToXMLParser(
                bean.getClass(), true);
        if(parse instanceof ObjectToXML)
        {
            return null;
        }else
        {
            parse.setPropertyDes(null);
            parse.setPropertyName(null);
            parse.setBean(null);
            parse.setPropertyValue(bean);
            return parse;
        }
    }
    /**
     * ��java bean����Ϊxml�ĵ���ʽ
     * 
     * @param file
     * @param bean
     * @param beanName
     * @throws XMLException
     */
    public void exportBeanToXML(File file, Object bean, String beanName)
            throws XMLException {
        if (file == null || bean == null)
            return;
        Document doc = null;
        doc = beanToXMLDoc(bean, beanName);
        saveDocumentToFile(doc,file);
    }
    public void saveDocumentToFile(Document doc,File file) throws XMLException
    {
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(file.getAbsolutePath(),false, false);
            out = new FileOutputStream(file);
            Format userFormat = Format.getPrettyFormat();
            userFormat.setEncoding(encoding);
            XMLOutputter xmlOut = new XMLOutputter(userFormat);
            xmlOut.output(doc, out);
        } catch (FileNotFoundException e) {
            throw new XMLException(e);
        } catch (IOException e) {
            throw new XMLException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
    /**
     * ���ļ��н������ĵ�����ģ��
     * 
     * @param file
     * @return
     * @throws XMLException
     * @throws IOException
     * @throws MalformedURLException
     * @throws JDOMException
     */
    public Document importDocumentFromXML(File file) throws MalformedURLException, XMLException, IOException {
        if (file == null)
            return null;
        if (!file.exists())
            return null;

        return importDocumentFromXML(file.toURL().openStream());

    }
    public Document importDocumentFromXML(InputStream input) throws XMLException {
        if (input == null)
            return null;

        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            doc = builder.build(input);
        } catch (JDOMException e) {
            throw new XMLException(e);
        } catch (IOException e) {
        	throw new XMLException(e);
		}
        return doc;

    }

    public  Class<?> getClass(String className) throws XMLException {
        try {
            return LoadJar.getInstance().getClassByName(className);
        } catch (ClassNotFoundException e) {
            throw new XMLException("can't find class:" + className + ", ClassLoader:" + loader.getClass());
        }
    }

    /**
     * ��ݸ��xmlԪ�ض��󣬻�ȡ��Ӧ��java bean����
     * 
     * @param root
     * @return
     * @throws XMLException
     * @throws XMLException
     */
    public Object getBean(Element root) throws XMLException{
        return getBean(root,null);
    }
    /**
     * ��ݸ��xmlԪ�ض��󣬻�ȡ��Ӧ��java bean����,�÷���ָ�����Ѿ���ʵ��Ķ��󣬸÷����Ĵ����̸������ø����bean��������ֵ��
     * �÷��������ڹ��췽�����η�Ϊ��public������
     * @param root  --�ĵ�Ԫ��
     * @param bean  --Object ���󣬸ö����Ѿ�����Ҫ���صĶ�������˳�����ʵ��
     * @return
     * @throws XMLException
     */
    protected Object getBean(Element root,Object bean) throws XMLException{
        BeanInfo info = null;
        try {
            if (bean == null) {
                /**
                 * ����Ƿ�Ϊ�Ƕ������Ԫ��
                 */
                bean = checkRootElement(root);
                if (bean != null)
                    return bean;

                /**
                 * ���root��Ӧ�Ľ�������Ϊ����
                 */
                String dataType = StringUtil.trim(root.getAttribute(
                        XMLConstant.TAG_ARRTIBUTE_DATATYPE).getValue());
                Class beanClass = getClass(dataType);
                
                /**
                 * ���Ϊ�ӿڣ���������
                 */
                if(beanClass.isInterface())
                    return null;
                
                bean = beanClass.newInstance();  
            }

            info = Introspector.getBeanInfo(bean.getClass(),
                    Introspector.USE_ALL_BEANINFO);
        }catch (InstantiationException e) {
            throw new XMLException(PublicResource.getSQLString("system.xml.instantiateerror")+e.getMessage(),e);
        }catch (Exception e) {
            throw new XMLException(PublicResource
                    .getSQLString("system.xml.loaderror")
                    + e.getMessage(), e);
        } 
        
        /**
         * ��������������map�����У����ں�������Ĳ���
         */
        PropertyDescriptor propDesc[] = info.getPropertyDescriptors();
        Map props = new HashMap();
        for (int i = 0; i < propDesc.length; i++)
            props.put(propDesc[i].getName(), propDesc[i]);

        //��ȡԪ�ؽ�����
        Vector beanParsers = xmlToBeanParser(root, bean);

        Iterator it = beanParsers.iterator();
        while (it.hasNext()) {
            BeanAndXMLParse parser = (BeanAndXMLParse) it.next();
            PropertyDescriptor curProp = (PropertyDescriptor) props.get(parser
                    .getName());
            Object tmpValue = parser.isNull()?null:parser.getObjectInXML();  //���xml�ж���ñ���Ϊnull,��ֱ�ӽ�����ֵ��Ϊnull
            evaluateProperty(bean, curProp, tmpValue);
        }

        return bean;
    }
    /**
     * �������ĵ�Ԫ�ض����Ƿ��ǷǶ������ӡ�����ǷǶ���Ԫ�أ�ֱ�ӽ�������������Ӧ��ֵ������ֱ�ӷ���nullֵ
     * @param root
     * @return  --����ǷǶ���Ԫ�أ����ҽ�����̲����?���طǿն�������Ƕ���Ԫ�أ�����nullֵ
     * @throws XMLException
     */
    private Object checkRootElement(Element root) throws XMLException
    {
        String dataType = StringUtil.trim(root.getAttribute(
                XMLConstant.TAG_ARRTIBUTE_DATATYPE).getValue());
        Class beanClass = getClass(dataType);

        BeanAndXMLParse parser=XMLFactory.getBeanToXMLParser(beanClass,false);
        if(parser instanceof ObjectToXML)
        {
            return null;
        }else
        {
            parser.setElement(root);
            
            return parser.getObjectInXML();
            
        }
    }
    /**
     * �����Խ��и�ֵ
     * 
     * @param bean
     *            --����ֵ�Ķ���
     * @param propDescr
     *            --��������
     * @param value --
     *            ֵ
     * @throws XMLException
     */
    public static void evaluateProperty(Object bean,
            PropertyDescriptor propDescr, Object value) throws XMLException {
        Method setter = propDescr.getWriteMethod();
        try {
            setter.invoke(bean, new Object[] { value });
        } catch (Exception e) {
            throw new XMLException(e);
        }
    }

    /**
     * ��xml�ĵ�����Ϊbean�Ľ�����
     * 
     * @param doc
     * @return
     * @throws XMLException
     */
    private Vector xmlToBeanParser(Element root, Object bean)
            throws XMLException {
        Vector v = new Vector();

        /**
         * ��������������map�����У����ں�������Ĳ���
         */
        PropertyDescriptor propDesc[] = this.getBeanPropertys(bean);
        Map props = new HashMap();
        for (int i = 0; i < propDesc.length; i++)
            props.put(propDesc[i].getName(), propDesc[i]);

        Iterator it = root.getChildren().iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String name = e.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME); //������
            String dataType = StringUtil.trim(e.getAttribute(
                    XMLConstant.TAG_ARRTIBUTE_DATATYPE).getValue());//�������

            PropertyDescriptor pd = (PropertyDescriptor) props.get(name);
            if (pd == null)//����ܹ��ҵ���Ӧ�����ԣ���ô���д��?���������һ�ν���
                continue;

            Method setter = pd.getWriteMethod();
            if(setter==null)  //���ڷ���isProperty���͵ķ�����û�ж�Ӧ�����÷�������ôsetterΪnull
                continue;
            
            if(!name.equals(pd.getName()))  //���������ƥ��
            {
                throw new XMLException("property name("+name+") in xml don't match with name("+pd.getName()+") in java class");
            }
            
            Class paramType =setter.getParameterTypes()[0];

            Class type = null;
            if (paramType.isPrimitive()) {  
                if (!paramType.getName().equals(dataType))//���������Ͳ�ƥ��
                    throw new XMLException("datatype(" + dataType
                            + ") in xml don't matche with type("
                            + paramType.getName() + ") in java class!");
                type = paramType;
            } else {
                type = getClass(dataType); //װ����
            }

            BeanAndXMLParse parse = XMLFactory.getBeanToXMLParser(type, false); //��ȡ�����͵Ľ�����
            parse.setElement(e);
            parse.setPropertyName(name);
            
            String isNull = e.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME); //�Ƿ�Ϊnull����ֵ
            if(isNull!=null&&isNull.trim().equals("true"))  //�����ʾ��ָ������Ϊnull,�Ž��ñ���ֵ��Ϊnull
                parse.setNull(true);
            else //�����������Ϊ��null
                parse.setNull(false);
            
            parse.setPropertyDes(pd);
            v.add(parse);
        }
        return v;
    }

    public Object importBeanFromXML(File file) throws MalformedURLException, XMLException, IOException {
        Document doc = importDocumentFromXML(file);
        if (doc != null) {
            return getBean(doc.getRootElement());
        } else
            return null;
    }
    public Object importBeanFromXML(String path) throws XMLException
    {
        Document doc = importDocumentFromXML(getInputStream(path));
        if (doc != null) {
            return getBean(doc.getRootElement());
        } else
            return null;
    }
    public Object importBeanFromXML(InputStream inputStream) throws XMLException
    {
        Document doc = importDocumentFromXML(inputStream);
        if (doc != null) {
            return getBean(doc.getRootElement());
        } else
            return null;
    }
    public Object importBeanFromXML(File file,Object bean) throws MalformedURLException, XMLException, IOException {
        Document doc = importDocumentFromXML(file);
        if (doc != null) {
            return getBean(doc.getRootElement(),bean);
        } else
            return null;
    }
    public Object importBeanFromXML(String path,Object bean) throws XMLException
    {
        Document doc = importDocumentFromXML(getInputStream(path));
        if (doc != null) {
            return getBean(doc.getRootElement(),bean);
        } else
            return null;
    }
    public Object importBeanFromXML(InputStream inputStream,Object bean) throws XMLException
    {
        Document doc = importDocumentFromXML(inputStream);
        if (doc != null) {
            return getBean(doc.getRootElement(),bean);
        } else
            return null;
    }
    /**
     * get inputStream with a given path
     * @param path --path of resource file
     * @return 
     * @throws  
     * @throws XMLException 
     */
    public static InputStream getInputStream(String path) throws XMLException
    {
    	File file=new File(path);
        if(StringUtil.trim(path).equals(""))
        {
            return null;
        }else
        {
        	if(file.exists())
        	{
    			try {
    				return file.toURL().openStream();
    			}catch (Exception e) {
    				throw new XMLException("��ȡ�ļ�����"+e.toString());
    			}
        	}
            return XMLBeanUtil.class.getResourceAsStream(path);
        }
    }
//    public static void main1(String args[]) {
//        XMLBeanUtil xml = new XMLBeanUtil();
//        Bookmark bookmark = new Bookmark();
//        System.out.println(bookmark.getType());
//        Document doc = null;
//        try {
//            doc = xml.beanToXMLDoc(bookmark, "bookmark");
//        } catch (XMLException e) {
//            e.printStackTrace();
//            return;
//        }
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream("test.xml");
//            XMLOutputter xmlOut = new XMLOutputter("", true);
//            xmlOut.setEncoding("GB2312");
//            xmlOut.output(doc, out);
//        } catch (FileNotFoundException e) {
//            // TODO �Զ���� catch ��
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO �Զ���� catch ��
//            e.printStackTrace();
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (Exception e) {
//                }
//            }
//        }
//    }

//    public static void main(String args[]) {
//        XMLBeanUtil xml = new XMLBeanUtil();
//        Bookmark bookmark = new Bookmark();
//        Document doc = null;
//        try {
//            bookmark = (Bookmark) xml.importBeanFromXML(new File("test.xml"));
//        } catch (XMLException e) {
//            e.printStackTrace();
//            return;
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        PropertyDescriptor propDesc[] = xml.getBeanPropertys(bookmark);
//        for (int i = 0; i < propDesc.length; i++) {
//            Method getter = propDesc[i].getReadMethod();
//            try {
//                System.out.println(propDesc[i].getName()+":"+getter.invoke(bookmark, null));
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println(propDesc[i].getName() + ": exception occur");
//            }
//        }
//    }
}
