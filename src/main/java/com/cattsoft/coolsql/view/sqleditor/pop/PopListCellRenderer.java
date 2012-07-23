/*
 * Created on 2007-2-9
 */
package com.cattsoft.coolsql.view.sqleditor.pop;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;

/**
 * @author liu_xlin ����list�ؼ���Ԫ����Ⱦ�࣬���ʵ�����������Ӧ��ͬ����ʾͼ��
 */
public class PopListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	public PopListCellRenderer() {

    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        BaseListCell cell=(BaseListCell)value;
        super.getListCellRendererComponent(list,cell.getDisplayLabel(),index,isSelected,cellHasFocus);        
        
        this.setIcon(BookMarkPubInfo.getIconList()[cell.getType()]);
        return this;
    }
}
