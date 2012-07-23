/*
 * �������� 2006-6-27
 *
 */
package com.cattsoft.coolsql.view.BookMarkwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.parse.PublicResource;


/**
 * @author liu_xlin ������ݿ�ʱ������Ϣ���
 */
public class ConnectPropertyPanel extends JPanel {
	private static final long serialVersionUID = -8058570152213571487L;

	private TextEditor userText = null;

	private JPasswordField pwdText = null;
    
	//�Ƿ���ʾ��������
	private JCheckBox box = null;
	//�Զ��ύ����
    private JCheckBox autoCommitSet=null;
    
	private URLInfoPanel urlPane=null;

    private Bookmark bookmark;
	public ConnectPropertyPanel() {	
	    this(null);
	}
	public ConnectPropertyPanel(Bookmark bookmark) {
		super();
		this.bookmark=bookmark;
		setLayout(new BorderLayout());
        JPanel pane=new JPanel();
        pane.setLayout(new GridBagLayout());
		
        //�������ֵ���������
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.0D;
        
		//�û���
		JLabel user = new JLabel(PublicResource.getString("connectpropertypanel.input.user"),SwingConstants.RIGHT);
		userText = new TextEditor();
		pane.add(user,gbc);
		gbc.weightx = 1.0D;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(userText,gbc);
		gbc.gridwidth = 1; //�ָ��������ռ����
		gbc.weightx = 0.0D;
		
		//����
		gbc.anchor = GridBagConstraints.EAST;
		JLabel pwd = new JLabel(PublicResource.getString("connectpropertypanel.input.password"),SwingConstants.RIGHT);
		pwdText = new JPasswordField();
		pane.add(pwd,gbc);
		gbc.weightx = 1.0D;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(pwdText,gbc);

		//�Ƿ���ʾ��������
		box = new JCheckBox(PublicResource
				.getString("connectpropertypanel.promptpwdtext"));
		pane.add(box);
        box.setHorizontalTextPosition(SwingConstants.LEFT);
        JPanel p31=new JPanel();
        p31.setLayout(new FlowLayout(FlowLayout.LEFT));
        p31.add(new JLabel(PublicResource.getString("connectpropertypanel.autocommitset")));
        autoCommitSet=new JCheckBox();
//        autoCommitSet.addItem("Always True");
//        autoCommitSet.addItem("Always False");
//        autoCommitSet.addItem("Last Saved");
        p31.add(autoCommitSet);
        pane.add(p31);
        
		//����url
		if(bookmark==null)
			urlPane=new URLInfoPanel();
	    else
	    {
	    	if(bookmark.getDriver().getParams().size()==0)
	    	{
	    		bookmark.getDriver().getParams().put("dbName", bookmark.getConnectUrl());
	    	}
		    urlPane=new URLInfoPanel(bookmark.getDriver().getOriginalURL(),bookmark.getDriver().getParams());
	    }

		Border border=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		border=BorderFactory.createTitledBorder(border,PublicResource.getString("connectpropertypanel.input.safeinfo"));
		pane.setBorder(border);
		
		add(pane,BorderLayout.NORTH);
		add(urlPane,BorderLayout.CENTER);
		
		setConnectInfo(bookmark);
	}

	/**
	 * @param bookmark
	 *            Ҫ���õ� bookmark��
	 */
	public void setConnectInfo(Bookmark bookmark) {
      
		if(bookmark==null)
			return;
	    this.setUserText(bookmark.getUserName());
	    this.setPwdText(bookmark.getPwd());
	    this.setBoxSelected(bookmark.isPromptPwd());
	    this.setAutoCommitSet(bookmark.isAutoCommit());
	    if(bookmark.getDriver().getParams().size()==0)
    	{
    		bookmark.getDriver().getParams().put("dbName", bookmark.getConnectUrl());
    	}
	    urlPane.updateAll(bookmark.getDriver().getOriginalURL(),bookmark.getDriver().getParams());
	    validate();
	}
	/**
	 * У�����������Ƿ��Ѿ���д
	 * @return
	 */
    public boolean checkData()
    {
    	return urlPane.checkData();
    }
	/**
	 * @return ���� box��
	 */
	public boolean getBoxSelected() {
		
		return box.getModel().isSelected();
	}
	/**
	 * @param box
	 *            Ҫ���õ� box��
	 */
	public void setBoxSelected(boolean selected) {
		this.box.getModel().setSelected(selected);
	}
	/**
	 * @param box
	 *            Ҫ���õ� box��
	 */
	public void setBoxText(String boxText) {
		this.box.setText(boxText);
	}
	/**
	 * @param box
	 *            Ҫ���õ� box��
	 */
	public String getBoxText() {
		return this.box.getText();
	}
	/**
	 * @return ���� pwdText��
	 */
	public String getPwdText() {
		return new String(pwdText.getPassword());
	}

	/**
	 * @param pwdText
	 *            Ҫ���õ� pwdText��
	 */
	public void setPwdText(String pwdText) {
		this.pwdText.setText(pwdText);
	}

	/**
	 * @return ���� url��
	 */
	public String getUrl() {
		return urlPane.getConnectUrl();
	}

	/**
	 * @param url
	 *            Ҫ���õ� url��
	 */
	public void setUrl(String url) {
		urlPane.setConnectUrl(url);
	}

	/**
	 * @return ���� userText��
	 */
	public String getUserText() {
		return userText.getText();
	}

	/**
	 * @param userText
	 *            Ҫ���õ� userText��
	 */
	public void setUserText(String userText) {
		this.userText.setText(userText);
	}
	
    /**
     * @return ���� autoCommitSet��
     */
    public boolean getAutoCommitSet() {
        return autoCommitSet.isSelected();
    }
    /**
     * @param autoCommitSet Ҫ���õ� autoCommitSet��
     */
    public void setAutoCommitSet(boolean isSelected) {
        this.autoCommitSet.setSelected(isSelected);
    }
    /**
     * @return ���� bookmark��
     */
    public Bookmark getBookmark() {
        return bookmark;
    }
}
