/*
 * �������� 2006-7-6
 *
 */
package com.cattsoft.coolsql.view.BookMarkwizard;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.URLBuilder;


/**
 * @author liu_xlin
 *��ݿ�������Ϣ¼�����
 */
public class URLInfoPanel extends JPanel {
   /**
    * Connection url pattern
    */
	private String urlPattern=null;
	/**
	 *���[ ����������ֵ ]
	 */
	private Map parameters=null;
	/**
	 * ���������
	 */
	private ActiveText[] texts=null;
	/**
	 * Connection url
	 */
	private TextEditor urlText=null;
	public URLInfoPanel()
	{
		this(null,null);
	}
	public URLInfoPanel(String urlPatten,Map params)
	{
		super();
		this.urlPattern=urlPatten;
		parameters=params;
		if(urlPatten!=null&&params!=null)
		{
			dataInit(urlPatten,params);
			
		}
		
	}
	/**
	 * ����url���
	 * @param urlPatten
	 * @param params
	 */
	public void updateAll(String urlPatten,Map params)
	{
		this.removeAll();
		dataInit(urlPatten,params);
		this.validate();
	}
	/**
	 * ��ʼ�����
	 * @param urlPatten
	 * @param params
	 */
	public void dataInit(String urlPatten,Map params)
	{
		setParameters(params);
		setUrlPatten(urlPatten);
        texts=new ActiveText[params.size()];
		Set set=params.keySet();
		Iterator it=set.iterator();
		int count=0;
		while(it.hasNext())
		{
			String key=(String)it.next();
			String value=(String)params.get(key);
			texts[count]=new ActiveText();
			texts[count].setText(value);
			texts[count].putClientProperty("key",key);
			count++;
		}
		guiInit();
	}
	/**
	 * ��ʼ�����
	 *
	 */
	private void guiInit()
	{
		JPanel tmp=new JPanel();
//		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setLayout(new BorderLayout());
		tmp.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.weightx=0;
//		gbc.weighty=0;

		
		gbc.insets=new Insets(2,2,2,2);
		
		for(int i=0;i<texts.length;i++)
		{
//		    gbc.fill=GridBagConstraints.NONE;
			gbc.anchor=GridBagConstraints.EAST;
			gbc.gridwidth=1;
			gbc.weightx = 0D;
			tmp.add(new JLabel((String)texts[i].getClientProperty("key")+":",4),gbc);
			gbc.gridx++;
			gbc.weightx=1.0D;
//			gbc.fill=GridBagConstraints.HORIZONTAL;
			gbc.gridwidth=GridBagConstraints.REMAINDER;
			gbc.anchor=GridBagConstraints.WEST;
			gbc.insets=new Insets(2,2,2,5);
			tmp.add(texts[i],gbc);
			gbc.gridx--;
			gbc.gridy++;
		}
		gbc.gridwidth=1;
		gbc.weightx = 0D;
		gbc.insets=new Insets(2,2,2,2);
		gbc.anchor=GridBagConstraints.EAST;
		tmp.add(new JLabel("URL:",4),gbc);
		
		gbc.weightx=1.0D;
		gbc.gridx++;
		urlText=new TextEditor();
		urlText.setEditable(false);
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.anchor=GridBagConstraints.WEST;
		gbc.insets=new Insets(2,2,2,5);
		tmp.add(urlText,gbc);
		setURL();
		
		Border urlBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		urlBorder = BorderFactory.createTitledBorder(urlBorder, PublicResource
				.getString("connectpropertypanel.connecttitle"));
		tmp.setBorder(urlBorder);
		this.add("Center",tmp);
	}
	private void setURL()
	{
		if(this.getUrlPatten()!=null&&this.getParameters()!=null)
		  urlText.setText(URLBuilder.createURL(this.getUrlPatten(),this.getParameters()));
		else if(this.getUrlPatten()==null&&this.getParameters().size()==1)
		{
			String key=(String)getParameters().keySet().iterator().next();
			String value=(String)getParameters().get(key);
			urlText.setText(value==null?"":value);
		}else
		{
			JOptionPane.showMessageDialog(this, "Unknown driver information!","Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
	private class ActiveText extends TextEditor
	{
	    public ActiveText()
	    {
			   super();	
			   this.getDocument().addDocumentListener(new URLDocumentListener(this));
	    }
		public ActiveText(int len)
		{
		   super(len);	
		   this.getDocument().addDocumentListener(new URLDocumentListener(this));
		}
		public void valueChange()
		{
			String key=(String)this.getClientProperty("key");
			if(key!=null&&getParameters().containsKey(key))
			{
				getParameters().put(key,this.getText());
				setURL();
			}
		}
	}
	/**
	 * У�����������Ƿ��Ѿ���д
	 * @return
	 */
	public boolean checkData()
	{
		for(int i=0;i<texts.length;i++)
		{
			if(texts[i].getText().trim().equals(""))
			{
				String name=(String)texts[i].getClientProperty("key");
				JOptionPane.showMessageDialog(this,"���������:"+name,"warning",2);
				texts[i].requestFocusInWindow();
				return false;
			}
		}
		return true;
	}
	protected class URLDocumentListener implements DocumentListener
	{

		private ActiveText text=null;
		public URLDocumentListener(ActiveText text)
		{
			this.text=text;
		}
		/* ���� Javadoc��
		 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
		 */
		public void insertUpdate(DocumentEvent e) {
			
			text.valueChange();
		}

		/* ���� Javadoc��
		 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
		 */
		public void removeUpdate(DocumentEvent e) {
			text.valueChange();
			
		}

		/* ���� Javadoc��
		 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
		 */
		public void changedUpdate(DocumentEvent e) {
			
		}
		
	}

	/**
	 * @return ���� connectUrl��
	 */
	public String getConnectUrl() {
		return urlText.getText();
	}

	/**
	 * @param connectUrl
	 *            Ҫ���õ� connectUrl��
	 */
	public void setConnectUrl(String connectUrl) {
		urlText.setText(connectUrl);
	}

	/**
	 * @return ���� parameters��
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @return ���� texts��
	 */
	public ActiveText[] getTexts() {
		return texts;
	}
	/**
	 * @param texts Ҫ���õ� texts��
	 */
	public void setTexts(ActiveText[] texts) {
		this.texts = texts;
	}
	/**
	 * @return ���� urlPatten��
	 */
	public String getUrlPatten() {
		return urlPattern;
	}
	/**
	 * @param urlPatten Ҫ���õ� urlPatten��
	 */
	public void setUrlPatten(String urlPatten) {
		this.urlPattern = urlPatten;
	}
	/**
	 * @param parameters Ҫ���õ� parameters��
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}
}
