/**
 * 
 */
package com.cattsoft.coolsql.view.sqleditor.action;

import java.awt.event.ActionEvent;
import java.util.List;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.system.menu.action.BaseEditorSelectAction;
import com.cattsoft.coolsql.view.SqlEditorView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-1 create
 */
public class MultiStatementExecuteAction extends BaseEditorSelectAction {

	private static final long serialVersionUID = 1L;
	public MultiStatementExecuteAction(){
		super();
		initMenuDefinitionById("MultiStatementExecuteAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void executeAction(ActionEvent e) {
		SqlEditorView view=ViewManage.getInstance().getSqlEditor();
		Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
		List<String> sqls=view.getQueries();
		ActionCommand command=new MultiStatementExecuteCommand(bookmark,sqls);
		try {
			command.exectue();
		} catch (Exception e1) {
			LogProxy.errorReport(e1);
		}
		
	}

}
