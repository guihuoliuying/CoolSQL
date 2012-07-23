/*
 * Created on 2007-4-26
 */
package com.cattsoft.coolsql.pub.component;

import java.util.Vector;

import javax.swing.ComboBoxModel;

/**
 * @author liu_xlin
 *��������༭������ṩ��ʷ��¼��������ǰ������ֵ���б��棬�����Ժ��ֱ��ѡ��
 */
public class RecordEditCombBox extends EditComboBox {

    /**
     * ���?����ʷ����������
     */
    private int maxItems;
    public RecordEditCombBox()
    {
        super();
        init();
    }
    public RecordEditCombBox(ComboBoxModel model)
    {
        super(model);
        init();
    }
    public RecordEditCombBox(Object[] items)
    {
        super(items);
        init();
    }
    public RecordEditCombBox(Vector items)
    {
        super(items);
        init();
    }
    /**
     * ��ʼ��
     *
     */
    protected void init()
    {
        maxItems=10; //��ʼֵ���?��10��
        setEditable(true);
    }
    /**
     * ����ǰѡ��ֵ�����ѡ���б��У�����Ѿ����ڣ���������ڵ�һ��λ��
     *
     */
    public void record()
    {
        String str=getSelectedItem().toString();
        if(str.equals(""))  //�������ֵΪ�մ�����������
            return;
        
        removeItem(str);
        
        if(getItemCount()>=maxItems)  //������󱣴�������ɾ�����һ��
            removeItemAt(getItemCount()-1);
        
        insertItemAt(str,0);
        
        setSelectedIndex(0);
    }

    /**
     * @return Returns the maxItems.
     */
    public int getMaxItems() {
        return maxItems;
    }
    /**
     * @param maxItems The maxItems to set.
     */
    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }
}
