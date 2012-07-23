/*
 * Created on 2007-1-17
 */
package com.cattsoft.coolsql.modifydatabase;

/**
 * @author liu_xlin
 *��ݾ���ȡ�ӿڣ����ڸ��ֱ༭������ڱ༭��ɺ󣬷��ص���Ӧ�����ֵ
 */
public interface DataHolder {

    /**
     * ��ȡ�༭�����ǰ����ݶ���ֵ
     * @return --�༭�����ǰֵ
     */
    public abstract Object getHolderValue();
    
    /**
     * ���ñ༭�����ֵ
     *
     */
    public abstract void setValue(Object value);
}
