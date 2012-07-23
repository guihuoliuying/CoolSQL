/*
 * Created on 2007-1-17
 */
package com.cattsoft.coolsql.modifydatabase.model;

import com.cattsoft.coolsql.modifydatabase.DataHolder;
import com.cattsoft.coolsql.modifydatabase.EditorLimiter;
import com.cattsoft.coolsql.pub.component.StringEditor;

/**
 * @author liu_xlin
 *�̳��ı��༭�����ʵ������ݳ��нӿڣ����ڻ�ȡ�༭�����ֵ
 */
public class SQLStringEditor extends StringEditor implements DataHolder,EditorLimiter {

    public SQLStringEditor() {
        super();
    }

    public SQLStringEditor(int MaxLen) {
        super(MaxLen);
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#getHolderValue()
     */
    public Object getHolderValue() {
        return getText();
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#setValue()
     */
    public void setValue(Object value) {
        setText(value==null?"":value.toString());       
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.EditorLimiter#setSize(int)
     */
    public void setSize(int size) {
        setMaxLength(size);
    }

    /*�����κδ���
     * @see com.coolsql.modifydatabase.EditorLimiter#setDigits(int)
     */
    public void setDigits(int digits) {
        
    }

}
