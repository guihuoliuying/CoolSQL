/*
 * �������� 2006-9-12
 */
package com.cattsoft.coolsql.gui.property.database;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.gui.property.PropertyInterface;
import com.cattsoft.coolsql.gui.property.PropertyPane;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin ��ݿ���Ϣ�������
 */
public class DatabaseProperty extends PropertyPane implements PropertyInterface {

    private Bookmark bookmark;

    private JLabel l;
    public DatabaseProperty() {
        super();

    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.gui.property.PropertyInterface#set()
     */
    public boolean set() {
        return true;
    }

    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#apply()
     */
    public void apply() {
       set();
        
    }
    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.gui.property.PropertyPane#initContent(java.lang.Object)
     */
    public JPanel initContent() {
        JPanel tmp = new JPanel();
        l=new JLabel("");
        tmp.add(l);
        return tmp;
    }
    
    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.gui.property.PropertyInterface#quit()
     */
    public void cancel() {

    }

    /* ���� Javadoc��
     * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if (ob == null)
            return ;

        Bookmark bookmark = (Bookmark) ob;
        if (bookmark.isConnected()) {
            setInfo(PublicResource
                    .getSQLString("sql.propertyset.dbproduct")
                    + bookmark.getDriver().getDbProductName()
                    + " "
                    + bookmark.getDriver().getDbPruductVersion());
        } else
            setInfo(PublicResource
                    .getSQLString("sql.propertyset.noversioninfo"));
        
    }
   public void setInfo(String txt)
   {
       l.setText(txt);
   }

/* ���� Javadoc��
 * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
 */
public boolean isNeedApply() {
    return false;
}
}
