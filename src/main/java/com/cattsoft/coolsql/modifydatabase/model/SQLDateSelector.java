/*
 * Created on 2007-1-17
 */
package com.cattsoft.coolsql.modifydatabase.model;

import java.text.ParseException;
import java.util.Date;

import com.cattsoft.coolsql.modifydatabase.DataHolder;
import com.cattsoft.coolsql.modifydatabase.EditorFactory;
import com.cattsoft.coolsql.pub.component.DateSelector;

/**
 * @author liu_xlin
 *����ѡ������ļ̳У�������Ϊ�༭���ʱ����ȡ��ǰ�����ѡ�е�����ֵ
 */
public class SQLDateSelector extends DateSelector implements DataHolder {

    private String addtionalPart;
    public SQLDateSelector(String editorType)
    {
        super(STYLE_DATE1);
        setStyleByType(editorType);
    }
    /**
     * ��������ֵ��1900-01-01������ĸ�ʽ
     * @param editorType --����ʱ�䣬���ڣ���ʱ����������ͷ��ز�ͬ��ʱ���ʽ
     */
    private void setStyleByType(String editorType)
    {
        if(editorType==null||editorType.equals(EditorFactory.EDITORTYPE_DATE))
             addtionalPart="";
        else if(editorType.equals(EditorFactory.EDITORTYPE_TIME))
        {
            addtionalPart=" 00:00:00";
        }else if(editorType.equals(EditorFactory.EDITORTYPE_TIMESTAMP))  //��ʱֻ������DB2��ݿ�
        {
            addtionalPart=" 00:00:00 000000";
        }else 
            addtionalPart="";
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#getHolderValue()
     */
    public Object getHolderValue() {
        String currentDate=getSelectedItem().toString();
        
        return currentDate+addtionalPart;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        if(value==null)
            try {
                setSelectedDate(new Date());
            } catch (ParseException e) {
                setSelectedItem("1900-01-01");
            }
        setSelectedItem(value==null?"":value.toString());
    }

}
