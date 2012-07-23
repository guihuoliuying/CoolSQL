/*
 * �������� 2006-7-7
 *
 */
package com.cattsoft.coolsql.view.log;

/**
 * @author liu_xlin
 *��־���ӿ�
 */
public interface BaseLog {
  /**
   * ������Ϣ��ӡ�����ڲ�ѯ����ִ��sql���ʱ��ӡ����Ϣ
   */
  public abstract void debug(Object ob);
  
  /**
   * print warning information on the log area
   */
  public void warning(Object ob);
  /**
   * ������Ϣ��ӡ
   */
  public abstract void error(Object ob);
  /**
   * һ����Ϣ��ӡ
   */
  public abstract void info(Object ob);
}
