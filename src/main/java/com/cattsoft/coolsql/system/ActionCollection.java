/**
 * 
 */
package com.cattsoft.coolsql.system;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * this class preloads all actions that will be used frequently in application
 * @author ��Т��(kenny liu)
 *
 * 2008-3-5 create
 */
public class ActionCollection {
	private static ActionCollection instance=null;
	
	public static ActionCollection getInstance()
	{
		if(instance==null)
			instance=new ActionCollection();
		
		return instance;
	}
	private ActionCollection()
	{}
	/** Collection of all Actions keyed by class name. */
	private final Map<String, Action> _actionColl = new HashMap<String, Action>();
	/**
	 * Add an <TT>Action</TT> to this collection. Normally <TT>get</TT> will
	 * do this &quot;on demand&quot; but this function can be used when
	 * there is no default ctor for the <TT>Action</TT>.
	 *
	 * @param	action	<TT>Action</TT> to be added.
	 *
	 * @throws	IllegalArgumentException
	 *			If a <TT>null</TT> <TT>Action</TT> passed.
	 */
	public void add(Action action)
	{
		if (action == null)
		{
			throw new IllegalArgumentException("Action == null");
		}
		_actionColl.put(action.getClass().getName(), action);
	}
	/**
	 * Returns the instance of the passed <TT>Action</TT> class that is stored
	 * in this collection.
	 *
	 * @param	actionClass	The <TT>Class</TT> of the <TT>Action</TT>
	 *						required. Because the instance is created
	 *						using <TT>newInstance()</TT> this <TT>Class</TT>
	 *						must have a default ctor.
	 *
	 * @throws	IllegalArgumentException	Thrown if a null action class passed.
	 */
	public synchronized Action get(Class<? extends Action> actionClass)
	{
		if (actionClass == null)
		{
			throw new IllegalArgumentException("null Action Class passed.");
		}

		return get(actionClass.getName());
	}

	/**
	 * Returns the instance of the passed <TT>Action</TT> class name that is
	 * stored in this collection.
	 *
	 * @param	actionClass	The <TT>Class</TT> of the <TT>Action</TT>
	 *						required. Because the instance is created
	 *						using <TT>newInstance()</TT> this <TT>Class</TT>
	 *						must have a default ctor.
	 *
	 * @throws	IllegalArgumentException	Thrown if a null action class passed.
	 */
	public synchronized Action get(String actionClassName)
	{
		if (actionClassName == null)
		{
			throw new IllegalArgumentException("null Action Class Name passed.");
		}

		Action action = _actionColl.get(actionClassName);
		if (action == null)
		{
			action = createAction(actionClassName);
		}
		return action;
	}

	/**
	 * Create a new instance of <TT>actionCassName</TT> and store in this
	 * collection.
	 *
	 * @param	actionClass	The name of the <TT>Class</TT> of the <TT>Action</TT>
	 *						required. Because the instance is created
	 *						using <TT>newInstance()</TT> this <TT>Class</TT>
	 *						must have a default ctor.
	 */
	private Action createAction(String actionClassName)
	{
		Action action = null;
		try {
			action = (Action) LoadJar.getInstance().getClassLoader().loadClass(actionClassName).newInstance();
			_actionColl.put(actionClassName, action);
		}
		catch (Exception ex)
		{
			LogProxy.errorReport("creating action in ActionCollection failed!", ex);
		}
		return action;
	}
}
