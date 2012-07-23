/**
 * Create date:2008-5-18
 */
package com.cattsoft.coolsql.view.resultset;

import java.util.EventListener;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-18 create
 */
public interface TableModelModifyListener extends EventListener {

	/**
	 * This method will be invoked when the some cell of Data table model take a change.
	 */
	public void dataChanged(TableModelModifyEvent e);
}
