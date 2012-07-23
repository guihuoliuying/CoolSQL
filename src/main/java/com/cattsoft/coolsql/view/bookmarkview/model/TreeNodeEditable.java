/*
 * �������� 2006-8-18
 *
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import java.sql.SQLException;

import com.cattsoft.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin
 *�ڵ���ݵĲ����ӿ�
 *��Ĺ����У��ڵ�����Բ鿴  
 */
public interface TreeNodeEditable {
    public abstract void property() throws SQLException,UnifyException;
    public abstract void copy();
}
