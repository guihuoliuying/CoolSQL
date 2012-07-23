/**
 * 
 */
package com.cattsoft.coolsql.system.task;

/**
 * when managing the executing of all tasks,it'll throw such exception if logic is illegal.
 * @author ��Т��(kenny liu)
 *
 * 2008-2-4 create
 */
public class TaskException extends Exception {

	/**
	 * 
	 */
	public TaskException() {
	}

	/**
	 * @param message
	 */
	public TaskException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public TaskException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TaskException(String message, Throwable cause) {
		super(message, cause);
	}

}
