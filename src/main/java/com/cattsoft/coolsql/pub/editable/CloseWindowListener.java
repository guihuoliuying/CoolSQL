/**
 * 
 */
package com.cattsoft.coolsql.pub.editable;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

/**
 * @author kenny liu
 *������ɹرմ��ڵĶ�����ǰ���ǲ�ִ���κ��ض����߼������������ڹرմ���ǰ�Ƿ���ʾ��
 * 2007-11-6 create
 */
public class CloseWindowListener extends WindowAdapter {
	
	private Window window; //���رյĴ��ڶ���
	private boolean isPrompt; //�ر�ʱ�Ƿ������ʾ(true:��ʾ,false:����ʾ)
	
	/**
	 * �ù��췽��Ĭ�ϲ�������Ϣ��ʾ��
	 * @param w --���رյĴ���
	 */
	public CloseWindowListener(Window w)
	{
		this(w,false);
	}
	public CloseWindowListener(Window w,boolean isPrompt)
	{
		super();
		this.window=w;
		this.isPrompt=isPrompt;
	}
	public void windowClosing(WindowEvent e)
	{
		if(isPrompt)
		{
			int r=JOptionPane.showConfirmDialog(window, "","please confirm",JOptionPane.OK_CANCEL_OPTION);
			if(r==JOptionPane.OK_OPTION)
				window.dispose();
			else
				return;
		}else
			window.dispose();
	}
}
