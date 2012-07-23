/**
 * Create date:2008-5-18
 */
package com.cattsoft.coolsql.view.resultset;


/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-18 create
 */
public class TableModelModifyEvent extends java.util.EventObject {

	public static final int ACTION_TYPE_MODIFY=0;
	public static final int ACTION_TYPE_RESTORE=1;
	public static final int ACTION_TYPE_RESTROE_ALL=2;
	
	public static final int ACTION_TYPE_DELETE = 3;
	public static final int ACTION_TYPE_CANCEL_DELETE = 4;
	
	private static final long serialVersionUID = 1L;

	private int actionType;
	private Object oldValue;
	private Object newValue;
	
	private boolean hasModified;
	/**
	 * @param source
	 */
	public TableModelModifyEvent(Object source,int type,Object oldValue,Object newValue,boolean hasModified) {
		super(source);
		this.oldValue=oldValue;
		this.newValue=newValue;
		this.hasModified=hasModified;
		this.actionType = type;
	}
	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return this.oldValue;
	}
	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return this.newValue;
	}
	/**
	 * @return the actionType
	 */
	public int getActionType() {
		return this.actionType;
	}
	public boolean hasModified()
	{
		return this.hasModified;
	}
	
}
