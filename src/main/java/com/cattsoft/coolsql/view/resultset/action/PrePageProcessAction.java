/**
 * 
 */
package com.cattsoft.coolsql.view.resultset.action;

import com.cattsoft.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-20 create
 */
public class PrePageProcessAction extends SQLPageProcessAction {

	private static final long serialVersionUID = 1L;
	
	public PrePageProcessAction()
	{
		super(ResultSetDataProcess.PREVIOUS);
		initMenuDefinitionById("PrePageProcessAction");
	}

}
