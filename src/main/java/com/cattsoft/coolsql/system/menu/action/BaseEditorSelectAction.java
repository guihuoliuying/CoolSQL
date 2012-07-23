/**
 * 
 */
package com.cattsoft.coolsql.system.menu.action;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.display.TextSelectionListener;
import com.cattsoft.coolsql.view.SqlEditorView;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-17 create
 */
public class BaseEditorSelectAction extends CsAction implements TextSelectionListener{

	private static final long serialVersionUID = 1L;

	//Indicate whether the connection of default bookmark should be checked when selection changed.
	protected boolean isCheckConnection;
	
	public BaseEditorSelectAction()
	{
		this(false);
	}
	public BaseEditorSelectAction(boolean isCheckConnection)
	{
		super();
		SqlEditorView view=ViewManage.getInstance().getSqlEditor();
		view.getEditorPane().addSelectionListener(this);
		this.isCheckConnection=isCheckConnection;
	}
	
	public void selectionChanged(int newStart, int newEnd)
	{
		boolean isConnected=true;
		if(isCheckConnection)
		{
			Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
			if(bookmark!=null)
				isConnected=bookmark.isConnected();
			else
				isConnected=false;
		}
		this.setEnabled(isConnected&&(newEnd > newStart));
	}
}
