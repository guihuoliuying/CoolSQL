/*
 * Created on 2007-1-16
 */
package com.cattsoft.coolsql.modifydatabase;

import javax.swing.JComponent;

import com.cattsoft.coolsql.modifydatabase.model.SQLDateSelector;
import com.cattsoft.coolsql.modifydatabase.model.SQLNumberEditor;
import com.cattsoft.coolsql.modifydatabase.model.SQLStringEditor;
import com.cattsoft.coolsql.sql.util.TypesHelper;

/**
 * @author liu_xlin
 *�༭��������࣬�����������ݿ���²�������Ҫ�ı༭�������
 */
public class EditorFactory {

    /**
     * �ı����ͱ༭���
     */
    public static final String EDITORTYPE_TEXT="text";
    
    /**
     * ��������
     */
    public static final String EDITORTYPE_NUMBER="number";
    
    /**
     * ��������
     */
    public static final String EDITORTYPE_DATE="date";
    /**
     * ʱ������
     */
    public static final String EDITORTYPE_TIME="time";
    
    /**
     * ʱ�������
     */
    public static final String EDITORTYPE_TIMESTAMP="timestamp";
    
    /**
     * �ֽڣ������ƣ�����
     */
    public static final String EDITORTYPE_BIT="bit";
    
    /**
     * ��ȡ�༭�������
     * @param editorType  --�༭�������
     * @return --����������,��ȡ��Ӧ���������
     */
    public static JComponent getEditorComponent(String editorType,int size,int digits)
    {
        if(editorType==null)
            return new SQLStringEditor(size);
        if(editorType.equals(EDITORTYPE_TEXT))
        {
            return new SQLStringEditor(size);
        }else if(editorType.equals(EDITORTYPE_NUMBER))
        {
            return new SQLNumberEditor(size,digits);
        }else if(editorType.equals(EDITORTYPE_DATE))
//                ||editorType.equals(EDITORTYPE_TIME)
//                ||editorType.equals(EDITORTYPE_TIMESTAMP))
        {
            return new SQLDateSelector(editorType);
        }else
            return new SQLStringEditor(size);
    }
    /**
     * ͨ��sql��������ȡ��Ӧ�ı༭�������
     * @param sqlType  --sql����
     * @return  --�༭�������
     */
    public static String getEditorTypeBySQLType(int sqlType)
    {
        if(TypesHelper.isNumberic(sqlType))
        {
            return EDITORTYPE_NUMBER;
        }else if(TypesHelper.isText(sqlType))
            return EDITORTYPE_TEXT;
        else if(sqlType==TypesHelper.DATE)
            return EDITORTYPE_DATE;
        else if(sqlType==TypesHelper.TIME)
            return EDITORTYPE_TIME;
        else if(sqlType==TypesHelper.TIMESTAMP)
            return EDITORTYPE_TIMESTAMP;
        else if(sqlType==TypesHelper.BLOB)
            return EDITORTYPE_BIT;
        else
            return EDITORTYPE_TEXT;
            
    }
}
