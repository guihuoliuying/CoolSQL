/*
 * �������� 2006-12-11
 */
package com.cattsoft.coolsql.bookmarkBean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * @author liu_xlin �������װ������ǩ�б��������ؼ�������ǩɾ����ӡ��������ʱ���ֱ�Կؼ�����ʾ���е���
 */
public class BookmarkUpdateOfComboBoxListener implements BookmarkListener,
        PropertyChangeListener {

    /**
     * װ����ǩ�����������ؼ�
     */
    private JComboBox box = null;

    public BookmarkUpdateOfComboBoxListener(JComboBox box) {
        super();
        this.box = box;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkAdded(com.coolsql.bookmarkBean.BookMarkEvent)
     */
    public void bookmarkAdded(BookmarkEvent e) {
        e.getBookmark().addPropertyListener(this);
        DefaultComboBoxModel model = (DefaultComboBoxModel) box.getModel();
        model.addElement(e.getBookmark().getAliasName());
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkDeleted(com.coolsql.bookmarkBean.BookMarkEvent)
     */
    public void bookmarkDeleted(BookmarkEvent e) {
        e.getBookmark().removePropertyListener(this);
        DefaultComboBoxModel model = (DefaultComboBoxModel) box.getModel();
        model.removeElement(e.getBookmark().getAliasName());
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkUpdated(com.coolsql.bookmarkBean.BookMarkEvent)
     */
    public void bookMarkUpdated(BookmarkEvent e) {

    }

    /*
     * ��ѯ����У���ݿ������仯ʱ�ļ�����
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("aliasName")) {
            String oldValue = (String) evt.getOldValue();
            String newValue = (String) evt.getNewValue();
            if (oldValue.equals(newValue))
                return;
            DefaultComboBoxModel model = (DefaultComboBoxModel) box.getModel();
            int selectIndex = box.getSelectedIndex();
            int index = model.getIndexOf(oldValue);
            model.removeElement(oldValue);
            model.insertElementAt(newValue, index);
            box.setSelectedIndex(selectIndex);
        }

    }

}
