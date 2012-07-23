/*
 * �������� 2006-10-15
 */
package com.cattsoft.coolsql.pub.display;

import java.util.Vector;

/**
 * @author liu_xlin
 * �����ݵ�ת��
 */
public class DataTran {
    /**
     * ����ά����ת��Ϊ��������
     * @param anArray
     * @return
     */
    public static Vector<Object> convertToVector(Object[][] anArray) {
        if (anArray == null) {
            return null;
	}
        Vector<Object> v = new Vector<Object>(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.addElement(convertToVector(anArray[i]));
        }
        return v;
    }
    /**
     * ��һά����ת��Ϊ��������
     * @param anArray
     * @return
     */
    protected static Vector<Object> convertToVector(Object[] anArray) {
        if (anArray == null) { 
            return null;
	}
        Vector<Object> v = new Vector<Object>(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.addElement(anArray[i]);
        }
        return v;
    }
}
