/*
 * Created on 2007-5-15
 */
package com.cattsoft.coolsql.system.menubuild;

/**
 * @author liu_xlin
 *�����˲˵������ļ��е�Ԫ�����
 */
public class MenuXMLConstants {
	public static final String MENU_CLIENTPROPERTY_NAME="name"; 
	
	/**
     * root���
     */
    public static final String TAG_MENUS="menus";
    
    /**
     * resource tag ,locationing resource file in which menubuilder find all resources
     */
    public static final String TAG_RESOURCE="resource";
    /**
     * �˵����
     */
    public static final String TAG_MENU="menu";
    
    /**
     * �˵�����
     */
    public static final String TAG_MENUITEM="menuitem";
    
    /**
     * �˵������ʾ�����
     */
    public static final String TAG_TOOLTIP="tooltip";
    
    /**
     * �˵����¼�������
     */
    public static final String TAG_ACTIONLISTENER="action";
    /**
     * �˵����ݼ���
     */
    public static final String TAG_SHORTCUT="shortcut";
    /**
     * У��˵���Ŀ�����
     */
    public static final String TAG_ENABLECHECK="enablecheck";
    
    /**
     * �˵��ķָ���
     */
    public static final String TAG_SEPARATOR="separator";
    
    /**
     * �˵�/�˵����ͼ��Ԫ��
     */
    public static final String TAG_ICON="icon";
    
    /**
     * �˵����˵��ı�ǩ����
     */
    public static final String ATTRIBUTE_LABEL="label";
    
    public static final String ATTRIBUTE_MNEMONIC="mnemonic";
    /**
     * �˵����λ�����ԣ����ڲ˵�/�˵����λ������
     */
    public static final String ATTRIBUTE_LOCATION="location";
    /**
     * Indicate whether register CsAction to ShortcutManager.
     */
//    public static final String ATTRIBUTE_ISREGISTER="isregister";
    /**
     * �˵�/�˵����ʼ�Ƿ����
     */
    public static final String ATTRIBUTE_DISABLED="disabled";
    
    /**
     * �˵�/�˵����ͼ��
     */
    public static final String ATTRIBUTE_ICON="icon";
    public static final String ATTRIBUTE_ICON_TYPE="type"; //iconԪ�ص����ԣ�resource(��Դ�󶨵��ļ���Դ),file(�ӱ����ļ��л�ȡ����Դ)
    /**
     * �˵����˵������������JComponent.putClientProperty(),��Ӧ������Ϊ"name"
     */
    public static final String ATTRIBUTE_CLIENTPROPERTY="clientproperty";
    
    /**
     * �˵�������
     * 1����ͨ�˵���(JMenuItem) :plain
     * 2����ѡ�˵���(JRadioButtonMenuItem)  :radio
     * 3����ѡ�˵���(JCheckBoxMenuItem)   :check
     */
    public static final String ATTRIBUTE_TYPE="type";
    
    /**
     * �˵�����ʱ�������ü��������ü���������ʵ��ActionListener�ӿڣ������Կ�����menu��menuitem�������͵�Ԫ��
     */
    public static final String ATTRIBUTE_LOADLISTENER="loadlistener";
    
    /**
     * ����icon���Ժ�iconԪ�ص�����ֵ�ж�
     */
    public static final String BLANK="blank";
}
