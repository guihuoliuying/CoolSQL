/*
 * �������� 2006-5-31
 *
 */
package com.cattsoft.coolsql.action.common;

import javax.swing.AbstractAction;
import javax.swing.JComponent;



/**
 * @author liu_xlin
 *
 * �����¼�������
 */
public abstract class PublicAction extends AbstractAction {

	 private JComponent com=null;
	 public PublicAction(JComponent com)
	 {
	 	super();
	 	this.com=com;
	 }
     public PublicAction()
     {
         this(null);
     }
	/**
	 * @param view Ҫ���õ� view��
	 */
	public void setComponent(JComponent com) {
		this.com = com;
	}
	/**
	 * @return ���� view��
	 */
	public JComponent getComponent() {
		return com;
	}
}
