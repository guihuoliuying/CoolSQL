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
public class NextPageProcessAction extends SQLPageProcessAction {

	private static final long serialVersionUID = 1L;
	
	public NextPageProcessAction()
	{
		super(ResultSetDataProcess.NEXT);
		initMenuDefinitionById("NextPageProcessAction");
	}
}
