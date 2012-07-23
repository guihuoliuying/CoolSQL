/**
 * 
 */
package com.cattsoft.coolsql.system;

import java.io.File;

import com.cattsoft.coolsql.pub.parse.xml.XMLBeanUtil;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;

/**
 * ����������Ϣ�Ĺ����࣬���ڶ�ȡϵͳ��������Ϣ��
 * @author kenny liu
 *
 * 2007-10-30 create
 */
public class PropertyManage {

	private static SystemProperties sp=null;  //ϵͳ����
	
	public final static String FILE_SYSTEMPROPERTY=SystemConstant.userPath+"SystemProperty.xml";
	
	private static LaunchInitProperty launchPros;
	private PropertyManage()
	{
		
	}
	/**
	 * ����ϵͳ������Ϣ
	 * @throws XMLException ������ϵͳ���������ļ����󣬽����׳����쳣����
	 */
	public static void loadSystemProperty() throws XMLException
	{
		if(sp==null)
		{
			XMLBeanUtil xml = new XMLBeanUtil();
			Object ob=xml.importBeanFromXML(FILE_SYSTEMPROPERTY);
			if(ob==null)  //�ļ�������
			{
				sp= new SystemProperties();
				return;
			}
			if(!(ob instanceof SystemProperties))
				throw new XMLException("ϵͳ�����ļ����ô��󣬴���ԭ�����������ʹ���,�������ͣ�"+ob.getClass().getName());
			sp=(SystemProperties)ob;
		}
	}
	/**
	 * ��ȡϵͳ������Ϣ��
	 * @return ϵͳ���Զ���
	 * 
	 */
	public static SystemProperties getSystemProperty() 
	{
		return sp;
	}
	/**
	 * ����ϵͳ������Ϣ
	 * @throws XMLException ����ϵͳ���Զ�����?���׳����쳣����
	 */
	public static void saveSystemProperty() throws XMLException
	{
		if(sp==null)
			return;
		XMLBeanUtil xml = new XMLBeanUtil();
		xml.exportBeanToXML(new File(FILE_SYSTEMPROPERTY), sp, "systemproperty");
	}
	public static LaunchInitProperty getLaunchSetting()
	{
		if(launchPros==null)
		{
			launchPros=new LaunchInitProperty();
		}
		return launchPros;
	}
	public static void saveLaunchSetting()
	{
		getLaunchSetting().saveToFile();
	}
}
