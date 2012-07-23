/**
 * 
 */
package com.cattsoft.coolsql.system;

import java.io.File;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-1-25 create
 */
public class SystemConstant {

	public final static String separator=File.separator; //Ŀ¼�ָ���
	public final static String userPath=System.getProperty("user.home")+separator+".coolsql"+separator;
	@SuppressWarnings("unchecked")
	public final static String lineSeparator=(String) java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
	/**
	 * ������Ϣ������ļ�
	 */
	//��ǩ��Ϣ
	public static String bookmarkInfo =userPath+ "bookmarkinfo.xml";
	//���ִ��sql��Ϣ
	public static String recentSqlPATH =userPath+ "recentsql"+separator;
	public static String recentSqlInfo =recentSqlPATH+"recentSql";
	
	//����Ϣ
	public static String driversInfo = userPath+"driver.classpath";
	
	public static String extraFiles = userPath+"Extra.classpath";
	//sql�༭��ͼ�ı༭����
	public static String sqlEditeInfo = userPath+"sqlEdite.txt";
	//�ղص�sql����ŵ��ļ�·��
	public static String favoriteSQLFilePath=userPath+"favoriteSQL.dat";

	/**
	 * plugin folder that place files related to plugin
	 */
	public static String PLUGIN_FOLDER=userPath+separator;
	
	public static String SETTING_FILE=userPath+"System.setting";
	
	public static String LOGVIEW_LOGFILE=userPath+"system.log";
	
	public static String LAUNCH_INI=userPath+"launch.ini";
}
