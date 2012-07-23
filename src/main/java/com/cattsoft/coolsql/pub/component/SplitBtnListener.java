/*
 * �������� 2006-9-25
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.event.ActionEvent;
import java.util.EventListener;

/**
 * @author liu_xlin
 *
 */
public interface SplitBtnListener extends EventListener {

	/**
	 * �÷�������ϰ�ť������ť�����¼�ʱ������
	 * @param e   --��ť�¼�����
	 * @param data  --��ݶ��󣬸ö��󱣴��ڲ˵����У�ͨ��<a>JMenuItem.getClientProperty(Object key)</a>
	 */
	public abstract void action(ActionEvent e,Object data);
}
