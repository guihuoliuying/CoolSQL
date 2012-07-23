/*
 * �������� 2006-9-10
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

/**
 * @author liu_xlin
 *�ڵ�����ݶ���ӿ�
 */
public interface ObjectHolder {
   /**
    * ����ʵ��ڵ��а��˾����ʵ�����,�÷������ڻ�ȡ���ʵ����ݶ���
    * @return
    */
   public abstract Object getDataObject();
   
   /**
    * check whether this object is selected or not
    * @return true if selected, otherwise false;
    */
   public boolean isSelected();
   
}
