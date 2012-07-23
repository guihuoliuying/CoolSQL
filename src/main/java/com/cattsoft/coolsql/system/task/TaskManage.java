/**
 * 
 */
package com.cattsoft.coolsql.system.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-2-3 create
 */
public class TaskManage {

	private static final StringManager strMg=StringManagerFactory.getStringManager(TaskManage.class);
	
	private static TaskManage tm=null;
	private List<IExecute> tasks=null;
	
	private IExecute currentTask=null; //task that is executing,it will be null if have no task to execute.
	
//	private int taskCount=0; //the count of all thread that is executing currently.
	
//	private boolean isSuspend=false; //this variant is not useful,but may be used to control the executing of task;
	
	private boolean isStopExecuting=false;
	private TaskController tc=null;
	public static TaskManage getInstance()
	{
		if(tm==null)
			tm=new TaskManage();
		
		return tm;
	}
	private TaskManage()
	{
		tasks=new ArrayList<IExecute>();
		tc=new TaskController();
		
		/**
		 * set the controller thread as a daemon thread.
		 */
		Thread controllerThread =new Thread(tc);
		controllerThread.setDaemon(true);
		controllerThread.setPriority(Thread.MIN_PRIORITY);
		controllerThread.start();
	}
	private synchronized IExecute nextTask()
	{
		IExecute task=null;
		if(tasks.size()>0)
		{
			task=tasks.remove(0);
			return task;
		}else
			return null;
		
	}
	public void addTask(IExecute task)
	{
		if(task!=null)
		{
			tasks.add(task);
			tc.awake();
		}
	}
	public void showMessage(final Throwable t)
	{
//		SwingUtilities.invokeLater(new Runnable()
//		{
//			public void run()
//			{
				if(t instanceof SQLException)
					LogProxy.SQLErrorReport((SQLException)t);
				else
					LogProxy.errorReport(strMg.getString("error.runtime") + t.getMessage(), t);
//			}
//		});
	}
	/**
	 * 
	 * @param task -- the task that need to be removed
	 * @throws TaskException --if task is executing,it 'll throw taskexception.
	 */
	public void removeTask(Runnable task) throws TaskException
	{
		if(currentTask==task)
			throw new TaskException(strMg.getString("error.taskexecuting"));
		tasks.remove(task);
	}
	private class TaskController implements Runnable
	{
		public synchronized void awake()
		{
			super.notify();
		}
		public synchronized void run()
		{
			while(!isStopExecuting)
			{
				IExecute task=nextTask();
				if(task==null)
					try {
						wait();
					} catch (InterruptedException e) {
						// ignore all error
					}
				else
				{
					currentTask=task;
//					taskCount++;
					try
					{
						task.run();
					}catch(Throwable t)
					{
						showMessage(t);
					}finally
					{
//						taskCount--;
					}
				}
				
			}
		}
	}
}
