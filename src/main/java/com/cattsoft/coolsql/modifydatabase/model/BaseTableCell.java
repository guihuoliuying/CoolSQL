/*
 * Created on 2007-1-12
 */
package com.cattsoft.coolsql.modifydatabase.model;

import java.awt.Color;

import com.cattsoft.coolsql.modifydatabase.EditorFactory;
import com.cattsoft.coolsql.modifydatabase.TableCell;

/**
 * @author liu_xlin
 *��Բ���(insert)�͸��£�update�����಻ͬ��sql��ɣ�����ÿ�����Ӧ�ı��Ԫ���ص㲻һ��Ϊ�˶���һ�������͡�
 */
public abstract class BaseTableCell implements TableCell {

    /**
     * �Ƿ�ɱ༭����
     */
    private boolean isEditable;
    /**
     * ǰ��ɫ��ɫ
     */
    private Color foregroundColor=null;
    
    /**
     * ����ɫ��ɫ
     */
    private Color backgroundColor=null;
    
    /**
     * �Ƿ���Ϊ���������ı�־�ֶ�
     */
    private boolean isAsTerm;
    
    private boolean isNull;
    public BaseTableCell()
    {
        this(false,null,null,false,false);
    }
    public BaseTableCell(boolean isEditable,boolean isAsTerm,boolean isNull)
    {
        this(isEditable,null,null,isAsTerm,isNull);
    }
    public BaseTableCell(boolean isEditable,Color foreColor,Color backColor,boolean isAsTerm,boolean isNull)
    {
        this.isEditable=isEditable;
        foregroundColor=foreColor;
        backgroundColor=backColor;
        this.isAsTerm=isAsTerm;
        this.isNull=isNull;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#isEditable()
     */
    public boolean isEditable() {
        return isEditable;
    }

    /* Ĭ��Ϊ�ı�����
     * @see com.coolsql.modifydatabase.TableCell#getEditor()
     */
    public String getEditorType() {
        return EditorFactory.EDITORTYPE_TEXT;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#isNeedModify()
     */
    public boolean isNeedModify() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getRenderOfForeground()
     */
    public Color getRenderOfForeground() {
        return foregroundColor;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getRenderOfBackground()
     */
    public Color getRenderOfBackground() {
        return backgroundColor;
    }

    /**
     * ʵ�ַ���ı���Ĭ���뷽����toString()�ķ���ֵһ��
     */
    public String getStyleString()
    {
        return toString();
    }
    /**
     * ʹ�����ֶ��ܹ����Ķ����ֶ��Ƿ���Ϊ��������
     * Ĭ�ϲ���Ϊ��������
     */
    public boolean isAsTerm()
    {
        return isAsTerm;
    }
    /**
     * 
     */
    public boolean isNullValue()
    {
        return isNull;
    }
    public void setIsNullValue(boolean isNull)
    {
        this.isNull=isNull;
    }
    /**
     * @param isAsTerm The isAsTerm to set.
     */
    public void setAsTerm(boolean isAsTerm) {
        this.isAsTerm = isAsTerm;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getToolTip()
     */
    public String getToolTip() {
        return null;
    }
    public String toString()
    {
        return getValue().toString();
    }
    /**
     * @param backgroundColor The backgroundColor to set.
     */
    public void setRenderOfBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /**
     * @param foregroundColor The foregroundColor to set.
     */
    public void setRenderOfForeground(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    /**
     * @param isEditable The isEditable to set.
     */
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
}
