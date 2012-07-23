/*
 * Created on 2007-1-31
 */
package com.cattsoft.coolsql.modifydatabase.insert;

/**
 * @author liu_xlin
 *��ͷԪ�ض���Ľӿڶ���
 */
public interface TableHeaderCell {

    /**
     * ��ȡ��ͷ��״̬���˶��������ڱ�ͷ���Ƿ�ѡ��״̬
     * @return
     */
    public boolean getState();
    
    /**
     * ��ȡ��ͷԪ�صĶ���ֵ
     * @return
     */
    public Object getHeaderValue();
}