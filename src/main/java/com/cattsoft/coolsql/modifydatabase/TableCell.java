/*
 * Created on 2007-1-12
 */
package com.cattsoft.coolsql.modifydatabase;

import java.awt.Color;

import javax.swing.JTable;

/**
 * @author liu_xlin
 *���Ԫ�ؽӿڣ��������insert��update���͵�sql���
 */
public interface TableCell {

    /**
     * ���ñ�����ʾ,��ǰ��ɫ����Ҫ��Ⱦ,ʹ�ô˷�����ȡǰ��ɫ.����null,��ʾ�ñ��ǰ��ɫ����Ҫ��Ⱦ
     * @return  --�����Ҫ��Ⱦ��ǰ��ɫ
     */
    public abstract Color getRenderOfForeground();
    
    /**
     * ��ȡ����ɫ����Ⱦ��ɫ,����null,��ʾ�ñ�񱻾�ɫ����Ҫ��Ⱦ
     * @return --��񱻾�ɫ����Ⱦ��ɫ
     */
    public abstract Color getRenderOfBackground();
    /**
     * ���Ԫ���Ƿ���Ա༭
     * @return --true���ɱ༭  false:���ɱ༭
     */
    public abstract boolean isEditable();
    
    /**
     * ��ȡ���Ԫ�����Ķ���ֵ
     * @return
     */
    public abstract Object getValue();
    
    /**
     * ����ñ�����Ӧ�ı༭�������
     * @return �÷���ֵ����EditorFactory��������͵Ķ���һ��
     */
    public abstract String getEditorType();
    
    /**
     * �÷������ر����������ı�ؼ�
     * @return
     */
    public abstract JTable getTable();
    
    /**
     * �����˸ñ��Ԫ�����Ӧ���ֶζ����Ƿ���Ҫ�����
     * 1������ڽ��д�������sqlʱ���÷�����ӳ�˶�Ӧ�ֶ��Ƿ���Ҫ����Ϊһ����Ч�ֶΣ�������һ���Ϸ�ֵ��������ݿ���
     * 2������ڽ��д�������sqlʱ���÷�����ӳ����Ҫ�����µ��ֶΡ�
     * @return true�����ֶν�����Ϊ������߸��µ��ֶμ���sql�С� false�������κεĵ���
     */
    public abstract boolean isNeedModify();
    
    /**
     * ���������ڸñ��֮��ʱ����Ҫ��Ӧ����ʾ��Ϣ���÷����ṩ����Ҫ��ʾ���ı�����
     * @return  --��ʾ�ı���Ϣ
     */
    public abstract String getToolTip();
    
    /**
     * չʾ���Ԫ��ʱ�ķ���ı�,ͨ���Ծ�̬html��ʽ���з���
     * @return --����html��ʽ���ַ�
     */
    public abstract String getStyleString();
    
    /**
     * �Ƿ���Ϊ��������������ڸ��������ʱ����Ҫ��ĳһ�ֶ���Ϊ�����������ɶ��Ƹ÷���
     * @return  --true������Ϊ��������  false:����Ϊ��������
     */
    public abstract boolean isAsTerm();
    
    /**
     * ���Ԫ�ض�����Чֵ�Ƿ���null
     * @return  true:null  false:��null
     */
    public abstract boolean isNullValue();
}
