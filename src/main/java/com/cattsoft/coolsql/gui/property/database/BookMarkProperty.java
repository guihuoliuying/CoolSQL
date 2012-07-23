/*
 * �������� 2006-9-12
 */
package com.cattsoft.coolsql.gui.property.database;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.gui.property.PropertyPane;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.BookMarkwizard.ConnectPropertyPanel;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��ǩ���Բ쿴������
 */
public class BookMarkProperty extends PropertyPane implements PropertyChangeListener{

	private static final long serialVersionUID = 1L;

	private ConnectPropertyPanel cp;

    private Bookmark bookmark;

    /**
     * ��ʱ���
     */
    private Map map = null;

    private String url;

    public BookMarkProperty() {
        super();
    }
	protected boolean isNeedListenToChild() {
		return false;
	}
    /*
     * 
     * @see com..gui.property.PropertyInterface#set()
     */
    public boolean set() {
		if(cp.getUserText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this,PublicResource.getString("connectpropertydialog.nouser"),"warning",2);
			return false;
		}
        if(!cp.checkData())  //���У��
        {
            return false;
        }
        if (bookmark == null)
            return false;
        bookmark.setUserName(cp.getUserText());
        bookmark.setPwd(cp.getPwdText());
        bookmark.setPromptPwd(cp.getBoxSelected());
        bookmark.setConnectUrl(cp.getUrl());
        try {
			bookmark.setAutoCommit(cp.getAutoCommitSet());
		} catch (Exception e) {
			LogProxy.errorReport(e);
		} 
        return true;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com..gui.property.PropertyPane#initContent()
     */
    public JPanel initContent() {
        cp = new ConnectPropertyPanel();
        return cp;
    }

    /**
     * @return ���� bookmark��
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com..gui.property.PropertyInterface#quit()
     */
    public void cancel() {
        bookmark.setConnectUrl(url);
        bookmark.getDriver().getParams().clear();
        bookmark.getDriver().setParams(map);
        map = null;
    }
    public void doOnClose() {
		bookmark.removePropertyListener(this);
		cp.removeAll();
		cp = null;
	}
    /*
	 * ���� Javadoc��
	 * 
	 * @see com..gui.property.PropertyInterface#apply()
	 */
    public void apply() {
		if(cp.getUserText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this,PublicResource.getString("connectpropertydialog.nouser"),"warning",2);
			return ;
		}
        if(!cp.checkData())  //���У��
        {
            return ;
        }
        if (bookmark == null)
            return ;
        bookmark.setUserName(cp.getUserText());
        bookmark.setPwd(cp.getPwdText());
        bookmark.setPromptPwd(cp.getBoxSelected());
        bookmark.setConnectUrl(cp.getUrl());
        try {
			bookmark.setAutoCommit(cp.getAutoCommitSet());
		} catch (Exception e) {
			LogProxy.errorReport(e);
		} 
        
        //���¸������
        map = (HashMap) ((HashMap) bookmark.getDriver().getParams()).clone(); 
        url = bookmark.getConnectUrl();
    }
    /*
     * ���� Javadoc��
     * 
     * @see com..gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if (ob == null)
            return;
        this.bookmark = (Bookmark) ob;
        bookmark.removePropertyListener(this); //�����ظ�����
        bookmark.addPropertyListener(this);
        map = (HashMap) ((HashMap) bookmark.getDriver().getParams()).clone(); //�������
        url = bookmark.getConnectUrl();
        cp.setConnectInfo(bookmark);
        this.validate();
    }

    /* ������ı�ʱ��������е���
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
       String name=evt.getPropertyName();
       if(name.equals("classname"))
       {
           setData(evt.getSource());
       }
    }

    /* ���� Javadoc��
     * @see com..gui.property.PropertyInterface#isNeedApply()
     */
    public boolean isNeedApply() {
        return true;
    }

}
