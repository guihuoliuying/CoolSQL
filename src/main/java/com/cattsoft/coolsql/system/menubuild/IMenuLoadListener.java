/**
 * 
 */
package com.cattsoft.coolsql.system.menubuild;

import java.util.EventListener;

import javax.swing.JMenuItem;

/**
 * ϵͳ�˵�����ʱִ�иýӿڶ���ķ���action();
 * @author kenny liu
 *
 * 2007-11-3 create
 */
public interface IMenuLoadListener extends EventListener{

	/**
	 * 
	 * @param item �Ѿ���������ɵĲ˵�/�˵������
	 */
	public void action(JMenuItem item);
}
