/*
 * �������� 2006-6-3
 *
 * TODO Ҫ��Ĵ���ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.cattsoft.coolsql.pub.component;

import javax.swing.JFileChooser;

/**
 * @author liu_xlin
 *�ļ�ѡ��Ի���������һ��������ǰ�ļ�·���ı���
 */
public class FileSelectDialog extends JFileChooser {
    public static String oldSelectPath="";
	/**
	 * @param oldSelectPath Ҫ���õ� oldSelectPath��
	 */
	public static void setOldSelectPath(String oldSelectPath) {
		FileSelectDialog.oldSelectPath = oldSelectPath;
	}
}
