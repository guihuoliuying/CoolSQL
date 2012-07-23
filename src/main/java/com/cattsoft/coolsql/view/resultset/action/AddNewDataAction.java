/**
 * 
 */
package com.cattsoft.coolsql.view.resultset.action;

/**
 * @author ��Т��(kenny liu)
 * 
 * 2008-4-20 create
 */
public class AddNewDataAction extends ModifyDataAction {
	private static final long serialVersionUID = 1L;
	public AddNewDataAction()
	{
		super(ModifyDataAction.INSERT);
		initMenuDefinitionById("AddNewDataAction");
	}
}
