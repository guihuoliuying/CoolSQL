/*
 * �������� 2006-6-27
 *
 */
package com.cattsoft.coolsql.view.BookMarkwizard;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.cattsoft.coolsql.action.common.NextButtonAction;
import com.cattsoft.coolsql.action.common.PreButtonAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.component.CommonFrame;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.log.LogProxy;


/**
 * @author liu_xlin 
 * ������ǩ�����ԶԻ���
 */
public class ConnectPropertyDialog extends CommonFrame {

	private static final long serialVersionUID = 1L;
	
	private Bookmark bookmark;
	private ConnectPropertyPanel connectPane;
	/**���캯��
	 * @param d����������
	 * @param b���Ƿ���ģ̬��ʽ����
	 * @param view��������ͼ
	 */
	public ConnectPropertyDialog(JDialog d, View view,Bookmark bookmark) {
		super(d, true, view);
		this.bookmark=bookmark;
		init();
	}
	public ConnectPropertyDialog(JDialog d, boolean b, View view,Bookmark bookmark) {
		super(d, b, view);
		this.bookmark=bookmark;
		init();
	}
	public ConnectPropertyDialog(JFrame f, View view,Bookmark bookmark) {
		super(f, true,view);
		this.bookmark=bookmark;
		init();
	}
	public ConnectPropertyDialog(JFrame f, boolean b, View view,Bookmark bookmark) {
		super(f, b,view);
		this.bookmark=bookmark;
		init();
	}
    public void init()
    {
    	this.setTitle(PublicResource.getString("connectpropertydialog.title"));
    	super.setDescribeText(PublicResource.getString("bookmark.connectinfotitle"));
    	connectPane.setConnectInfo(bookmark);
    	enablePreButton(new PreButtonAction(this),true);
    	enableNextButton(new NextButtonAction(this),true);
    	 this.getRootPane().setDefaultButton(nextButton);
    }
	/* ���� Javadoc��
	 * @see src.pub.window.CommonFrame#initDialog()
	 */
	public JComponent initDialog() {
		connectPane=new ConnectPropertyPanel();
		JScrollPane sp=new JScrollPane(connectPane);
		return sp;
	}
	/**
	 * @return ���� bookmark��
	 */
	public Bookmark getBookmark() {
		if(bookmark==null)
			bookmark=new Bookmark();
		bookmark.setUserName(connectPane.getUserText());
		bookmark.setPwd(connectPane.getPwdText());
		bookmark.setPromptPwd(connectPane.getBoxSelected());
		bookmark.setConnectUrl(connectPane.getUrl());
		try {
			bookmark.setAutoCommit(connectPane.getAutoCommitSet());
		} catch (Exception e) {
			LogProxy.errorReport(e);
		}
		return bookmark;
	}
	/**
	 * У����ݵ�������
	 * @return
	 */
	public boolean checkData()
	{
//		if(bookmark.getUserName().trim().equals(""))
//		{
//			JOptionPane.showMessageDialog(this,PublicResource.getString("connectpropertydialog.nouser"),"warning",2);
//			return false;
//		}
//		if(bookmark.getConnectUrl().trim().equals(""))
//		{
//			JOptionPane.showMessageDialog(this,PublicResource.getString("connectpropertydialog.nodatasource"),"warning",2);
//			return false;
//		}
		if(!connectPane.checkData())
		{
			return false;
		}
		return true;
	}
	/**
	 * @param bookmark Ҫ���õ� bookmark��
	 */
	public void setBookmark(Bookmark bookmark) {
		this.bookmark = bookmark;
		connectPane.setConnectInfo(bookmark);
	}
	/**
	 * ������һ����ť�����Ĵ���ӿ�
	 * @param ob
	 */
	public void nextButtonProcess(Object ob)
	{		
		Bookmark tmp=getBookmark();
		if(!checkData())
			return;
		this.dispose();
		new AliasNameInputDialog((JFrame)(this.getParent()),getView(),tmp).setVisible(true);
	}
	/**
	 * ������һ����ť�����Ĵ���ӿ�
	 * @param ob
	 */
	public void preButtonProcess(Object ob)
	{		
		getBookmark();
		this.dispose();
		Container con=this.getParent();
		new BookMarkWizardFrame((JFrame)(this.getParent()),getView(),bookmark).setVisible(true);
	}
}
