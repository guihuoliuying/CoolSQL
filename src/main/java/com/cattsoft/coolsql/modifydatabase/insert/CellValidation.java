/*
 * Created on 2007-3-7
 */
package com.cattsoft.coolsql.modifydatabase.insert;

/**
 * @author liu_xlin
 *��ؼ���Ԫ��Ԫ��ֵ��У��ӿ�
 */
public interface CellValidation {

    /**
     * У�鵥Ԫ�����ֵ����Ч��
     * @param value  --��Ԫ�����ֵ
     * @param row  --��Ԫ���Ӧ��������
     * @param column  --��Ԫ���Ӧ��������
     * @return  --���У��ɹ�������true�����ɹ������ܣ�����false
     */
    public boolean checkValidation(Object value,int row,int column);
}
