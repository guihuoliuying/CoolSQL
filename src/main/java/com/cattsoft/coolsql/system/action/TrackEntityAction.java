/**
 * 
 */
package com.cattsoft.coolsql.system.action;

import java.awt.event.ActionEvent;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.system.task.IExecute;
import com.cattsoft.coolsql.system.task.TaskManage;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-3-5 create
 */
public class TrackEntityAction extends PublicAction {

	private static final long serialVersionUID = 1L;
	ActionCommand command=null;
	public TrackEntityAction()
	{
		command=new TrackEntityCommand();
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		TaskManage.getInstance().addTask(new IExecute()
		{
			public void run() throws Throwable
			{
				command.exectue();
			}
		}
		);
	}
	

}
