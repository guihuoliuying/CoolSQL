package com.cattsoft.coolsql.exportdata.excel;

/**
 *�����ж������쳣��UserException�еĴ�����Ϣ�ʹ�������
 * @author CMIS��Ŀ��
 */
public class ExceptionInfoSet {
	
	/**
	 * ��ݿ�����쳣�࣬�漰����ݿ�������ֵ��쳣��
	 */
	public static String DB_Category = "DB";
	
	/**
	 * Ӧ�ò����쳣�࣬������㡢Ȩ�޲������Լ�ͨ���ַ��캯�����ͨ���쳣����Ϊ�����͡�
	 */
	public static String App_Category = "App";
	
	/**
	 * �����ڼ䷢���ϵͳ�쳣�࣬һ���漰��Ӧ�÷�������ص��쳣��������Ϊϵͳ�쳣���͡�
	 */
	public static String System_Category = "System";
	
	/**
	 * ��ݿⲻ����ʱ�Ĵ�����Ϣ��errorCode=-1013
	 */
	public static String DB_NotFound = "����ݿⲻ���ڣ�";
	
	/**
	 * �û�������ʱ�Ĵ�����Ϣ��errorCode��-30082
	 */
	public static String DB_InvalidUser =  "������û�����������";
	
	/**
	 * ��ݿ������Ѿ��رյ��쳣��Ϣ,errorCode = -99999
	 */
	public static String DB_ConnectionClosed = "��ݿ������ѹرգ�";
	
	/**
	 * ִ�в�������ݿ��в����ڸñ�,errorCode=-204
	 */
	public static String DB_TableNotExist = "ִ�в�������ݱ?���ڣ�";
	
	/**
	 * ����ݿ��в���һ���Ѿ����ڵļ�¼ʱ���쳣��Ϣ,errorCode=-803
	 */
	public static String DB_RecordExits = "�ü�¼�Ѿ����ڣ�";
	
	/**
	 * ����ݿ��в����¼���������쳣��Ϣ
	 */
	public static String DB_InsertFailed = "����ݿ��в����¼����";
	
	/**
	 * ִ����ݿ��ѯʱ���������쳣��Ϣ
	 */
	public static String DB_QueryFailed = "��ݿ��ѯ����";
	
	/**
	 * ����ݿ���ɾ���¼ʱ�������ʱ���쳣��Ϣ
	 */
	public static String DB_DeleteFailed = "��ݿ�ɾ���¼����";
	
	/**
	 * ȱʡ����ݿ������Ϣ��
	 */
	public static String DB_Default_Error = "ִ����ݿ��������!";

}