/**
 * Create date:2009-12-23
 */
package com.cattsoft.coolsql.action.framework;

import java.awt.event.ActionListener;

/**
 * @author (kenny liu)
 *
 * 2009-12-23 create
 */
public class Auto2CsAction extends CsAction {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public Auto2CsAction() {
		super();
		init();
	}

	/**
	 * @param l
	 */
	public Auto2CsAction(ActionListener l) {
		super(l);
		init();
	}

	/**
	 * @param l
	 * @param name
	 */
	public Auto2CsAction(ActionListener l, String name) {
		super(l, name);
		init();
	}
	protected void init()
	{
		initMenuDefinitionById(getClass().getName());
	}
}
