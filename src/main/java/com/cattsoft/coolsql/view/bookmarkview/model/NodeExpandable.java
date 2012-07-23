/*
 * �������� 2006-9-8
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.sql.SQLException;

import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author liu_xlin
 *���ڵ�չ����ʹ�õĽӿ�
 */
public interface NodeExpandable {
    /**
     * ���ڵ㱻չ��ʱ�����ô˷��������ӽڵ�ĳ�ʼ��
     * @param parent ITreeNode
     * @param filter --treenode filter
     * @throws UnifyException
     */
   public abstract void expand(DefaultTreeNode parent,INodeFilter filter) throws SQLException, UnifyException;
   
   /**
    * ˢ��ѡ�нڵ�,���»�ȡ�ڵ��µ������ӽڵ�
    * @param parent
    * @param filter --treenode filter
    * @throws SQLException
    * @throws UnifyException
    */
   public abstract void refresh(DefaultTreeNode parent,INodeFilter filter) throws SQLException,UnifyException;
}
