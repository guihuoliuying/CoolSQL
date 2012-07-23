/*
 * �������� 2006-11-1
 */
package com.cattsoft.coolsql.exportdata.html;

/**
 * @author liu_xlin
 *���������html���е���ʱ����������ļ������Ͳ���ȷ���߳����������Χʱ���׳����쳣
 */
public class HtmlExportException extends Exception {
   public HtmlExportException()
   {
       super();
   }
   public HtmlExportException(String message)
   {
       super(message);
   }
   public HtmlExportException(Throwable cause)
   {
       super(cause);
   }
   public HtmlExportException(String message,Throwable cause)
   {
       super(message,cause);
   }
}
