/**
 * Create date:2008-5-17
 */
package com.cattsoft.coolsql.action.framework;

import java.awt.event.ActionListener;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-17 create
 */
public class AutoCsAction extends CsAction {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public AutoCsAction() {
		super();
		init();
	}

	/**
	 * @param l
	 */
	public AutoCsAction(ActionListener l) {
		super(l);
		init();
	}

	/**
	 * @param l
	 * @param name
	 */
	public AutoCsAction(ActionListener l, String name) {
		super(l, name);
		init();
	}
	protected void init()
	{
		initMenuDefinitionById(getClass().getSimpleName());
	}
}
